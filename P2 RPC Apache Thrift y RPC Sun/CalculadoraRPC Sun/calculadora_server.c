/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "calculadora.h"

double *
sumar_1_svc(componentes x,  struct svc_req *rqstp)
{
    static double result;
    result= x.a + x.b;
    return &result;
}

double *
resta_1_svc(componentes x,  struct svc_req *rqstp)
{
    static double  result;
    result= x.a - x.b;
    return &result;
}

double *
multiplicar_1_svc(componentes x,  struct svc_req *rqstp)
{
    double static result;
    result= x.a * x.b;
    return &result;
}

double *
dividir_1_svc(componentes x,  struct svc_req *rqstp)
{
    double static result;
    result= x.a / x.b;
    return &result;
}


