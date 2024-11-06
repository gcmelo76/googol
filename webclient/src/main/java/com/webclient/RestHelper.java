package com.webclient;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.ui.Model;

import com.common.GatewayClientInt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestHelper {
    
    /** 
     * @param model
     * @param search
     * @param gatewayClientInt
     */
    public void hackerNews(Model model, String search, GatewayClientInt gatewayClientInt) {
        int total = 0;
        try {
            URL url = new URL("https://hacker-news.firebaseio.com/v0/topstories.json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            if(con.getResponseCode() < 300) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            String[] ids = content.toString().replace("[", "").replace("]", "").split(",");
            in.close();
            con.disconnect();
            for (int i = 0; i < 10; i++) {
                url = new URL("https://hacker-news.firebaseio.com/v0/item/" + ids[i] + ".json");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                int count = 0;
                for (String s : search.split(" ")) {
                    if (content.toString().contains(s)) {
                        count++;
                        break;
                    }
                }
                if(count == search.split(" ").length){
                    JSONParser parser = new JSONParser(content.toString());
                    String temp = parser.parseObject().get("url").toString();
                    System.out.println("Indexing: " + temp + " from hacker news");
                    gatewayClientInt.indexUrl(temp);
                    total++;
                }
            }
            if (total == 0){
                model.addAttribute("hackerNews", "No data found");
            }else {
                model.addAttribute("hackerNews", "Urls indexed from hacker news: " + total);
            }
            } else {
                model.addAttribute("hackerNews", "No data found");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            model.addAttribute("error", "Error fetching data");
        }
    }
}
