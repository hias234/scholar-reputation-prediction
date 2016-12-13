package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;
import at.jku.paperprediction.entites.Publication;

import java.util.List;

/**
 * Created by marku on 12.12.2016.
 */
public class PublicationCountUntilYear extends AbstractFeatureCalculator {

    public PublicationCountUntilYear(int yearToPredict, int yearsToPredictBack) {
        super("PublicationCountUntilYear", yearToPredict, yearsToPredictBack);
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        return author.publications.entrySet().stream()
                .filter(e -> e.getKey() <= yearToPredict - yearsToPredictBack)
                .mapToDouble(e -> e.getValue().size())
                .sum();
    }
}
