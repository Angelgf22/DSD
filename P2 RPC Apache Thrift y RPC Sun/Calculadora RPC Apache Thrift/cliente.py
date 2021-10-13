from calculadora import Calculadora

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

transport = TSocket.TSocket("localhost", 9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

client = Calculadora.Client(protocol)

transport.open()

print("hacemos ping al server")
client.ping()
n1= 2
n2= 3
resultado = client.suma(n1, n2)
print(str(n1) + " + " + str(n2) + " = " + str(resultado))
resultado = client.resta(n1, n2)
print(str(n1) + " - " + str(n2) + " = " + str(resultado))
resultado = client.multiplicacion(n1, n2)
print(str(n1) + " * " + str(n2) + " = " + str(resultado))

n2= 3
try:
    n1/n2
except ZeroDivisionError:
        print("Error: ", ZeroDivisionError)
else:
    resultado = client.division(n1, n2)
    print(str(n1) + " / " + str(n2) + " = " + str(resultado))

v1 = [2, 4, 4]
v2 = [3, 2, 1]
resultado= client.sumavectores(v1, v2)
print("La suma de: ", v1, " y ", v2, "es: ", resultado)

m1= [v1, v1, v1]
m2= [v2, v2, v2]

print("Se van a sumar: ")
for i in m1:
    print(i)

print("\n y \n")

for i in m2:
    print(i)

resultado= client.sumamatrices(m1, m2)

print("\nEl resultado es: ")
for i in resultado:
    print(i)

m1= [v1, v1, v1]
m2= [v2, v2, v2]



print("Se van a multiplicar: ")
for i in m1:
    print(i)

print("\n y \n")

for i in m2:
    print(i)

resultado= client.multiplicacionmatrices(m1, m2)

print("\nEl resultado es: ")
for i in resultado:
    print(i)


transport.close()
