package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

/**
 * Created by marku on 08.12.2016.
 */
public class AvgCitationCountLast5Years extends AbstractFeatureCalculator {

    public AvgCitationCountLast5Years(int yearToPredict, int yearsToPredictBack) {
        super("AvgCitationCountLast5Years", yearToPredict, yearsToPredictBack);
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        int year = yearToPredict - yearsToPredictBack - 5;
        double avgCitationCount = 0.0;

        for (; year <= yearToPredict - yearsToPredictBack; year++) {
            Integer citationCount = author.citationsPerYear.get(year);
            if (citationCount == null) {
                citationCount = 0;
            }
            avgCitationCount += citationCount;
        }

        return avgCitationCount / 6;
    }
}
