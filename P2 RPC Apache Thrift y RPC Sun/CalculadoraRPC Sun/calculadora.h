/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#ifndef _CALCULADORA_H_RPCGEN
#define _CALCULADORA_H_RPCGEN

#include <rpc/rpc.h>


#ifdef __cplusplus
extern "C" {
#endif


struct componentes {
	double a;
	double b;
};
typedef struct componentes componentes;

#define CALCULADORA 0x20000001
#define CALCULADORAV1 1

#if defined(__STDC__) || defined(__cplusplus)
#define SUMAR 1
extern  double * sumar_1(componentes , CLIENT *);
extern  double * sumar_1_svc(componentes , struct svc_req *);
#define RESTA 2
extern  double * resta_1(componentes , CLIENT *);
extern  double * resta_1_svc(componentes , struct svc_req *);
#define MULTIPLICAR 3
extern  double * multiplicar_1(componentes , CLIENT *);
extern  double * multiplicar_1_svc(componentes , struct svc_req *);
#define DIVIDIR 4
extern  double * dividir_1(componentes , CLIENT *);
extern  double * dividir_1_svc(componentes , struct svc_req *);
extern int calculadora_1_freeresult (SVCXPRT *, xdrproc_t, caddr_t);

#else /* K&R C */
#define SUMAR 1
extern  double * sumar_1();
extern  double * sumar_1_svc();
#define RESTA 2
extern  double * resta_1();
extern  double * resta_1_svc();
#define MULTIPLICAR 3
extern  double * multiplicar_1();
extern  double * multiplicar_1_svc();
#define DIVIDIR 4
extern  double * dividir_1();
extern  double * dividir_1_svc();
extern int calculadora_1_freeresult ();
#endif /* K&R C */

/* the xdr functions */

#if defined(__STDC__) || defined(__cplusplus)
extern  bool_t xdr_componentes (XDR *, componentes*);

#else /* K&R C */
extern bool_t xdr_componentes ();

#endif /* K&R C */

#ifdef __cplusplus
}
#endif

#endif /* !_CALCULADORA_H_RPCGEN */
