package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

import java.util.function.IntSupplier;

/**
 * Created by marku on 20.12.2016.
 */
public class LastPublicationXYearsAgo extends AbstractFeatureCalculator {

    public static final String FEATURE_KEY = "LastPublicationXYearsAgo";

    public LastPublicationXYearsAgo(int yearToPredict, int yearsToPredictBack) {
        super(FEATURE_KEY, yearToPredict, yearsToPredictBack);
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        if (author.publications.isEmpty()) {
            return Double.MAX_VALUE;
        }

        int yearLastPublication = author.publications.keySet().stream().mapToInt(p -> p)
                .filter(p -> p <= yearToPredict - yearsToPredictBack)
                .max().orElseGet(() -> Integer.MIN_VALUE);

        return (double) (yearToPredict - yearsToPredictBack - yearLastPublication);
    }
}
