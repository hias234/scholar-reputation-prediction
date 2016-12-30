package at.jku.paperprediction;

import at.jku.paperprediction.entites.Model;
import at.jku.paperprediction.entites.Publication;
import at.jku.paperprediction.features.AvgCitationCountLast5Years;
import at.jku.paperprediction.features.FeatureCalculator;
import at.jku.paperprediction.io.*;

import javax.xml.ws.Holder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main {

    private static final String PATH = "F:\\";
    private static final String INPUT_FILE = PATH + "citation-acm-v8.txt";
    private static final String OUTPUT_FILE = PATH + "output.csv";

    public static void main(final String[] args) throws Exception {

        Model model = new PublicationReader().readPublications(INPUT_FILE);

        System.out.println("DONE READING FILE - calling GC");
        System.gc();
        System.out.println("DONE GC - STARTING WITH THE PROCESSING");

//        List<Integer> sortedYears =
//                model.publicationsOfYear.keySet().stream().filter(year -> year > 0).sorted(Integer::compareTo)
//                .collect(Collectors.toList());

//        int startYear = sortedYears.get(0);
//        int lastYear = sortedYears.get(sortedYears.size() - 1);

        //model = calculateAuthorHIndices(model, startYear, lastYear);
        model = new JounalRankReader().readJounalRanks(PATH + "publication_jouranlrank.csv", model);
        model = new AuthorsHIndexReader().readAuthorsHIndices(PATH + "authors_hindex_acm_v8.csv", model);

        int yearToPredict = 2014;
        new ArffFileWriter().computeAndWriteArffFile(PATH + "features.arff", PATH + "features_classification.arff", model, yearToPredict, 5);

        //new AuthorsHIndexWriter().writeAuthorsHIndices(OUTPUT_FILE, startYear, lastYear, model.authors.values());

        System.out.println("DONE");
    }

    private static Model calculateAuthorHIndices(final Model model, int startYear, int lastYear) {
        try (IntStream currentYearStream = IntStream.rangeClosed(startYear, lastYear)) {
            currentYearStream.forEach(currentYear -> {
                System.out.print("\rCalculating for Year " + currentYear);
                model.authors.forEach((name, author) -> {
                    List<Integer> citationCount = new LinkedList<>();
                    try (IntStream tracedYearStream = IntStream.rangeClosed(startYear, currentYear)) {
                        tracedYearStream.forEach(tracedYear -> model.publicationsOfYear
                            .getOrDefault(tracedYear, Collections.emptyMap())
                            .getOrDefault(name, Collections.emptyList())
                            .forEach(publicationToProcess -> {
                                try (IntStream yearStream = IntStream
                                    .rangeClosed(startYear, currentYear)) {
                                    citationCount.add(yearStream.map(
                                        year -> publicationToProcess.numberOfCitationPerYear
                                            .getOrDefault(year, 0)).sum());
                                }
                            }));
                    }
                    citationCount.sort((a, b) -> -1 * a.compareTo(b));
                    int hIndex = -1;
                    if (citationCount.size() != 0) {
                        hIndex = 0;
                        for (int i = 0; i < citationCount.size(); i++) {
                            if (i >= citationCount.get(i)) {
                                break;
                            } else {
                                hIndex = i + 1;
                            }
                        }
                    }

                    author.hIndexForYear.put(currentYear, hIndex);
                });
            });
        }

        return model;
    }
}
