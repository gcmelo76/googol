package com.webclient.beans;

import com.common.BarrelMetrics;
import com.common.ClientInt;
import com.webclient.Message;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Admin extends UnicastRemoteObject implements ClientInt {

    @Autowired
    private SimpMessagingTemplate template;
    ArrayList<String> topSearches = new ArrayList<>();
    ArrayList<BarrelMetrics> barrelMetrics = new ArrayList<>();

    public Admin() throws RemoteException {
        super();
    }

    
    /** 
     * @param activeBarrels
     * @param topSearches
     * @param barrelMetrics
     * @throws RemoteException
     */
    @Override
    public void adminBoard(ArrayList<Integer> activeBarrels, ArrayList<String> topSearches, ArrayList<BarrelMetrics> barrelMetrics) throws RemoteException {
        this.topSearches = topSearches;
        this.barrelMetrics = barrelMetrics;
        JSONObject json = new JSONObject();
        json.put("topSearches", topSearches);
        json.put("barrelMetrics", barrelMetrics);
        template.convertAndSend("/topic/messages", new Message(json.toString()));
    }

    
    /** 
     * @param model
     */
    public void display(Model model){
        model.addAttribute("topSearches", topSearches);
        model.addAttribute("barrelMetrics", barrelMetrics);
    }
}
