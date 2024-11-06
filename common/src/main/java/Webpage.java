import java.io.Serializable;

public class Webpage implements Serializable {
    String url;
    String title;
    String text;
    int numberOfLinksTo;

    public Webpage(String url, String title, String text, int numberOfLinksTo){
        this.url = url;
        this.title = title;
        this.text = text;
        this.numberOfLinksTo = numberOfLinksTo;
    }
    public Webpage(String url, String title, String text){
        this.url = url;
        this.title = title;
        this.text = text;
        this.numberOfLinksTo = 0;
    }

    
    /** 
     * @return int
     */
    public int getLinks(){
        return numberOfLinksTo;
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString(){
        return "[url: " + url +" title: " + title+"]";
    }
}
