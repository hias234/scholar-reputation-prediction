package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

/**
 * Created by marku on 12.12.2016.
 */
public class AvgPublicationsPerYear extends AbstractFeatureCalculator {

    private CareerLength careerLengthCalculator;
    private PublicationCountUntilYear publicationCountCalculator;

    public AvgPublicationsPerYear(int yearToPredict, int yearsToPredictBack) {
        super("AvgPublicationsPerYear", yearToPredict, yearsToPredictBack);
        careerLengthCalculator = new CareerLength(yearToPredict, yearsToPredictBack);
        publicationCountCalculator = new PublicationCountUntilYear(yearToPredict, yearsToPredictBack);
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        double careerLength = careerLengthCalculator.getFeatureOfAuthor(author, model);
        double publicationCount = publicationCountCalculator.getFeatureOfAuthor(author, model);

        if (careerLength == 0.0) {
            return -1.;
        }

        return publicationCount / careerLength;
    }
}
