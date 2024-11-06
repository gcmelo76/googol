package com.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WebpagePage implements Serializable {
    ArrayList<Webpage> sites;
    int page;
    int totalPages;

    
    /** 
     * @return ArrayList<Webpage>
     */
    public ArrayList<Webpage> getSites() {
        return sites;
    }

    
    /** 
     * @return int
     */
    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public WebpagePage(Webpage[] sites, int page, int totalPages){
        this.sites = new ArrayList<>();
        this.sites.addAll(List.of(sites));
        this.page = page;
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        if (sites.size() == 0) {
            return "No results found";
        }
        StringBuilder sb = new StringBuilder();
        if (totalPages == 1) {
            for (Webpage site : sites) {
                sb.append(site).append("\n");
            }
            return sb.toString();
        }
        sb.append("Page ").append(page).append(" of ").append(totalPages).append("\n");
        for (Webpage site : sites) {
            sb.append(site).append("\n");
        }
        return sb.toString();
    }
}
