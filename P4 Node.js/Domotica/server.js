var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
const nodemailer = require('nodemailer');
var socketio = require("socket.io");

var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mimeTypes = { "html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};




//Variables necesarias

//Sensores
var luminosidad= 50; // %
var temperatura= 25; // ºC
var gasto_energetico= 9; //kWh/dia

//Interruptores
var estado_persiana= false;
var estado_ac= false;
var modo_ahorro_energia= false;

//Mensajes de alarma y eventos
var alarmas_agente= [];
var eventos= [];





//Correo jeje
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'dsdp4ugr@gmail.com',
        pass: 'nodejsdsd'
    }
});

//Funcion del agente que controla los umbrales
function agente(){

    const min_lum = 20;
    const max_lum = 90;
    const min_temp = 15;
    const max_temp = 35;
    const min_ener= 3;
    const max_ener= 14;

    let alarma = '';
    alarmas_agente= [];

    if(temperatura <= min_temp){
        estado_ac= false;
        alarma= 'El agente apaga el aire acondicionado (temperatura actual baja).';
    }else if( temperatura >= max_temp) {
        estado_ac= true;
        alarma= 'El agente enciende el aire acondicionado (temperatura actual alta).';
    }
    if(alarma!=='')
        alarmas_agente.push(alarma);

    alarma='';

    if(luminosidad <= min_lum){
        estado_persiana= true;
        alarma= 'El agente sube la persiana (luminosidad actual baja).';
    }else if( luminosidad >= max_lum) {
        estado_persiana= false;
        alarma= 'El agente baja la persiana (luminosidad actual alta).';
    }
    if(alarma!=='')
        alarmas_agente.push(alarma);

    alarma='';

    if(gasto_energetico <= min_ener){
        modo_ahorro_energia= false;
        alarma= 'El agente sube la persiana (luminosidad actual baja).';
    }else if( gasto_energetico >= max_ener) {
        modo_ahorro_energia= true;
        alarma= 'El agente baja la persiana (luminosidad actual alta).';
    }
    if(alarma!=='')
        alarmas_agente.push(alarma);
}



//Crea servidor http
var httpServer = http.createServer(
    function(request, response) {
        var uri = url.parse(request.url).pathname;
        if (uri=="/") uri = "/sensores.html";
        var fname = path.join(process.cwd(), uri);
        fs.exists(fname, function(exists) {
            if (exists) {
                fs.readFile(fname, function(err, data){
                    if (!err) {
                        var extension = path.extname(fname).split(".")[1];
                        var mimeType = mimeTypes[extension];
                        response.writeHead(200, mimeType);
                        response.write(data);
                        response.end();
                    }
                    else {
                        response.writeHead(200, {"Content-Type": "text/plain"});
                        response.write('Error de lectura en el fichero: '+uri);
                        response.end();
                    }
                });
            }else{
                console.log("Peticion invalida: "+uri);
                response.writeHead(200, {"Content-Type": "text/plain"});
                response.write('404 Not Found\n');
                response.end();
            }
        });
    }
)


//Crea cliente de mongodb
MongoClient.connect("mongodb://localhost:27017/", {useNewUrlParser: true, useUnifiedTopology: true},function(err, db){
    var io = require("socket.io")(httpServer.listen(8080));
    var dbo = db.db("BD");

    dbo.createCollection('registroEventos', function(err, collection){
        io.sockets.on('connection',
            function(client) {

                //Inicializará los datos en el cliente
                updateClientData(true);

                //Inicializa el vector de eventos
                eventos= function(){
                    dbo.collection("registroEventos").find().toArray(function (err, results) {
                        return results;
                    });
                }

                //Recibe mensaje de interruptor para cambiar estado
                client.on('AC', function(){ estado_ac= !estado_ac;
                    updateClientData(false);
                });
                //Recibe mensaje de interruptor para cambiar estado
                client.on('persiana', function(){ estado_persiana= !estado_persiana;
                    updateClientData(false);
                });
                //Recibe mensaje de interruptor para cambiar estado
                client.on('ahorroEnergia', function(){ modo_ahorro_energia= !modo_ahorro_energia;
                    updateClientData(false);
                });

                //Actualizará el valor de un sensor recibido desde el cliente además de crear el evento y guardarlo en la BD
                client.on('updateTemp', function(data){
                    temperatura= data.temperatura;
                    var evento= {fecha: data.fecha, evento: 'La temperatura ha sido actualizada a: '+ data.temperatura /*+ ' por el usuario con direccion: '+ client.request.connection.remoteAddress +':'+ client.request.connection.remotePort*/};
                    addEvento(evento);
                });
                //Actualizará el valor de un sensor recibido desde el cliente además de crear el evento y guardarlo en la BD
                client.on('updateLum', function(data){
                    luminosidad= data.luminosidad;
                    var evento= {fecha: data.fecha, evento: 'La luminosidad ha sido actualizada a: '+ data.luminosidad /*+ ' por el usuario con direccion: '+ client.request.connection.remoteAddress +':'+ client.request.connection.remotePort*/};
                    addEvento(evento);
                });
                //Actualizará el valor de un sensor recibido desde el cliente además de crear el evento y guardarlo en la BD
                client.on('updateEner', function(data){
                    gasto_energetico= data.energia;
                    var evento= {fecha: data.fecha, evento: 'El gasto energetico ha sido actualizado a: '+ data.energia /*+ ' por el usuario con direccion: '+ client.request.connection.remoteAddress +':'+ client.request.connection.remotePort*/};
                    addEvento(evento);
                });

                //Insertará el evento en la base de datos compuesto por la {fecha, evento}
                function addEvento(evento){
                    dbo.collection("registroEventos").insertOne(evento, {safe:true}, function(err, result) {
                        updateClientData(true);
                    });
                }

                //Actualizará los datos de los clientes al realizarse cambios
                //Si getEventos es true además pedira los eventos a la base de datos (true si es necesario)
                function updateClientData(getEventos){
                    if(getEventos) {
                        dbo.collection("registroEventos").find().toArray(function (err, results) {
                            eventos = results;
                            agente();
                            emitData();
                        });
                    }else{
                        emitData();
                    }
                }
                //Función encargada de enviar los datos a los clientes
                function emitData(){
                    io.sockets.emit('data', {
                        temperatura: temperatura,
                        luminosidad: luminosidad,
                        energia: gasto_energetico,
                        estadoAC: estado_ac,
                        estadoPersiana: estado_persiana,
                        modoAhorro: modo_ahorro_energia,
                        eventos: eventos,
                        alarmasAgente: alarmas_agente
                    });
                }

                //Función auxiliar para borrar los eventos de la BD al ser llamada desde el cliente
                client.on('borrarRegistros', function(){
                    dbo.collection("registroEventos").deleteMany({},{}, function(err, result) {
                        //console.log(err);
                        updateClientData(true);
                    });
                });

                //Al recibir el mensaje enviará un email al correo introducido y enviará la confimación al cliente
                client.on('enviarEmail', async function (data) {
                    if(data.correo != null) {
                        let enviado = false;
                        let texto = 'Aire Acondicionado: ' + estado_ac +
                            '\nPersiana: ' + estado_persiana +
                            '\nModo ahorro de energía: ' + modo_ahorro_energia +
                            '\n---------Sensores---------' +
                            '\nLuminosidad: ' + luminosidad +
                            '\nTemperatura: ' + temperatura +
                            '\nGasto energetico: ' + gasto_energetico;
                        const mailOptions = {
                            from: 'dsdp4ugr@gmail.com',
                            to: data.correo,
                            subject: 'Estado de la casa',
                            text: texto
                        };

                        await transporter.sendMail(mailOptions, function (error, info) {
                            if (error) {
                                console.log(error);
                            } else {
                                console.log('Email sent: ' + info.response + ' a la direccion ' + data.correo);
                                enviado = true;
                            }
                            client.emit('correoconf', {enviado: enviado});
                        });
                    }
                    //console.log(data.correo);
                });

            });
        });
    });

console.log("Servidor iniciado");