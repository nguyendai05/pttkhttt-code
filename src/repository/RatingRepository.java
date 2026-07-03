package repository;

import model.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Repository in-memory dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
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
