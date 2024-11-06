import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GatewayBarrelInt extends Remote {
    boolean subscribe(int id, BarrelInt barrel) throws RemoteException;
}
