package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

/**
 * Created by marku on 08.12.2016.
 */
public class AvgCitationCountLast5Years extends AbstractFeatureCalculator {

    public AvgCitationCountLast5Years(int yearToPredict) {
        super(yearToPredict);
    }

    @Override
    public Model createFeatures(Model model) {
        // TODO
        return model;
    }
}
