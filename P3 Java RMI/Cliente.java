import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {
    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        Scanner in = new Scanner(System.in);

        if (args.length!=2) {
            System.out.println("Poner como parametro el nombre de usuario");
        }

        try {
            String nombre_objeto_remoto = "Server";
            System.out.println("Buscando el objeto remoto");
            Registry registry = LocateRegistry.getRegistry(args[0]);
            Server_I instancia_local = (Server_I) registry.lookup(nombre_objeto_remoto);
            System.out.println("Invocando el objeto remoto");
            String opc="";
            do{
                System.out.println("\n\t|----\tMenu\t----|\t\n"
                        + "\t-1: Registrarse\n"
                        + "\t-2: Donar\n"
                        + "\t-3: Consultar total donado\n"
                        + "\t-0: Salir\n");
                 opc= in.nextLine();

                 switch (Integer.parseInt(opc)){
                     case 1:
                         instancia_local.registrar(args[1]);
                         break;
                     case 2:
                         System.out.println("\n- ¿Que cantidad desea donar?");
                         String cant= in.nextLine();
                         instancia_local.donar(args[1], Integer.parseInt(cant));

                         break;
                     case 3:
                         int aux= instancia_local.totalDonado(args[1]);
                         if(aux<=0){
                             System.out.println("\nDebe registrarse y donar antes de poder ver este dato.");
                         }else{
                            System.out.println("\n- La cantidad donada total es de: "+aux);
                         }
                         break;
                 }
            }while(!opc.equals("0"));

        } catch (Exception e) {
            System.err.println("Cliente excp.");
            e.printStackTrace();
        }
    }
}
