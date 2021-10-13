/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "calculadoravectores.h"


res_v *
suma_vectores_1_svc(vectores arg1,  struct svc_req *rqstp)
{

    static res_v  result;
    xdr_free(xdr_res_v, &result);

    //Los dos vectores tendrán el mismo tamaño.
    result.v.v_len= arg1.v1.v1_len;

    //reservo
    result.v.v_val= malloc(sizeof(double) * result.v.v_len);

    for(u_int i=0; i < arg1.v1.v1_len , i < arg1.v2.v2_len; ++i){
        result.v.v_val[i]= arg1.v1.v1_val[i] + arg1.v2.v2_val[i];
    }

    return &result;
}
