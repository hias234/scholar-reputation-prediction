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

    public void computeAndWriteArffFile(String path, String path_classification, Model model, int yearToPredict, int yearsPredictBack) {

        // compute features
        List<FeatureCalculator> featureCalculators = new ArrayList<>(Arrays.asList(
                new AvgCitationCountLast5Years(yearToPredict, yearsPredictBack),
                new CareerLength(yearToPredict, yearsPredictBack),
                new PublicationCountUntilYear(yearToPredict, yearsPredictBack),
                new CitationCountUntilYear(yearToPredict, yearsPredictBack),
                new AvgPublicationsPerYear(yearToPredict, yearsPredictBack),
                new AvgCitationsPerYear(yearToPredict, yearsPredictBack),
                new AvgCitationsPerPaper(yearToPredict, yearsPredictBack),
                new LastPublicationXYearsAgo(yearToPredict, yearsPredictBack)
        ));

        for (int year = yearToPredict - yearsPredictBack - 4; year <= yearToPredict - yearsPredictBack; year++) {
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
        PrintWriter writerClassification = null;
        try {
            File file = new File(path);
            file.createNewFile();
            writer = new PrintWriter(file);

            File fileClassification = new File(path_classification);
            fileClassification.createNewFile();
            writerClassification = new PrintWriter(fileClassification);

            writeAttributes(featureKeys, writer);
            writeAttributes(featureKeys, writerClassification);

            writer.println("@ATTRIBUTE hIndexDiff NUMERIC");
            writer.println();

            writerClassification.print("@ATTRIBUTE hIndexDiff {");
            for (int i = 0; i < 50; i++) {
                if (i > 0) {
                    writerClassification.print(",");
                }
                writerClassification.print(i);
            }
            writerClassification.println("}");
            writerClassification.println();

            writer.println("@DATA");
            writerClassification.println("@DATA");

            // write for every authors which already have published before the last year where the h-index is known
            for (Author author : getRelevantAuthors(model)) {

                writeDataLine(yearToPredict, yearsPredictBack, featureKeys, writer, author);
                writeDataLine(yearToPredict, yearsPredictBack, featureKeys, writerClassification, author);

                if (count % 1000 == 0) {
                    writer.flush();
                    writerClassification.flush();
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
            if (writerClassification != null) {
                writerClassification.close();
            }
        }
    }

    /**
     * only write authors to features file, which have a positive career length and did publish in the last 10 years
     * @param model
     * @return
     */
    private List<Author> getRelevantAuthors(Model model) {
        return model.authors.values().stream()
                .filter(a -> a.features.get(CareerLength.FEATURE_KEY) > 1 && a.features.get(CareerLength.FEATURE_KEY) < 100)
                .filter(a -> a.features.get(LastPublicationXYearsAgo.FEATURE_KEY) < 10)
                .collect(Collectors.toList());
    }

    private void writeDataLine(int yearToPredict, int yearsPredictBack, List<String> featureKeys, PrintWriter writer, Author author) {
        for (String featureKey : featureKeys) {
            writer.write(author.features.get(featureKey).toString());
            writer.write(",");
        }
        Integer hIndex = getHIndex(yearToPredict, author);
        Integer hIndex5YearsAgo = getHIndex(yearToPredict - yearsPredictBack, author);
        writer.write(Integer.toString(hIndex - hIndex5YearsAgo));
        writer.println();
    }

    private void writeAttributes(List<String> featureKeys, PrintWriter writer) {
        writer.println("% Group A");
        writer.println("% Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        writer.println();

        writer.println("@RELATION sysdev");
        writer.println();

        for (String featureKey : featureKeys) {
            writer.println("@ATTRIBUTE " + featureKey + " NUMERIC");
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
