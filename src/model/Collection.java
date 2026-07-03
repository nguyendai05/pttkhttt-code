package model;

import model.enums.CollectionVisibility;

import java.util.ArrayList;
import java.util.List;


public class Collection {
    private String id;
    private String ownerId;
    private String name;
    private CollectionVisibility visibility;
    private List<String> documentIds = new ArrayList<>();

    public Collection(String id, String ownerId, String name, CollectionVisibility visibility) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.visibility = visibility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CollectionVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(CollectionVisibility visibility) {
        this.visibility = visibility;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<String> documentIds) {
        this.documentIds = documentIds;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + visibility + " | documents=" + documentIds;
    }
}
