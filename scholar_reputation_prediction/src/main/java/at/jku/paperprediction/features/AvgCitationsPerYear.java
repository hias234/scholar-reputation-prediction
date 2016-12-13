package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

/**
 * Created by marku on 12.12.2016.
 */
public class AvgCitationsPerYear extends AbstractFeatureCalculator {

    private CareerLength careerLengthCalculator;
    private CitationCountUntilYear citationCountCalculator;

    public AvgCitationsPerYear(int yearToPredict, int yearsToPredictBack) {
        super("AvgCitationsPerYear", yearToPredict, yearsToPredictBack);

        careerLengthCalculator = new CareerLength(yearToPredict, yearsToPredictBack);
        citationCountCalculator = new CitationCountUntilYear(yearToPredict, yearsToPredictBack);
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        double careerLength = careerLengthCalculator.getFeatureOfAuthor(author, model);
        double citationCount = citationCountCalculator.getFeatureOfAuthor(author, model);

        if (careerLength == 0.0) {
            return -1.;
        }

        return citationCount / careerLength;
    }
}
