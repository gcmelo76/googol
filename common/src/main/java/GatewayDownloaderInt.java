import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GatewayDownloaderInt extends Remote {
    void push(String url, boolean first) throws RemoteException;

    String pop() throws RemoteException, InterruptedException;

}
