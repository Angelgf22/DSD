import glob
import sys

from calculadora import Calculadora

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

import numpy as np
import logging

logging.basicConfig(level=logging.DEBUG)


class CalculadoraHandler:
    def __init__(self):
        self.log = {}

    def ping(self):
        print("me han hecho ping()")

    def suma(self, n1, n2):
        print("\nsumando " + str(n1) + " con " + str(n2))
        return n1 + n2

    def resta(self, n1, n2):
        print("\nrestando " + str(n1) + " con " + str(n2))
        return n1 - n2

    def division(self, n1, n2):
            print("\ndividiendo " + str(n1) + " entre " + str(n2))
            return float(n1) / float(n2)

    def multiplicacion(self, n1, n2):
        print("\nmultiplicando " + str(n1) + " por " + str(n2))
        return n1*n2

    def sumavectores(self, v1, v2):
        print("\nSe van a sumar: ", v1, " y ", v2)
        return [ x + y for x, y in zip(v1, v2) ]

    def sumamatrices(self, m1, m2):
        print("Se van a sumar: ")
        for i in m1:
            print(i)

        print(" y ")

        for i in m2:
            print(i)
        return np.add(m1, m2)

    def multiplicacionmatrices(self, m1, m2):
        print("Se van a multiplicar: ")
        for i in m1:
            print(i)

        print(" y ")

        for i in m2:
            print(i)
        return np.dot(m1, m2)




if __name__ == "__main__":
    handler = CalculadoraHandler()
    processor = Calculadora.Processor(handler)
    transport = TSocket.TServerSocket(host="127.0.0.1", port=9090)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

    print("iniciando servidor...")
    server.serve()
    print("fin")
