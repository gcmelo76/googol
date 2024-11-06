import com.common.BarrelMetrics;
import com.common.ClientInt;
import com.common.GatewayClientInt;
import com.common.PropertiesReader;
import com.common.Webpage;
import com.common.WebpagePage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements ClientInt {

    String RMI_ADDRESS;
    GatewayClientInt server;

    public Client() throws MalformedURLException, NotBoundException, RemoteException {
        this.RMI_ADDRESS = PropertiesReader.read("RMI_ADDRESS");
        this.server = (GatewayClientInt) Naming.lookup("rmi://" + this.RMI_ADDRESS +"/gatewayClient");
        this.server.subscribe(this);
    }

    
    /** 
     * @param activeBarrels
     * @param topSearches
     * @param barrelMetrics
     */
    @Override
    public void adminBoard(ArrayList<Integer> activeBarrels, ArrayList<String> topSearches, ArrayList<BarrelMetrics> barrelMetrics) {
        System.out.println("___________________");
        System.out.println("Admin board");
        System.out.println("Active Barrels: " + activeBarrels.toString());
        System.out.println("Top searches: ");
        if(topSearches.isEmpty()) {
            System.out.println("No searches yet");
        }
        for (String search : topSearches) {
            System.out.println(search);
        }
        System.out.println("Barrel metrics: ");
        if (barrelMetrics.isEmpty()) {
            System.out.println("No metrics yet");
        }
        for (BarrelMetrics metrics : barrelMetrics) {
            System.out.println(metrics);
        }
        System.out.println("___________________");
        System.out.print("->");
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.out.print("""
                        /link <url> -list urls that link to a certain link
                        /exit -exit the program""");
            Client client = new Client();
            Scanner scanner = new Scanner(System.in);
            while (true){
                String message = scanner.nextLine().strip();
                if (message.isBlank()) {
                    System.out.println("Invalid input");
                    System.out.print("->");
                    continue;
                }
                if (message.equals("/exit")) {
                    System.out.println("Exiting");
                    System.exit(0);
                }

                if(message.split(" ").length == 2 && message.startsWith("/link")) {
                    String[] words = message.split(" ");
                    Webpage[] results = client.server.linkedPages(words[1]);
                    if (results == null || results.length == 0) {
                        System.out.println("___________________");
                        System.out.println("No results found");
                        System.out.println("___________________");
                    } else {
                        System.out.println("___________________");
                        for (Webpage result : results) {
                            System.out.println(result);
                        }
                        System.out.println("___________________");
                    }
                } else if(message.split(" ").length == 1 && message.startsWith("https://") || message.startsWith("http://")) {
                    WebpagePage results = client.server.search(new String[]{message}, 1);
                    if (results == null) {
                        System.out.println("___________________");
                        System.out.println("No results found");
                        System.out.println("indexing url");
                        System.out.println("___________________");
                        client.server.indexUrl(message);
                    } else {
                        System.out.println("___________________");
                        System.out.println(results);
                        System.out.println("___________________");
                    }
                }else {
                    String[] words = message.split(" ");
                    int page = 1;
                    while (true) {
                        WebpagePage results = client.server.search(words, page);
                        if (results == null) {
                            System.out.println("___________________");
                            System.out.println("No results found");
                            System.out.println("___________________");
                            break;
                        } else {
                            System.out.println("___________________");
                            System.out.println(results);
                            if (results.getTotalPages() <= 1) {
                                break;
                            } else {
                                try {
                                    System.out.println("Insert page number:");
                                    page = scanner.nextInt();
                                    if (page < 1 || page > results.getTotalPages()) {
                                        System.out.println("Invalid page number or search term");
                                        System.out.print("->");
                                        break;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid input");
                                    break;
                                }
                            }
                            System.out.println("___________________");
                        }
                    }
                }
                System.out.print("->");
            }
        } catch (RemoteException e){
            System.err.println("Gateway is down" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
