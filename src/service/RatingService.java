package service;

import model.DocumentItem;
import model.Rating;
import model.UserAccount;
import model.enums.DocumentStatus;
import model.enums.Role;
import repository.DocumentRepository;
import repository.RatingRepository;
import util.IdGenerator;
import util.OperationResult;


public class RatingService {
    private RatingRepository ratingRepository;
    private DocumentRepository documentRepository;
    private SessionManager sessionManager;

    public RatingService(RatingRepository ratingRepository, DocumentRepository documentRepository, SessionManager sessionManager) {
        this.ratingRepository = ratingRepository;
        this.documentRepository = documentRepository;
        this.sessionManager = sessionManager;
    }



    public OperationResult<Double> rateDocument(String documentId, int score) {
        UserAccount user = sessionManager.getCurrentUser().orElse(null);
        if (user == null || user.getRole() != Role.USER) {
            return OperationResult.fail("Chỉ User đã đăng nhập được đánh giá tài liệu.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.getStatus() != DocumentStatus.APPROVED) {
            return OperationResult.fail("Chỉ được đánh giá tài liệu APPROVED.");
        }
        if (score < 1 || score > 5) {
            return OperationResult.fail("Điểm rating phải từ 1 đến 5.");
        }
        Rating rating = ratingRepository.findByDocumentAndUser(documentId, user.getId())
                .orElse(new Rating(IdGenerator.nextId("RAT"), documentId, user.getId(), score));
        rating.setScore(score);
        ratingRepository.save(rating);
        double average = calculateAverage(documentId);
        return OperationResult.ok("Đánh giá thành công. Điểm trung bình hiện tại: " + average, average);
    }



    public double calculateAverage(String documentId) {
        return ratingRepository.findByDocument(documentId).stream()
                .mapToInt(rating -> rating.getScore())
                .average()
                .orElse(0.0);
    }
}
