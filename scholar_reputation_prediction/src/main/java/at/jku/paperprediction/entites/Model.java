package at.jku.paperprediction.entites;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marku on 05.12.2016.
 */
public class Model {

    public Map<String, Publication> existingPublications = new HashMap<>();
    public Map<String, Publication> unknownPublications = new HashMap<>();
    public Map<Integer, Map<String, List<Publication>>> publicationsOfYear = new HashMap<>();

    public Map<String, Author> authors = new HashMap<>();

    public long objectCounter = 0;
    public long duplicationCounter = 0;

}
