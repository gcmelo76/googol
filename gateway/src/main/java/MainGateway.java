import com.common.PropertiesReader;

import java.rmi.RemoteException;
import java.util.Objects;

public class MainGateway {
    GatewayDownloader gatewayDownloader;
    GatewayClient gatewayClient;
    GatewayBarrel gatewayBarrel;
    int PORT;

    public MainGateway() throws RemoteException {
        this.gatewayDownloader = new GatewayDownloader(this);
        this.gatewayClient = new GatewayClient(this);
        this.gatewayBarrel = new GatewayBarrel(this);
        this.PORT = Integer.parseInt(Objects.requireNonNull(PropertiesReader.read("PORT")));
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        try {
            MainGateway gateway = new MainGateway();
            java.rmi.registry.LocateRegistry.createRegistry(gateway.PORT);
            java.rmi.Naming.rebind("gatewayClient", gateway.gatewayClient);
            java.rmi.Naming.rebind("gatewayBarrel", gateway.gatewayBarrel);
            java.rmi.Naming.rebind("gatewayDownloader", gateway.gatewayDownloader);
            System.out.println("Gateway running");
        } catch (Exception e){
            System.err.println("Error:" + e.getMessage());
        }
    }
}
