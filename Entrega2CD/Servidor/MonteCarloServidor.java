

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MonteCarloServidor {
    public static void main (String[] args) {
        Scanner entradaDatos = new Scanner(System.in);
        String registroURL;
        int numeroPuerto;
        try {
            System.out.println("Introduce el numero de puerto del registro RMI: ");
            numeroPuerto = entradaDatos.nextInt();
            comprobarRegistro(numeroPuerto);
            GeneradorParesAleatoriosImp obj = new GeneradorParesAleatoriosImp();
            registroURL = "rmi://localhost:" + String.valueOf(numeroPuerto) +
                    "/GeneradorParesAleatorios";
            Naming.rebind(registroURL, obj);
            listaRegistros(registroURL);
            System.out.println("Servidor registrado y listo para funcionar.");
        } catch (RemoteException e) {
            System.err.println("RemoteException: " + e.getMessage());
        } catch (MalformedURLException a) {
            System.err.println("MalformedURLException: " + a.getMessage());
        }
    }
    
    public static void comprobarRegistro (int puerto) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(puerto);
            registry.list( );  
        } catch (RemoteException e) { 
            System.out.println ("RMI registry cannot be located at port " 
                + puerto);
            Registry registry = LocateRegistry.createRegistry(puerto);
            System.out.println("RMI registry created at port " + puerto);
      }
    }
    
    private static void listaRegistros (String registroURL)
     throws RemoteException, MalformedURLException {
       System.out.println("Registro " + registroURL + " tiene: ");
       String [ ] names = Naming.list(registroURL);
       for (int i=0; i < names.length; i++)
          System.out.println(names[i]);
    }

}

