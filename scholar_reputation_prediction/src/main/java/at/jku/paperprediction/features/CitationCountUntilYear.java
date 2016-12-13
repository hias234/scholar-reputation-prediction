package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

import java.util.Map;

/**
 * Created by marku on 12.12.2016.
 */
public class CitationCountUntilYear extends AbstractFeatureCalculator {

    public CitationCountUntilYear(int yearToPredict, int yearsToPredictBack) {
        super("CitationCountUntilYear", yearToPredict, yearsToPredictBack);
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        return author.citationsPerYear.entrySet().stream()
                .filter(e -> e.getKey() <= yearToPredict - yearsToPredictBack)
                .mapToDouble(Map.Entry::getValue)
                .sum();
    }
}
