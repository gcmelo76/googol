import com.common.GatewayDownloaderInt;
import com.common.PropertiesReader;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Objects;

public class Downloader {
    String RMI_ADDRESS;
    String MULTICAST_ADDRESS;
    int MULTICAST_PORT;
    GatewayDownloaderInt gatewayDownloader;
    MulticastSocket socket;
    InetAddress multicastGroup;

    public Downloader() throws IOException, NotBoundException {
        this.RMI_ADDRESS = PropertiesReader.read("RMI_ADDRESS");
        this.MULTICAST_ADDRESS = PropertiesReader.read("MULTICAST_ADDRESS");
        this.MULTICAST_PORT = Integer.parseInt(Objects.requireNonNull(PropertiesReader.read("MULTICAST_PORT")));
        this.gatewayDownloader = (GatewayDownloaderInt) Naming.lookup("rmi://" + this.RMI_ADDRESS + "/gatewayDownloader");
        this.socket = new MulticastSocket(this.MULTICAST_PORT);
        this.multicastGroup = InetAddress.getByName(this.MULTICAST_ADDRESS);
    }

    public void indexUrl() {
        try {
            String link = gatewayDownloader.pop();
            Document doc = Jsoup.connect(link).get();
            System.out.println("Indexing document" + link);
            String text = doc.text();

            JSONObject message;
            message = new JSONObject();
            message.put("type", "index");
            message.put("text", text);
            message.put("url", link);
            message.put("title", doc.title());
            sendMessage(message);

            Elements elements = doc.select("a[href]");
            for (Element element : elements){
                String url = element.attr("abs:href");
                if(url.startsWith("https://") || url.startsWith("http://")) {
                    message = new JSONObject();
                    message.put("type", "link");
                    message.put("to", url);
                    message.put("from", link);
                    sendMessage(message);
                    gatewayDownloader.push(url, false);
                }
            }
        } catch (IOException e) {
            System.out.println("Error downloading url" + e.getMessage());
        } catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }
    }

    
    /** 
     * @param object
     */
    void sendMessage(JSONObject object) {
        try {
            String msg = object.toString();
            byte[] buffer = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.multicastGroup, this.MULTICAST_PORT);
            this.socket.send(packet);
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        try {
            Downloader downloader = new Downloader();
            System.out.println("Downloader is ready");
            while (true){
                downloader.indexUrl();
            }
        } catch (Exception e ){
            System.err.println("Error" + e.getMessage());
        }
    }
}
