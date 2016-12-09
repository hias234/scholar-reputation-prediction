package at.jku.paperprediction.io;

import at.jku.paperprediction.entites.Author;

import javax.xml.ws.Holder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by marku on 09.12.2016.
 */
public class AuthorsHIndexWriter {

    public void writeAuthorsHIndices(String outputFile, int startYear, int lastYear, Collection<Author> authors) throws IOException {
        System.out.println();
        System.out.print("Writing file...");
        List<Integer> yearList = IntStream.rangeClosed(startYear, lastYear).mapToObj(val -> val)
                .collect(Collectors.toList());
        FileWriter fileWriter = new FileWriter(outputFile);
        Holder<Integer> outputLineCounter = new Holder<>(0);
        fileWriter.write(
                "author," + yearList.stream().map(Object::toString).collect(Collectors.joining(","))
                        + "\n");
        authors.forEach(author -> {
            try {
                fileWriter.write(author.name.replace(",", "\\,") + "," + yearList.stream()
                        .map(year -> author.hIndexForYear.get(year).toString())
                        .collect(Collectors.joining(",")) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (outputLineCounter.value % 1000 == 0) {
                try {
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outputLineCounter.value = outputLineCounter.value + 1;
        });
        fileWriter.flush();
        fileWriter.close();
    }

}
