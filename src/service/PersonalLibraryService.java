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


public class PersonalLibraryService {
    private ProfileRepository profileRepository;
    private DocumentRepository documentRepository;
    private PersonalLibraryRepository personalLibraryRepository;
    private SessionManager sessionManager;

    public PersonalLibraryService(ProfileRepository profileRepository, DocumentRepository documentRepository,
                                  PersonalLibraryRepository personalLibraryRepository, SessionManager sessionManager) {
        this.profileRepository = profileRepository;
        this.documentRepository = documentRepository;
        this.personalLibraryRepository = personalLibraryRepository;
        this.sessionManager = sessionManager;
    }



    public OperationResult<UserProfile> viewProfile() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được xem profile cá nhân.");
        }
        return OperationResult.ok("Hồ sơ cá nhân.", profileRepository.findByUserId(user.getId()).orElse(null));
    }



    public OperationResult<UserProfile> updateProfile(String fullName, String bio) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được cập nhật profile.");
        }
        if (InputValidator.isBlank(fullName)) {
            return OperationResult.fail("FullName không được trống.");
        }
        UserProfile profile = profileRepository.findByUserId(user.getId())
                .orElse(new UserProfile(IdGenerator.nextId("PRO"), user.getId(), fullName.trim(), ""));
        profile.setFullName(fullName.trim());
        profile.setBio(bio == null ? "" : bio.trim());
        profileRepository.save(profile);
        return OperationResult.ok("Cập nhật profile thành công.", profile);
    }



    public OperationResult<List<DocumentItem>> myUploadedDocuments() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được xem tài liệu mình upload.");
        }
        return OperationResult.ok("Danh sách tài liệu đã upload.", documentRepository.findByUploader(user.getId()));
    }



    public OperationResult<PersonalLibrary> saveApprovedDocument(String documentId) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được lưu tài liệu.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.getStatus() != DocumentStatus.APPROVED) {
            return OperationResult.fail("Chỉ được lưu tài liệu APPROVED.");
        }
        PersonalLibrary library = personalLibraryRepository.getOrCreate(user.getId());
        if (!library.getSavedDocumentIds().contains(documentId)) {
            library.getSavedDocumentIds().add(documentId);
        }
        personalLibraryRepository.save(library);
        return OperationResult.ok("Lưu tài liệu vào thư viện cá nhân thành công.", library);
    }



    public OperationResult<List<DocumentItem>> viewSavedDocuments() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được xem thư viện cá nhân.");
        }
        PersonalLibrary library = personalLibraryRepository.getOrCreate(user.getId());
        List<DocumentItem> documents = library.getSavedDocumentIds().stream()
                .map(id -> documentRepository.findById(id).orElse(null))
                .filter(document -> document != null)
                .collect(Collectors.toList());
        return OperationResult.ok("Danh sách tài liệu đã lưu.", documents);
    }



    public OperationResult<Collection> createCollection(String name, CollectionVisibility visibility) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chỉ User đã đăng nhập được tạo collection.");
        }
        if (InputValidator.isBlank(name)) {
            return OperationResult.fail("Tên collection không được trống.");
        }
        Collection collection = new Collection(IdGenerator.nextId("COL"), user.getId(), name.trim(), visibility);
        PersonalLibrary library = personalLibraryRepository.getOrCreate(user.getId());
        library.getCollections().add(collection);
        personalLibraryRepository.save(library);
        return OperationResult.ok("Tạo collection thành công.", collection);
    }

    private UserAccount requireUser() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.getRole() == Role.USER ? actor : null;
    }
}
