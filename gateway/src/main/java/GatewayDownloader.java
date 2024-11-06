import com.common.GatewayDownloaderInt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.LinkedBlockingDeque;

public class GatewayDownloader extends UnicastRemoteObject implements GatewayDownloaderInt {
    LinkedBlockingDeque<String> queue;
    MainGateway gateway;
    protected GatewayDownloader(MainGateway gateway) throws RemoteException {
        super();
        this.queue = new LinkedBlockingDeque<>();
        this.gateway = gateway;
    }

    
    /** 
     * @param url
     * @param first
     * @throws RemoteException
     */
    @Override
    public void push(String url, boolean first) throws RemoteException {
        // Avoids adding too much repeated urls
        if(queue.contains(url)){
            return;
        }
        if(first) {
            queue.addFirst(url);
        }else {
            queue.addLast(url);
        }
        System.out.println("Added to queue " + url);
    }

    
    /** 
     * @return String
     * @throws RemoteException
     * @throws InterruptedException
     */
    @Override
    public String pop() throws RemoteException, InterruptedException {
        return queue.takeFirst();
    }

}
