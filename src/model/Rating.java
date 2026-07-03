package model;


public class Rating {
    private String id;
    private String documentId;
    private String userId;
    private int score;

    public Rating(String id, String documentId, String userId, int score) {
        this.id = id;
        this.documentId = documentId;
        this.userId = userId;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
