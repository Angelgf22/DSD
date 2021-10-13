
struct vectores{
	double v1<>;
	double v2<>;
};

struct res_v{
    double v<>;
};

program CALCULADORA {
	version CALCULADORAVECTORESV1 {
		res_v SUMA_VECTORES (vectores)= 1;
	}= 1;
}= 0x20000001;