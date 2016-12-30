package at.jku.paperprediction.io;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;
import at.jku.paperprediction.entites.Publication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by marku on 29.12.2016.
 */
public class JounalRankReader {

    public Model readJounalRanks(String path, Model model) throws IOException {
        Scanner in = new Scanner(new FileInputStream(path));

        // skip first line
        String firstLine = in.nextLine();

        int count = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine();

            String[] parts = line.split(",");
            String publicationId = parts[0];
            Double sjr = Double.parseDouble(parts[1]);
            Double snip = Double.parseDouble(parts[2]);

            Double avgRank = -1.0;
            if (sjr != -1) {
                avgRank = snip != -1 ? (sjr + snip) / 2.0 : sjr;
            }
            if (snip != -1) {
                avgRank = snip;
            }

            Publication publication = model.existingPublications.get(publicationId);
            if (publication != null) {
                publication.journalRank = avgRank;
            }
            else {
                System.out.println("publication not found");
            }

            if (count % 10000 == 0) {
                System.out.println(count + " jounrnals read\r");
            }
            count++;
        }

        return model;
    }

}
