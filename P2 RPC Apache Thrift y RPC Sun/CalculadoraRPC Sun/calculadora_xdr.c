/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "calculadora.h"

bool_t
xdr_componentes (XDR *xdrs, componentes *objp)
{
	register int32_t *buf;

	 if (!xdr_double (xdrs, &objp->a))
		 return FALSE;
	 if (!xdr_double (xdrs, &objp->b))
		 return FALSE;
	return TRUE;
}
