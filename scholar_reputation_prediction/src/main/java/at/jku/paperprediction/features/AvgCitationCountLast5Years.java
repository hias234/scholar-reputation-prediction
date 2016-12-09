package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

/**
 * Created by marku on 08.12.2016.
 */
public class AvgCitationCountLast5Years extends AbstractFeatureCalculator {

    public AvgCitationCountLast5Years(int yearToPredict) {
        super("AvgCitationCountLast5Years", yearToPredict);
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        int year = yearToPredict - 10;
        double avgCitationCount = 0.0;

        for (; year <= yearToPredict - 5; year++) {
            Integer citationCount = author.citationsPerYear.get(year);
            if (citationCount == null) {
                citationCount = 0;
            }
            avgCitationCount += citationCount;
        }

        return avgCitationCount / 6;
    }
}
