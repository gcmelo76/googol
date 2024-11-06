import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BarrelInt extends Remote {
    void test() throws RemoteException;
    WebpagePage search(String[] words, int page) throws RemoteException;
    Webpage[] linkedPages(String url) throws RemoteException;
}