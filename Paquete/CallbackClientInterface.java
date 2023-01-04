package Paquete;

public interface CallbackClientInterface  extends java.rmi.Remote {
    public String notifyMe(String message)
            throws java.rmi.RemoteException;
}
