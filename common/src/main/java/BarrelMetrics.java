import java.io.Serializable;

public class BarrelMetrics implements Serializable {
    double total;
    int searches;
    int barrelId;

    public BarrelMetrics(int barrelId){
        this.barrelId = barrelId;
        this.total = 0;
        this.searches = 0;
    }

    
    /** 
     * @param time
     */
    public void addSearch(long time){
        total += time*10e-7;
        searches++;
    }


    
    /** 
     * @return String
     */
    @Override
    public String toString(){
        if(searches == 0)
            return barrelId + ": No metrics available";
        return barrelId + ": Average time: " + total/searches + "ds, Searches: " + searches;
    }
}
