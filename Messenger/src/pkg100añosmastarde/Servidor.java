package pkg100añosmastarde;

import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Servidor {

    public static void main(String args[]) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is); //necesario para leer las entradas del usuario
        ConexionUsuarios con = new ConexionUsuarios();
        ArrayList<String> prueba = new ArrayList<String>();
        String portNum, registryURL;

        try {
            System.out.println("Introduce el número de puerto del registro RMI: ");
            portNum = "1099";//(br.readLine()).trim(); 
            int RMIPortNum = 1099;//Integer.parseInt(portNum);
            startRegistry(RMIPortNum);
            ImpServer exportedObj = new ImpServer();
            registryURL = "rmi://localhost:" + portNum + "/servidorMensajes";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Servidor registrado.  El registro actualmente contiene:");
            listRegistry(registryURL);
            System.out.println("\nServidor de aplicación P2P disponible...");
            Iterator<String> it = prueba.iterator();
            while (it.hasNext()) {
                System.out.println(it.next());
            }
            
        } catch (Exception re) {
            System.out.println("Excepción en el servidor: " + re);
        }
    }

    // This method starts a RMI registry on the local host, if it
    // does not already exists at the specified port number.
    private static void startRegistry(int RMIPortNum)
            throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();  // This call will throw an exception
            // if the registry does not already exist
        } catch (RemoteException e) {
            // No valid registry at that port.
            System.out.println("El registro rmi no se pudo localizar en el puerto " + RMIPortNum);
            Registry registry
                    = LocateRegistry.createRegistry(RMIPortNum);
            /**/ System.out.println(
                    /**/"Registro rmi creado en el puerto " + RMIPortNum);
        }
    }

    // This method lists the names registered with a Registry object
    private static void listRegistry(String registryURL)
            throws RemoteException, MalformedURLException {
        System.out.println("El registro " + registryURL + " contiene: ");
        String[] names = Naming.list(registryURL);

        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
        }
    }

}
