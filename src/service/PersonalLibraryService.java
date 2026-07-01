package service;

import model.Collection;
import model.DocumentItem;
import model.PersonalLibrary;
import model.UserAccount;
import model.UserProfile;
import model.enums.CollectionVisibility;
import model.enums.DocumentStatus;
import model.enums.Role;
import repository.DocumentRepository;
import repository.PersonalLibraryRepository;
import repository.ProfileRepository;
import util.IdGenerator;
import util.InputValidator;
import util.OperationResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OWNER: Tạ Văn Huy
 * FEATURE GROUP: Hồ sơ / thư viện cá nhân
 * RELATED USE CASES: UC-9
 * PURPOSE: Quản lý profile, tài liệu đã upload, tài liệu đã lưu và collection cá nhân.
 */
public class PersonalLibraryService {
    private final ProfileRepository profileRepository;
    private final DocumentRepository documentRepository;
    private final PersonalLibraryRepository personalLibraryRepository;
    private final SessionManager sessionManager;

    public PersonalLibraryService(ProfileRepository profileRepository, DocumentRepository documentRepository,
                                  PersonalLibraryRepository personalLibraryRepository, SessionManager sessionManager) {
        this.profileRepository = profileRepository;
        this.documentRepository = documentRepository;
        this.personalLibraryRepository = personalLibraryRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Xem hồ sơ cá nhân
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: User xem profile của chính mình.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> ProfileRepository -> SessionManager.
     */
    public OperationResult<UserProfile> viewProfile() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được xem profile cá nhân.");
        }
        return OperationResult.ok("Hồ sơ cá nhân.", profileRepository.findByUserId(user.id).orElse(null));
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Cập nhật hồ sơ cá nhân
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: User cập nhật fullName và bio của chính mình.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> ProfileRepository -> SessionManager.
     */
    public OperationResult<UserProfile> updateProfile(String fullName, String bio) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được cập nhật profile.");
        }
        if (InputValidator.isBlank(fullName)) {
            return OperationResult.fail("FullName không được trống.");
        }
        UserProfile profile = profileRepository.findByUserId(user.id)
                .orElse(new UserProfile(IdGenerator.nextId("PRO"), user.id, fullName.trim(), ""));
        profile.fullName = fullName.trim();
        profile.bio = bio == null ? "" : bio.trim();
        profileRepository.save(profile);
        return OperationResult.ok("Cập nhật profile thành công.", profile);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Xem tài liệu mình upload
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: User xem tài liệu do chính mình upload, bao gồm lý do từ chối nếu REJECTED.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<List<DocumentItem>> myUploadedDocuments() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được xem tài liệu mình upload.");
        }
        return OperationResult.ok("Danh sách tài liệu đã upload.", documentRepository.findByUploader(user.id));
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Lưu tài liệu vào thư viện cá nhân
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: User lưu tài liệu APPROVED vào thư viện cá nhân của chính mình.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> PersonalLibraryRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<PersonalLibrary> saveApprovedDocument(String documentId) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được lưu tài liệu.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.status != DocumentStatus.APPROVED) {
            return OperationResult.fail("Chỉ được lưu tài liệu APPROVED.");
        }
        PersonalLibrary library = personalLibraryRepository.getOrCreate(user.id);
        if (!library.savedDocumentIds.contains(documentId)) {
            library.savedDocumentIds.add(documentId);
        }
        personalLibraryRepository.save(library);
        return OperationResult.ok("Lưu tài liệu vào thư viện cá nhân thành công.", library);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Xem thư viện cá nhân
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: User xem các tài liệu đã lưu trong thư viện cá nhân.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> PersonalLibraryRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<List<DocumentItem>> viewSavedDocuments() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được xem thư viện cá nhân.");
        }
        PersonalLibrary library = personalLibraryRepository.getOrCreate(user.id);
        List<DocumentItem> documents = library.savedDocumentIds.stream()
                .map(id -> documentRepository.findById(id).orElse(null))
                .filter(document -> document != null)
                .collect(Collectors.toList());
        return OperationResult.ok("Danh sách tài liệu đã lưu.", documents);
    }

    /**
     * OWNER: Tạ Văn Huy
     * USE CASE: UC-9 - Tạo collection
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: User tạo collection đơn giản PRIVATE/SHARED trong thư viện cá nhân.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> PersonalLibraryRepository -> SessionManager.
     */
    public OperationResult<Collection> createCollection(String name, CollectionVisibility visibility) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được tạo collection.");
        }
        if (InputValidator.isBlank(name)) {
            return OperationResult.fail("Tên collection không được trống.");
        }
        Collection collection = new Collection(IdGenerator.nextId("COL"), user.id, name.trim(), visibility);
        PersonalLibrary library = personalLibraryRepository.getOrCreate(user.id);
        library.collections.add(collection);
        personalLibraryRepository.save(library);
        return OperationResult.ok("Tạo collection thành công.", collection);
    }

    private UserAccount requireUser() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.role == Role.USER ? actor : null;
    }
}
