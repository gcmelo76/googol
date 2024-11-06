import com.common.BarrelInt;
import com.common.GatewayBarrelInt;
import com.common.PropertiesReader;
import com.common.Webpage;
import com.common.WebpagePage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Barrel extends UnicastRemoteObject implements BarrelInt {

    int id;
    String RMI_ADDRESS;
    GatewayBarrelInt gatewayBarrel;
    String MULTICAST_ADDRESS;
    int MULTICAST_PORT;
    MulticastSocket socket;
    InetAddress multicastGroup;
    HashMap<String, HashSet<String>> values;
    HashMap<String, ArrayList<String>> urls;
    HashMap<String, HashSet<String>> urlsLinkTo;


    public Barrel() throws IOException, NotBoundException {
        this.urlsLinkTo = new HashMap<>();
        this.values = new HashMap<>();
        this.urls = new HashMap<>();
        this.RMI_ADDRESS = PropertiesReader.read("RMI_ADDRESS");
        this.MULTICAST_ADDRESS = PropertiesReader.read("MULTICAST_ADDRESS");
        this.MULTICAST_PORT = Integer.parseInt(Objects.requireNonNull(PropertiesReader.read("MULTICAST_PORT")));
        this.gatewayBarrel = (GatewayBarrelInt) Naming.lookup("rmi://"+this.RMI_ADDRESS+"/gatewayBarrel");
        this.socket = new MulticastSocket(this.MULTICAST_PORT);
        this.multicastGroup = InetAddress.getByName(this.MULTICAST_ADDRESS);
        this.socket.joinGroup(new InetSocketAddress(multicastGroup, 0), NetworkInterface.getByIndex(0));
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce barrel id (integer): ");
        this.id = scanner.nextInt();
        scanner.close();
        if(!gatewayBarrel.subscribe(id, this)) {
            System.out.println("Id already in use");
            System.exit(-1);
        } else {
            System.out.println("Barrel started with id " + id);
        }
    }

    
    /** 
     * @return JSONObject
     */
    private JSONObject receiveMessage(){
        try {
            byte[] buffer = new byte[64000];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            return new JSONObject(new String(buffer));
        } catch (IOException e) {
            System.err.println("Error receiving message: " + e.getMessage());
        }
        return null;
    }

    
    /** 
     * @param url
     * @param text
     * @param title
     */
    public void addUrl(String url, String text, String title){
        urls.put(url, new ArrayList<>());
        urls.get(url).add(title);
        urls.get(url).add(text);
    }

    public void indexURL(String url, String text){
        String[] words = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("\\p{Punct}", "")
                .toLowerCase()
                .split("\\s+");
        for (String word: words){
            if(values.containsKey(word)){
                values.get(word).add(url);
            }else {
                values.put(word, new HashSet<>());
                values.get(word).add(url);
            }
            if(!urlsLinkTo.containsKey(url)) {
                urlsLinkTo.put(url, new HashSet<>());
            }
        }
    }

    public void addLink(String from, String to){
        if(urlsLinkTo.containsKey(to)){
            urlsLinkTo.get(to).add(from);
        }else {
            urlsLinkTo.put(to, new HashSet<>());
            urlsLinkTo.get(to).add(from);
        }
    }

    @Override
    public void test() throws RemoteException {}

    @Override
    public WebpagePage search(String[] words, int page){
        if(words.length == 1 && (words[0].startsWith("http://") || words[0].startsWith("https://"))){
            if(urls.containsKey(words[0])){
                return new WebpagePage(new Webpage[]{new Webpage(words[0], urls.get(words[0]).get(0), urls.get(words[0]).get(1))}, 1, 1);
            } else {
                return null;
            }
        }

        System.out.println("Searching for: " + Arrays.toString(words));
        ArrayList<Webpage> webpages = new ArrayList<>();
        boolean first = true;
        for (String word: words){
            if(values.containsKey(word)){
                if(first){
                    webpages.addAll(values.get(word).stream().map(url -> new Webpage(url, urls.get(url).get(0), urls.get(url).get(1), urlsLinkTo.get(url).size())).toList());
                    first = false;
                }else {
                    webpages.retainAll(values.get(word).stream().map(url -> new Webpage(url, urls.get(url).get(0), urls.get(url).get(1), urlsLinkTo.get(url).size())).toList());
                }
            }
        }
        webpages.sort(Comparator.comparing(Webpage::getLinks).reversed());
        // Calculate start and end index for the sublist
        int startIndex = Math.min((page-1)*10, webpages.size());
        int endIndex = Math.min(startIndex + 10, webpages.size());
        List<Webpage> paginatedWebpages = webpages.subList(startIndex, endIndex);
        System.out.println(paginatedWebpages);
        return new WebpagePage(paginatedWebpages.toArray(new Webpage[0]), page, (webpages.size()/10)+1);
    }

    @Override
    public Webpage[] linkedPages(String url){
        if(urlsLinkTo.containsKey(url)){
            return urlsLinkTo.get(url).stream().map(link -> new Webpage(link, urls.get(link).get(0), urls.get(link).get(1))).toArray(Webpage[]::new);
        }else {
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            Barrel barrel = new Barrel();
            while (true){
                JSONObject message = barrel.receiveMessage();
                if(message != null){
                    if(message.getString("type").equals("index")){
                        barrel.addUrl(message.getString("url"), message.getString("text"), message.getString("title"));
                        barrel.indexURL(message.getString("url"), message.getString("text"));
                    }else if (message.getString("type").equals("link")){
                        barrel.addLink(message.getString("from"), message.getString("to"));
                    }
                }
            }
        } catch (RemoteException e){
            System.err.println("Failed connecting to gateway: "+ e.getMessage());
            System.exit(-1);
        } catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            System.exit(-1);
        }
    }
}
