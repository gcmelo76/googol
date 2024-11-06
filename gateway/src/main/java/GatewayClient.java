import com.common.ClientInt;
import com.common.GatewayClientInt;
import com.common.Webpage;
import com.common.WebpagePage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GatewayClient extends UnicastRemoteObject implements GatewayClientInt {

    Set<ClientInt> clients;
    MainGateway gateway;
    HashMap<String, Integer> topSearches;

    protected GatewayClient(MainGateway gateway) throws RemoteException {
        super();
        this.topSearches = new HashMap<>();
        this.clients = new HashSet<>();
        this.gateway = gateway;
    }

    
    /** 
     * @return ArrayList<String>
     */
    private ArrayList<String> getTopSearches(){
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(topSearches.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        ArrayList<String> topSearchesList = new ArrayList<>();
        int maxSize = Math.min(sortedEntries.size(), 10);
        for (int i = 0; i < maxSize; i++) {
            topSearchesList.add(sortedEntries.get(i).getKey());
        }
        return topSearchesList;
    }

    public void adminBoard(){
        for (ClientInt client: clients){
            try{
                client.adminBoard(gateway.gatewayBarrel.barrels, getTopSearches(), gateway.gatewayBarrel.getBarrelMetrics());
            } catch (RemoteException e){
                clients.remove(client);
            }
        }
    }

    
    /** 
     * @param client
     */
    @Override
    public void subscribe(ClientInt client) {
        try {
            clients.add(client);
            client.adminBoard(gateway.gatewayBarrel.barrels, getTopSearches(), gateway.gatewayBarrel.getBarrelMetrics());
        } catch (RemoteException e){
            System.err.println("Client died "+ e.getMessage());
        } catch (Exception e) {
            System.err.println("Error" + e.getMessage());
        }
    }


    @Override
    public void indexUrl(String url) {
        try {
            gateway.gatewayDownloader.push(url, true);
        } catch (RemoteException e ){
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public Webpage[] linkedPages(String url) {
        try {
            return gateway.gatewayBarrel.linkedPages(url);
        } catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public WebpagePage search(String[] words, int page) {
        try {
            String query = String.join(" ", words);
            if(topSearches.containsKey(query)){
                topSearches.put(query, topSearches.get(query) + 1);
            } else {
                topSearches.put(query, 1);
            }
            WebpagePage result = gateway.gatewayBarrel.search(words, page);
            adminBoard();
            return result;
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("Error searching: " + e.getMessage());
        }
        return null;
    }
}
