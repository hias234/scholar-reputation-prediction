package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;
import at.jku.paperprediction.entites.Publication;

import java.util.List;

/**
 * Created by marku on 09.12.2016.
 */
public class CitationCount extends AbstractFeatureCalculator {

    private int year;

    public CitationCount(int year) {
        super("CitationCount" + year);
        this.year = year;
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        Integer citationCount = author.citationsPerYear.get(year);
        if (citationCount == null) {
            citationCount = 0;
        }
        return citationCount.doubleValue();
    }
}
