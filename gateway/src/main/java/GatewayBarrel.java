import com.common.BarrelInt;
import com.common.BarrelMetrics;
import com.common.GatewayBarrelInt;
import com.common.Webpage;
import com.common.WebpagePage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GatewayBarrel extends UnicastRemoteObject implements GatewayBarrelInt {
    ArrayList<Integer> barrels;
    Map<Integer, BarrelInt> barrelsInt;
    Map<Integer, BarrelMetrics> metrics;
    MainGateway gateway;
    int lastBarrel;

    protected GatewayBarrel(MainGateway gateway) throws RemoteException {
        super();
        this.barrels = new ArrayList<>();
        this.barrelsInt = new ConcurrentHashMap<>();
        this.metrics = new ConcurrentHashMap<>();
        this.gateway = gateway;
        this.lastBarrel = 0;
    }

    
    /** 
     * @return ArrayList<BarrelMetrics>
     */
    public ArrayList<BarrelMetrics> getBarrelMetrics(){
        return new ArrayList<>(metrics.values());
    }

    
    /** 
     * @param url
     * @return Webpage[]
     */
    public Webpage[] linkedPages(String url){
        Webpage[] result;
        while (!barrels.isEmpty()){
            int currentBarrel = barrels.get(lastBarrel);
            BarrelInt barrel = barrelsInt.get(currentBarrel);
            try {
                result = barrel.linkedPages(url);
                lastBarrel = (lastBarrel + 1) % barrels.size();
                return result;
            } catch (RemoteException e){
                barrels.remove(lastBarrel);
                if(lastBarrel == barrels.size()){
                    lastBarrel = 0;
                }
                metrics.remove(currentBarrel);
                barrelsInt.remove(currentBarrel);
                gateway.gatewayClient.adminBoard();
            }
        }
        return null;
    }

    public WebpagePage search(String[] words, int page){
        WebpagePage result;
        while (!barrels.isEmpty()){
            int currentBarrel = barrels.get(lastBarrel);
            BarrelInt barrel = barrelsInt.get(currentBarrel);
            try {
                long startTime = System.currentTimeMillis();
                result = barrel.search(words, page);
                long endTime = System.currentTimeMillis();
                lastBarrel = (lastBarrel + 1) % barrels.size();
                metrics.get(currentBarrel).addSearch(endTime - startTime);
                return result;
            } catch (RemoteException e){
                System.out.println("Remote exception: " + e.getMessage());
                System.out.println("Removing barrel: " + currentBarrel);
                barrels.remove(lastBarrel);
                if(lastBarrel == barrels.size()){
                    lastBarrel = 0;
                }
                metrics.remove(currentBarrel);
                barrelsInt.remove(currentBarrel);
                gateway.gatewayClient.adminBoard();
            }
        }
        return null;
    }

    @Override
    public boolean subscribe(int id, BarrelInt barrel){
        if(barrels.contains(id)){
            try {
                barrelsInt.get(id).test();
                System.out.println("There is already a barrel with that id");
                gateway.gatewayClient.adminBoard();
                return false;
            } catch (RemoteException e){
                barrelsInt.put(id, barrel);
                System.out.println("Subscribed successfully");
                return true;
            }
        }else {
            barrels.add(id);
            barrelsInt.put(id, barrel);
            metrics.put(id, new BarrelMetrics(id));
            System.out.println("Subscribed successfully");
            gateway.gatewayClient.adminBoard();
            return true;
        }
    }
}
