package com.common;

import java.io.Serializable;

public class BarrelMetrics implements Serializable {
    double total;
    int searches;
    int barrelId;
    double average;

    
    /** 
     * @return double
     */
    public double getTotal() {
        return total;
    }

    
    /** 
     * @return int
     */
    public int getSearches() {
        return searches;
    }

    public int getBarrelId() {
        return barrelId;
    }

    public double getAverage() {
        return average;
    }

    public BarrelMetrics(int barrelId){
        this.barrelId = barrelId;
        this.total = 0;
        this.searches = 0;
        this.average = 0;
    }

    public void addSearch(long time){
        total += time*10e-7;
        searches++;
        average = total/searches;
    }




    @Override
    public String toString(){
        if(searches == 0)
            return barrelId + ": No metrics available";
        return barrelId + ": Average time: " + total/searches + "ds, Searches: " + searches;
    }
}
