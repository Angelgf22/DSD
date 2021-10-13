
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Server_I extends Remote {
    //Principales
    public void registrar(String user) throws RemoteException;
    public void donar (String user, int valor) throws RemoteException;
    public int totalDonado(String usuario) throws RemoteException;

    //Auxiliares
    public void guardarRefServer(Server_I replica) throws RemoteException;
    public int getCantidadLocal() throws RemoteException;
    public int serverRegistrado(String usuario) throws RemoteException;
    public void mostrarVerificacion(String m) throws RemoteException;

    //Gestion de array de usuarios y registro
    public ArrayList<String> getUsuarios() throws RemoteException;
    public void addUsuarios(String user) throws RemoteException;

    //Gestion de donaciones
    public void addDonacion(int cantidad) throws RemoteException;

    //Gestion de donaciones por usuario
    public void addDonado(Integer i) throws RemoteException;
    public ArrayList<Integer> getDonado() throws RemoteException;
    public void setDonado(ArrayList<Integer> donado) throws RemoteException;
    public void incrementoDonador(String usuario, Integer cantidad) throws RemoteException;
    public int getDonadoUsuario(String usuario) throws RemoteException;
}
