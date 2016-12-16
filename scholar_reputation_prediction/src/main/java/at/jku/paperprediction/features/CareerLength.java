package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

/**
 * Created by marku on 12.12.2016.
 */
public class CareerLength extends AbstractFeatureCalculator {

    public static final String FEATURE_KEY = "CareerLength";

    public CareerLength(int yearToPredict, int yearsToPredictBack) {
        super(FEATURE_KEY, yearToPredict, yearsToPredictBack);
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        if (!author.publications.isEmpty()) {
            Integer firstPublishedYear = author.publications.keySet().stream()
                    .filter(year -> !author.publications.get(year).isEmpty())
                    .sorted().findFirst().orElse(-1);

            return (double)(yearToPredict - yearsToPredictBack - firstPublishedYear + 1);
        }

        return -1.0;
    }
}
