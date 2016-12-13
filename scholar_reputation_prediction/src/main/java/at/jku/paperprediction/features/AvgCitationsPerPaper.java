package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

/**
 * Created by marku on 12.12.2016.
 */
public class AvgCitationsPerPaper extends AbstractFeatureCalculator {

    private PublicationCountUntilYear publicationCountCalculator;
    private CitationCountUntilYear citationCountCalculator;

    public AvgCitationsPerPaper(int yearToPredict, int yearsToPredictBack) {
        super("AvgCitationsPerPaper", yearToPredict, yearsToPredictBack);

        publicationCountCalculator = new PublicationCountUntilYear(yearToPredict, yearsToPredictBack);
        citationCountCalculator = new CitationCountUntilYear(yearToPredict, yearsToPredictBack);
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        double citationCount = citationCountCalculator.getFeatureOfAuthor(author, model);
        double publicationCount = publicationCountCalculator.getFeatureOfAuthor(author, model);

        if (publicationCount == 0.0) {
            return -1.0;
        }

        return citationCount / publicationCount;
    }
}
