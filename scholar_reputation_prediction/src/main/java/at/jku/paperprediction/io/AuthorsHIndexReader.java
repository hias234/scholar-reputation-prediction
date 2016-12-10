package at.jku.paperprediction.io;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by marku on 09.12.2016.
 */
public class AuthorsHIndexReader {

    public Model readAuthorsHIndices(String path, Model model) throws FileNotFoundException {
        Scanner in = new Scanner(new FileInputStream(path));

        String firstLine = in.nextLine();
        Stream<String> headerStream = Arrays.stream(firstLine.split(",")).map(String::trim).skip(1);
        List<Integer> years = headerStream.map(Integer::parseInt).collect(Collectors.toList());

        while (in.hasNextLine()) {
            String line = in.nextLine();

            String[] parts = line.split(",");
            String authorName = Arrays.stream(parts).findFirst().get();
            List<Integer> hIndices = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());

            Author author = model.authors.get(authorName);
            if (author != null) {
                for (int i = 0; i < years.size(); i++) {
                    author.hIndexForYear.put(years.get(i), hIndices.get(i));
                }
                model.authors.put(authorName, author);
            }
            else {
                System.out.println("author not found");
            }
        }

        return model;
    }

}
