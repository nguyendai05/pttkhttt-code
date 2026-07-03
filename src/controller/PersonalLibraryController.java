package controller;

import model.Collection;
import model.DocumentItem;
import model.PersonalLibrary;
import model.UserProfile;
import model.enums.CollectionVisibility;
import service.PersonalLibraryService;
import util.OperationResult;

import java.util.List;


public class PersonalLibraryController {
    private PersonalLibraryService personalLibraryService;

    public PersonalLibraryController(PersonalLibraryService personalLibraryService) {
        this.personalLibraryService = personalLibraryService;
    }



    public OperationResult<UserProfile> viewProfile() {
        return personalLibraryService.viewProfile();
    }



    public OperationResult<UserProfile> updateProfile(String fullName, String bio) {
        return personalLibraryService.updateProfile(fullName, bio);
    }



    public OperationResult<List<DocumentItem>> myUploadedDocuments() {
        return personalLibraryService.myUploadedDocuments();
    }



    public OperationResult<PersonalLibrary> saveApprovedDocument(String documentId) {
        return personalLibraryService.saveApprovedDocument(documentId);
    }



    public OperationResult<List<DocumentItem>> viewSavedDocuments() {
        return personalLibraryService.viewSavedDocuments();
    }



    public OperationResult<Collection> createCollection(String name, CollectionVisibility visibility) {
        return personalLibraryService.createCollection(name, visibility);
    }
}
