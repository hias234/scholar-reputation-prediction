package at.jku.paperprediction.features;

/**
 * Created by marku on 09.12.2016.
 */
public abstract class AbstractFeatureCalculator implements FeatureCalculator {

    protected int yearToPredict;

    public AbstractFeatureCalculator(int yearToPredict) {
        this.yearToPredict = yearToPredict;
    }
}
