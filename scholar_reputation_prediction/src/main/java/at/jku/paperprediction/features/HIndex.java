package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

/**
 * Created by marku on 09.12.2016.
 */
public class HIndex extends AbstractFeatureCalculator {

    private int year;

    public HIndex(int year) {
        super("HIndex" + year);
        this.year = year;
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        Integer hIndex = author.hIndexForYear.get(year);
        if (hIndex == null) {
            hIndex = -1;
        }
        return hIndex.doubleValue();
    }
}
