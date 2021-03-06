package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

/**
 * Created by marku on 09.12.2016.
 */
public abstract class AbstractFeatureCalculator implements FeatureCalculator {

    protected Integer yearToPredict;
    protected Integer yearsToPredictBack;
    protected final String featureKey;

    public AbstractFeatureCalculator(String featureKey) {
        this.featureKey = featureKey;
    }

    public AbstractFeatureCalculator(String featureKey, int yearToPredict, int yearsToPredictBack) {
        this.featureKey = featureKey;
        this.yearToPredict = yearToPredict;
        this.yearsToPredictBack = yearsToPredictBack;
    }

    @Override
    public String getFeatureKey() {
        return featureKey;
    }

    @Override
    public Model addFeature(Model model) {
        for (Author author : model.authors.values()) {
            author.features.put(featureKey, getFeatureOfAuthor(author, model));
        }
        return model;
    }

    protected abstract Double getFeatureOfAuthor(Author author, Model model);
}
