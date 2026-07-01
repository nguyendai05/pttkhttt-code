package model;

import model.enums.CollectionVisibility;

import java.util.ArrayList;
import java.util.List;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Model entity dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class Collection {
    public String id;
    public String ownerId;
    public String name;
    public CollectionVisibility visibility;
    public List<String> documentIds = new ArrayList<>();

    public Collection(String id, String ownerId, String name, CollectionVisibility visibility) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + visibility + " | documents=" + documentIds;
    }
}
