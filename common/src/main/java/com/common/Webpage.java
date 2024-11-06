package com.common;

import java.io.Serializable;
import java.util.ArrayList;

public class Webpage implements Serializable {
    String url;
    String title;
    String text;
    int numberOfLinksTo;

    ArrayList<Webpage> referencedIn;

    public Webpage(String url, String title, String text, int numberOfLinksTo){
        this.url = url;
        this.title = title;
        this.text = text;
        this.numberOfLinksTo = numberOfLinksTo;
        this.referencedIn = new ArrayList<>();
    }
    public Webpage(String url, String title, String text){
        this.url = url;
        this.title = title;
        this.text = text;
        this.numberOfLinksTo = 0;
        this.referencedIn = new ArrayList<>();
    }

    
    /** 
     * @return String
     */
    public String getUrl() {
        return url;
    }

    
    /** 
     * @return String
     */
    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getNumberOfLinksTo() {
        return numberOfLinksTo;
    }

    public void setReferencedIn(ArrayList<Webpage> referencedIn) {
        this.referencedIn = referencedIn;
    }

    public ArrayList<Webpage> getReferencedIn(){
        return referencedIn;
    }

    public int getLinks(){
        return numberOfLinksTo;
    }

    @Override
    public String toString(){
        return "[url: " + url +" title: " + title+"]";
    }
}
