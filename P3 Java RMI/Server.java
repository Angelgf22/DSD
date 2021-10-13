
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server implements Server_I {
	private int cantidadLocal;
	private ArrayList<String> usuarios;
	private ArrayList<Integer> donado; //(Por usuario) el usuario: usuarios[1] habra donado: donado[1]
	private Server_I replica;

	public Server() {
		super();
		cantidadLocal= 0;
		usuarios= new ArrayList<String>();
		donado= new ArrayList<Integer>();
	}


	//-------------------------------------------------------------------------
	//Principales
	@Override
	public void registrar(String usuario) throws RemoteException{
		try {
			if(serverRegistrado(usuario)!=-1){
				try {
					System.out.println("Ya esta registrado en el sistema");
				}catch (Exception a){
					System.err.println("Excepcion Registro");
				}
			}else{
				try {
					if(this.usuarios.size() <= this.replica.getUsuarios().size()){
						this.usuarios.add(usuario);
						this.donado.add(0);
						System.out.println("Registrado "+ usuario +" en origen.");
					}else{
						//ArrayList<String> aux= this.replica.getUsuarios();
						//aux.add(usuario);
						this.replica.addUsuarios(usuario);
						this.replica.addDonado(0);
						this.replica.mostrarVerificacion("Registrado "+ usuario +" en replica.");
					}
				}catch (Exception a){
					System.err.println("Excepcion Registro introducir usuario");
				}
			}
		}catch (Exception a){
			System.err.println("Excepcion Comprobacion serverRegistrado");
		}
	}

	@Override
	public void donar(String usuario, int cantidad) throws RemoteException{
		
		if(cantidad > 0){
			int sv= serverRegistrado(usuario);
			if(sv!=-1){
				if(sv==0){
					this.addDonacion(cantidad);
					this.incrementoDonador(usuario, cantidad);
					this.mostrarVerificacion("Se han donado "+ cantidad + " por el cliente: "+ usuario);

				}else if(sv==1){
					this.replica.addDonacion(cantidad);
					this.replica.incrementoDonador(usuario, cantidad);
					this.replica.mostrarVerificacion("Se han donado "+ cantidad + " por el cliente: "+ usuario);

				}
			}else{
				System.out.println("Debe registrarse antes de donar.");

			}
		}

	}

	@Override
	public int totalDonado(String usuario) throws RemoteException{
		int a= serverRegistrado(usuario);
		int res=0;
		if(a != -1){
			if(this.getDonadoUsuario(usuario) != 0 || this.replica.getDonadoUsuario(usuario) != 0){
				res= this.cantidadLocal + this.replica.getCantidadLocal();
			}
		}else{
			return a;
		}
		return res; //-1 no registrado
	}


	//-------------------------------------------------------------------------
	//Auxiliares
	public void guardarRefServer(Server_I replica) throws RemoteException{
		this.replica= replica;
	}

	public int getCantidadLocal() throws RemoteException{
		return this.cantidadLocal;
	}

	public int serverRegistrado(String usuario) throws RemoteException{

		if (usuarios.contains(usuario)) {
			return 0;
		} else if (this.replica.getUsuarios().contains(usuario)) {
			return 1;
		}else {
			return -1;
		}
	}

	public void mostrarVerificacion(String m) throws RemoteException{
		System.out.println(m);
	}


	//-------------------------------------------------------------------------
	//Gestion de array de usuarios y registro
	public void addUsuarios(String user) throws RemoteException{
		this.usuarios.add(user);
	}
	public ArrayList<String> getUsuarios() throws RemoteException{
		return this.usuarios;
	}

	//-------------------------------------------------------------------------
	//Gestion de donaciones
	public void addDonacion(int cantidad){
		this.cantidadLocal+= cantidad;
	}


	//-------------------------------------------------------------------------
	//Gestion de donaciones por usuario

	public void addDonado(Integer i) throws RemoteException{
		this.donado.add(i);
	}

	public void incrementoDonador(String usuario, Integer cantidad) throws RemoteException {
		int sv= serverRegistrado(usuario);
		if(sv==0){
			int i= this.usuarios.indexOf(usuario);
			System.out.println(usuarios.toString());
			if(i!=-1){
				this.donado.set(i, this.donado.get(i) + cantidad);
			}
		}else if(sv==1){

			int i= this.replica.getUsuarios().indexOf(usuario);

			System.out.println(this.replica.getUsuarios().toString());
			if(i!=-1){
				ArrayList<Integer> aux= this.replica.getDonado();
				aux.set(i, this.donado.get(i) + cantidad);
				this.replica.setDonado(aux);
			}
		}
	}

	public void setDonado(ArrayList<Integer> donado) throws RemoteException{
		this.donado= donado;
	}

	public ArrayList<Integer> getDonado() throws RemoteException{
		return this.donado;
	}

	public int getDonadoUsuario(String usuario) throws RemoteException{

		int sv= serverRegistrado(usuario);
		if(sv==0){
			if(this.donado.size()>0){


			int i= this.usuarios.indexOf(usuario);
			if(i!=-1){
				return this.donado.get(i);
			}
			}else{
				return 0;
			}
		}else if (sv==1){

			if(this.replica.getDonado().size()>0){
				int i= this.replica.getUsuarios().indexOf(usuario);
				if(i!=-1){
					return this.replica.getDonado().get(i);
				}
			}else{
				return 0;
			}
		}

		return 0;
	}

	//Auxiliar sin necesidad de conexion entre servidores.
	public void conectarConOrigen(String ip, String nObjRemoto){
		try{
				Registry r= LocateRegistry.getRegistry(ip);
				this.replica= (Server_I) r.lookup(nObjRemoto);  //realmente en la replica "replica" seria el padre
			    this.replica.guardarRefServer(this);
		}catch(Exception e){
			System.err.println("Excp. Server conectar con server origen.");
		}
	}

	public static void main(String[] args) {
		if(args.length < 2){
			System.out.println("Argumento 0 1 (0 Server, 1 Replica)");
			System.exit(0);
		}

		if(System.getSecurityManager() == null){
			System.setSecurityManager(new SecurityManager());
		}

		try{

			String nombre_objeto_remoto = "";

			if(Integer.parseInt(args[1])== 0){
				nombre_objeto_remoto = "Server";
			}else if(Integer.parseInt(args[1])== 1){
				nombre_objeto_remoto = "Replica";
			}

			Server_I servidor= new Server();
			Server_I  stub= (Server_I) UnicastRemoteObject.exportObject(servidor, 0);
			Registry registry= LocateRegistry.getRegistry();
			registry.rebind(nombre_objeto_remoto, stub);
			System.out.println("Servidor iniciado!");

			if(Integer.parseInt(args[1])== 1){
				((Server) servidor).conectarConOrigen(args[0],"Server");
			}

			System.out.println("Servidor conectado!");
		}catch(Exception e) {
			System.err.println("Excp. main:");
			e.printStackTrace();
		}

	}
}
