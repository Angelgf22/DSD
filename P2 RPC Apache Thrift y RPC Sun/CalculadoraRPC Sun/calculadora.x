

struct componentes{
	double a;
	double b;
};


program CALCULADORA {
	version CALCULADORAV1 {
		double SUMAR (componentes x)= 1;
		double RESTA (componentes x)= 2;
		double MULTIPLICAR (componentes x)= 3;
		double DIVIDIR (componentes x)= 4;
	}= 1;
}= 0x20000001;