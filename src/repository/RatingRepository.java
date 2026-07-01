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
    private final List<Rating> ratings = new ArrayList<>();

    public void save(Rating rating) {
        findByDocumentAndUser(rating.documentId, rating.userId).ifPresent(ratings::remove);
        ratings.add(rating);
    }

    public Optional<Rating> findByDocumentAndUser(String documentId, String userId) {
        return ratings.stream()
                .filter(rating -> rating.documentId.equals(documentId) && rating.userId.equals(userId))
                .findFirst();
    }

    public List<Rating> findByDocument(String documentId) {
        return ratings.stream().filter(rating -> rating.documentId.equals(documentId)).collect(Collectors.toList());
    }
}
