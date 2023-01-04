package Paquete;
import java.rmi.*;

public interface CallbackServerInterface extends Remote {

    public void registerForCallback(CallbackClientInterface callbackClientObject, String nombreEmpresa,
                                    float valorUmbral, boolean Venta) throws java.rmi.RemoteException;

    public void unregisterForCallback(CallbackClientInterface callbackClientObject) throws java.rmi.RemoteException;
}
