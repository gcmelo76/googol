package com.webclient;

import com.common.GatewayClientInt;
import com.common.PropertiesReader;
import com.common.Webpage;
import com.common.WebpagePage;
import com.webclient.beans.Admin;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class ThymeController {

    @Resource(name = "appScopedAdmin")
    private Admin admin;

    
    /** 
     * @return Admin
     */
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    Admin appScopedAdmin(){
        try {
            Admin temp = new Admin();
            GatewayClientInt gateway = (GatewayClientInt) Naming.lookup("rmi://" + RMI_ADDRESS +"/gatewayClient");
            gateway.subscribe(temp);
            return temp;
        } catch (Exception e){
            return null;
        }
    }


    @Autowired(required = false)
    private String RMI_ADDRESS = PropertiesReader.read("RMI_ADDRESS");

    
    /** 
     * @param model
     * @return String
     */
    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/search?q=";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q") String search, @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        try {
            model.addAttribute("query", search);
            RestHelper restHelper = new RestHelper();
            GatewayClientInt gateway = (GatewayClientInt) Naming.lookup("rmi://" + RMI_ADDRESS +"/gatewayClient");
            restHelper.hackerNews(model, search, gateway);
            String[] query = search.split(" ");
            WebpagePage results = gateway.search(query, page);
            if (results == null || results.getSites().isEmpty()){
                model.addAttribute("error", true);
                if(query.length == 1 && search.startsWith("https://") || search.startsWith("http://")) {
                    gateway.indexUrl(search);
                    model.addAttribute("errorText", "Indexing new URL");
                } else {
                    model.addAttribute("errorText", "No results found");
                }
            }else{
                for (Webpage webpage : results.getSites()){
                    ArrayList<Webpage> webpages = new ArrayList<>();
                    Webpage[] temp = gateway.linkedPages(webpage.getUrl());
                    if(temp != null)
                        webpages.addAll(Arrays.asList(temp));
                    webpage.setReferencedIn(webpages);
                }
                model.addAttribute("webpagePage", results);
                if(results.getPage() < results.getTotalPages()){
                    model.addAttribute("canNext", true);
                    model.addAttribute("nextPage", page+1);
                }
                if(results.getPage() > 1){
                    model.addAttribute("canPrevious", true);
                    model.addAttribute("previousPage", page-1);
                }
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            //todo handle error maybe return
            System.err.println("Failed to connect");
        }
        return "homepage";
    }

    @GetMapping("/admin")
    public String search(Model model){
        admin.display(model);
        return "admin";
    }
}