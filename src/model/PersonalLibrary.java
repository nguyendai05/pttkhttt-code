package model;

import java.util.ArrayList;
import java.util.List;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class PersonalLibrary {
    private String userId;
    private List<String> savedDocumentIds = new ArrayList<>();
    private List<Collection> collections = new ArrayList<>();

    public PersonalLibrary(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getSavedDocumentIds() {
        return savedDocumentIds;
    }

    public void setSavedDocumentIds(List<String> savedDocumentIds) {
        this.savedDocumentIds = savedDocumentIds;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    @Override
    public String toString() {
        return "PersonalLibrary{userId='" + userId + '\''
                + ", savedDocumentIds=" + savedDocumentIds
                + ", collections=" + collections + '}';
    }
}
