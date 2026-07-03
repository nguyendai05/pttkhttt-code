package repository;

import model.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class RatingRepository {
    private List<Rating> ratings = new ArrayList<>();

    public void save(Rating rating) {
        findByDocumentAndUser(rating.getDocumentId(), rating.getUserId()).ifPresent(ratings::remove);
        ratings.add(rating);
    }

    public Optional<Rating> findByDocumentAndUser(String documentId, String userId) {
        return ratings.stream()
                .filter(rating -> rating.getDocumentId().equals(documentId) && rating.getUserId().equals(userId))
                .findFirst();
    }

    public List<Rating> findByDocument(String documentId) {
        return ratings.stream().filter(rating -> rating.getDocumentId().equals(documentId)).collect(Collectors.toList());
    }
}
