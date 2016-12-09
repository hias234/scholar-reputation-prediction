package at.jku.paperprediction.io;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;
import at.jku.paperprediction.features.AvgCitationCountLast5Years;
import at.jku.paperprediction.features.FeatureCalculator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marku on 09.12.2016.
 */
public class ArffFileWriter {

    public void computeAndWriteArffFile(String path, Model model, int yearToPredict) {
        // compute features
        List<FeatureCalculator> featureCalculators = Arrays.asList(new AvgCitationCountLast5Years(yearToPredict));
        for (FeatureCalculator featureCalculator : featureCalculators) {
            model = featureCalculator.addFeature(model);
        }

        List<String> featureKeys = featureCalculators.stream().map(FeatureCalculator::getFeatureKey).collect(Collectors.toList());

        // write features
        PrintWriter writer = null;
        try {
            File file = new File(path);
            file.createNewFile();

            writer = new PrintWriter(file);
            writer.println("% Group A");
            writer.println("% Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            writer.println();

            writer.println("@RELATION sysdev");
            writer.println();

            for (String featureKey : featureKeys) {
                writer.println("@ATTRIBUTE " + featureKey + " NUMERIC");
            }
            writer.println("@ATTRIBUTE hIndex NUMERIC");
            writer.println();

            writer.println("@DATA");

            for (Author author : model.authors.values()) {
                for (String featureKey : featureKeys) {
                    writer.write(author.features.get(featureKey).toString());
                    writer.write(",");
                }
                Integer hIndex = author.hIndexForYear.get(yearToPredict);
                if (hIndex == null) {
                    hIndex = -1;
                }
                writer.write(hIndex.toString());
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
