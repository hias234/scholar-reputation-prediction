package at.jku.paperprediction.entites;

import java.util.*;

public class Author {

    public String name;
    public Map<Integer, Integer> hIndexForYear = new HashMap<>();
    public Map<Integer, List<Publication>> publications = new HashMap<>();
    public Map<String, Double> features = new HashMap<>();

    public Author() {}

    public Author(String name) {
        this.name = name;
    }

    public void addPublication(Publication publication) {
        if (publications.containsKey(publication.year)) {
            publications.get(publication.year).add(publication);
        }
        else {
            publications.put(publication.year, new ArrayList<>(Arrays.asList(publication)));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;

        Author author = (Author) o;

        return name.equals(author.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override public String toString() {
        return "Author{" + "name='" + name + '\'' + '}';
    }


}
