
import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class JavaClient {
    public static void main(String [] args) {

        try {
            TTransport transport;
            transport = new TSocket("127.0.0.1", 9090);
            transport.open();

            TProtocol protocol = new  TBinaryProtocol(transport);
            Calculadora.Client client = new Calculadora.Client(protocol);

            perform(client);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static void perform(Calculadora.Client client) throws TException
    {
        client.ping();
        System.out.println("ping()");

        double res = client.suma(1,1);
        System.out.println("1+1=" + res);

        res = client.resta(1,1);
        System.out.println("1-1=" + res);

        res = client.multiplicacion(2,2);
        System.out.println("2*2=" + res);

        res = client.division(4,2);
        System.out.println("4/2=" + res);

    }
}