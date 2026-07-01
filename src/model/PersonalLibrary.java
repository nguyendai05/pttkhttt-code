package model;

import java.util.ArrayList;
import java.util.List;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class PersonalLibrary {
    public String userId;
    public List<String> savedDocumentIds = new ArrayList<>();
    public List<Collection> collections = new ArrayList<>();

    public PersonalLibrary(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PersonalLibrary{userId='" + userId + '\''
                + ", savedDocumentIds=" + savedDocumentIds
                + ", collections=" + collections + '}';
    }
}