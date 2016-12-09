package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

import java.util.Map;

/**
 * Created by marku on 05.12.2016.
 */
public interface FeatureCalculator {

    /**
     * add a specific feature to the model
     */
    Model addFeature(Model model);

    String getFeatureKey();
}
