package at.jku.paperprediction.io;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;
import at.jku.paperprediction.features.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marku on 09.12.2016.
 */
public class ArffFileWriter {

    public void computeAndWriteArffFile(String path, Model model, int yearToPredict, int yearsPredictBack) {

        // compute features
        List<FeatureCalculator> featureCalculators = new ArrayList<>(Arrays.asList(
                new AvgCitationCountLast5Years(yearToPredict, yearsPredictBack),
                new CareerLength(yearToPredict, yearsPredictBack),
                new PublicationCountUntilYear(yearToPredict, yearsPredictBack),
                new CitationCountUntilYear(yearToPredict, yearsPredictBack),
                new AvgPublicationsPerYear(yearToPredict, yearsPredictBack),
                new AvgCitationsPerYear(yearToPredict, yearsPredictBack),
                new AvgCitationsPerPaper(yearToPredict, yearsPredictBack)
        ));

        for (int year = yearToPredict - yearsPredictBack - 5; year <= yearToPredict - yearsPredictBack; year++) {
            featureCalculators.add(new CitationCount(year));
            featureCalculators.add(new HIndex(year));
            featureCalculators.add(new PublicationCount(year));
        }

        for (FeatureCalculator featureCalculator : featureCalculators) {
            System.out.println("calculating feature " + featureCalculator.getFeatureKey());
            model = featureCalculator.addFeature(model);
        }
        System.out.println("Calculating features ... DONE");

        List<String> featureKeys = featureCalculators.stream().map(FeatureCalculator::getFeatureKey).collect(Collectors.toList());

        int count = 0;

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
            writer.println("@ATTRIBUTE hIndexDiff NUMERIC");
            writer.println();

            writer.println("@DATA");

            for (Author author : model.authors.values()) {
                for (String featureKey : featureKeys) {
                    writer.write(author.features.get(featureKey).toString());
                    writer.write(",");
                }
                Integer hIndex = getHIndex(yearToPredict, author);
                Integer hIndex5YearsAgo = getHIndex(yearToPredict - yearsPredictBack, author);
                writer.write(Integer.toString(hIndex - hIndex5YearsAgo));
                writer.println();

                if (count % 1000 == 0) {
                    writer.flush();
                }
                count++;
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

    private Integer getHIndex(int year, Author author) {
        Integer hIndex = author.hIndexForYear.get(year);
        if (hIndex == null) {
            hIndex = -1;
        }
        return hIndex;
    }

}
