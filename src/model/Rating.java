package model;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class Rating {
    public String id;
    public String documentId;
    public String userId;
    public int score;

    public Rating(String id, String documentId, String userId, int score) {
        this.id = id;
        this.documentId = documentId;
        this.userId = userId;
        this.score = score;
    }
}
