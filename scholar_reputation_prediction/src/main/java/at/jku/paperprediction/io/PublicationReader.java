package at.jku.paperprediction.io;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;
import at.jku.paperprediction.entites.Publication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by marku on 05.12.2016.
 */
public class PublicationReader {

    public Model readPublications(String filePath) throws FileNotFoundException {
        return readPublications(filePath, Integer.MAX_VALUE);
    }

    public Model readPublications(String filePath, int maxPublications) throws FileNotFoundException {
        Publication publication = null;
        Scanner in = new Scanner(new FileInputStream(filePath));

        Model model = new Model();

        boolean panicmode = false;

        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.length() < 2) {
                continue;
            }
            String content = line.substring(2);
            if (content.length() > 255) {
                content = content.substring(0, 255);
            }
            String type = line.substring(1, 2);
            if (panicmode) {
                if (type.equals("*")) {
                    panicmode = false;
                    publication = null;
                } else {
                    continue;
                }
            }
            switch (type) {
                // TITLE
                case "*": {
                    if (publication != null) {
                        Map<String, List<Publication>> publicationsOfAuthors = model.publicationsOfYear
                                .computeIfAbsent(publication.year, year -> new HashMap<>());
                        final Publication currentPublication = publication;
                        publication.authorList.forEach(author -> {
//                            List<Publication> publicationsOfAuthor = publicationsOfAuthors
//                                    .computeIfAbsent(author.name, name -> new LinkedList<>());
//                            publicationsOfAuthor.add(currentPublication);
                            author.addPublication(currentPublication);
                        });
                        model.existingPublications.put(publication.id, publication);
                        model.objectCounter++;
                        if (model.objectCounter % 10000 == 0)
                            System.out.print("\rCreated Object #" + model.objectCounter);
                    }
                    publication = new Publication();
                    publication.authorList = new HashSet<>();
                    publication.references = new ArrayList<>();
                    publication.title = content;
                    break;
                }
                // AUTHORS
                case "@": {
                    try (Stream<String> authorsStream = Arrays.stream(content.split(","))
                            .map(authorName -> authorName.replaceAll("\\d", "")).map(String::trim)) {
                        publication.authorList = authorsStream.map(String::toLowerCase)
                                .map(name -> model.authors.computeIfAbsent(name, Author::new))
                                .collect(Collectors.toSet());
                    }
                    break;
                }
                // YEAR
                case "t": {
                    try {
                        publication.year = Integer.parseInt(content);
                    }
                    catch(NumberFormatException e) {
                        publication.year = 0;
                        panicmode = true;
                    }
                    break;
                }
                // PUBLICATION VENUE
                case "c": {
                    publication.venue = content;
                    break;
                }
                // REFERENCES
                case "%": {
                    Publication alreadyExisting = model.existingPublications.get(content);
                    if (alreadyExisting == null) {
                        alreadyExisting =
                                model.unknownPublications.computeIfAbsent(content, Publication::new);
                    }
                    int citationCount = alreadyExisting.numberOfCitationPerYear
                            .computeIfAbsent(publication.year, year -> 0);
                    alreadyExisting.numberOfCitationPerYear.put(publication.year, ++citationCount);
                    publication.references.add(alreadyExisting);
                    break;
                }
                // ABSTRACT
                case "!": {
                    break;
                }
                // PAPER-ID
                case "i": {
                    if (line.startsWith("#index")) {
                        //TODO: clone from list
                        String index = line.substring(6);
                        if (model.existingPublications.containsKey(index)) {//PANICMODE
                            panicmode = true;
                            model.duplicationCounter++;
                            System.err.println(
                                    "Publication with id " + index + " already exists, skipping...");
                            break;
                        }
                        Publication alreadyExisting = model.unknownPublications.get(index);
                        if (alreadyExisting != null) {
                            publication.numberOfCitationPerYear =
                                    alreadyExisting.numberOfCitationPerYear;
                            model.unknownPublications.remove(index);
                        }

                        publication.id = index;
                    }
                }
            }

            if (model.existingPublications.size() >= maxPublications) {
                break;
            }
        }

        Map<String, List<Publication>> publicationsOfAuthors =
                model.publicationsOfYear.computeIfAbsent(publication.year, year -> new HashMap<>());
        final Publication currentPublication = publication;
        publication.authorList.forEach(author -> {
            List<Publication> publicationsOfAuthor =
                    publicationsOfAuthors.computeIfAbsent(author.name, name -> new LinkedList<>());
            publicationsOfAuthor.add(currentPublication);
        });
        System.out.println();
        System.out.println(
                "found " + model.existingPublications.size() + " publications with " + model.duplicationCounter
                        + " duplicate entries.");
        model.unknownPublications = null;

        return model;
    }

}
