package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;
import at.jku.paperprediction.entites.Publication;

import java.util.List;

/**
 * Created by marku on 09.12.2016.
 */
public class PublicationCount extends AbstractFeatureCalculator {

    private int year;

    public PublicationCount(int year) {
        super("PublicationCount" + year);
        this.year = year;
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        List<Publication> publicationsOfYear = author.publications.get(year);
        if (publicationsOfYear == null) {
            return 0.0;
        }
        return (double) publicationsOfYear.size();
    }
}