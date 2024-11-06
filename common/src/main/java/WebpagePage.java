import java.io.Serializable;

public class WebpagePage implements Serializable {
    Webpage[] sites;
    int page;
    int totalPages;

    public WebpagePage(Webpage[] sites, int page, int totalPages){
        this.sites = sites;
        this.page = page;
        this.totalPages = totalPages;
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        if (sites.length == 0) {
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
