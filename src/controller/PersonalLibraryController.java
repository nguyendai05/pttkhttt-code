package controller;

import model.Collection;
import model.DocumentItem;
import model.PersonalLibrary;
import model.UserProfile;
import model.enums.CollectionVisibility;
import service.PersonalLibraryService;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Tạ Văn Huy
 * FEATURE GROUP: Hồ sơ / thư viện cá nhân
 * RELATED USE CASES: UC-9
 * PURPOSE: Nhận request profile/library từ ConsoleView và gọi PersonalLibraryService.
 */
public class PersonalLibraryController {
    private final PersonalLibraryService personalLibraryService;

    public PersonalLibraryController(PersonalLibraryService personalLibraryService) {
        this.personalLibraryService = personalLibraryService;
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Xem hồ sơ cá nhân
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: Điều phối xem profile của chính user.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> ProfileRepository -> SessionManager.
     */
    public OperationResult<UserProfile> viewProfile() {
        return personalLibraryService.viewProfile();
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Cập nhật hồ sơ cá nhân
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối cập nhật fullName và bio.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> ProfileRepository -> SessionManager.
     */
    public OperationResult<UserProfile> updateProfile(String fullName, String bio) {
        return personalLibraryService.updateProfile(fullName, bio);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Xem tài liệu mình upload
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: Điều phối xem tài liệu do user upload, gồm lý do từ chối nếu REJECTED.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<List<DocumentItem>> myUploadedDocuments() {
        return personalLibraryService.myUploadedDocuments();
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Lưu tài liệu vào thư viện cá nhân
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối lưu tài liệu APPROVED vào thư viện cá nhân.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> PersonalLibraryRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<PersonalLibrary> saveApprovedDocument(String documentId) {
        return personalLibraryService.saveApprovedDocument(documentId);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Xem thư viện cá nhân
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: Điều phối xem tài liệu đã lưu.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> PersonalLibraryRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<List<DocumentItem>> viewSavedDocuments() {
        return personalLibraryService.viewSavedDocuments();
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Tạo collection
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Điều phối tạo collection PRIVATE/SHARED.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> PersonalLibraryRepository -> SessionManager.
     */
    public OperationResult<Collection> createCollection(String name, CollectionVisibility visibility) {
        return personalLibraryService.createCollection(name, visibility);
    }
}
