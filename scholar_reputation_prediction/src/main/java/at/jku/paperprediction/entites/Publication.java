package at.jku.paperprediction.entites;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Publication {

    public String id;
    public int year;
    public String title;
    public String venue;
    public boolean present;

    public java.util.Set<Author> authorList;

    public List<Publication> references;

    public Map<Integer, Integer> numberOfCitationPerYear = new HashMap<>();


    public Publication() {}
    public Publication(String id) {
        this.id = id;
    }


    @Override public String toString() {
        return "Publication{" + "id='" + id + '\'' + ", year=" + year + ", title='" + title + '\''
            + ", venue='" + venue + '\'' + ", present=" + present + ", authorList=" + authorList
            + ", references=" + references + '}';
    }
}
