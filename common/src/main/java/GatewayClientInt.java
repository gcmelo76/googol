import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GatewayClientInt extends Remote {
    void subscribe(ClientInt client) throws RemoteException;
    void indexUrl(String url) throws RemoteException;
    WebpagePage search(String[] words, int page) throws RemoteException;
    Webpage[] linkedPages(String url) throws RemoteException;
    }
