package at.jku.paperprediction.features;

import at.jku.paperprediction.entites.Author;
import at.jku.paperprediction.entites.Model;
import at.jku.paperprediction.entites.Publication;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marku on 30.12.2016.
 */
public class AvgPublicationJournalRank extends AbstractFeatureCalculator {

    public static final String FEATURE_KEY = "AvgPubJournalRankLast";

    private int lastXYears;

    public AvgPublicationJournalRank(int yearToPredict, int yearsToPredictBack, int lastXYears) {
        super(FEATURE_KEY + lastXYears, yearToPredict, yearsToPredictBack);

        this.lastXYears = lastXYears;
    }

    @Override
    protected Double getFeatureOfAuthor(Author author, Model model) {
        List<Publication> authorPublications =
                author.publications.values().stream()
                        .flatMap(Collection::stream)
                        .filter(p -> p.year <= yearToPredict - yearsToPredictBack && p.year >= yearToPredict - yearsToPredictBack - lastXYears)
                        .collect(Collectors.toList());

        double avgRank = 0;
        int cnt = 0;

        for (Publication p : authorPublications) {
            if (p.journalRank != -1) {
                avgRank += p.journalRank;
                cnt++;
            }
        }

        if (cnt == 0) {
            return -1.0;
        }
        return avgRank / cnt;
    }
}
