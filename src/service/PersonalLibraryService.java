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
 * OWNER: Táº¡ VÄƒn Huy
 * FEATURE GROUP: Há»“ sÆ¡ / thÆ° viá»‡n cĂ¡ nhĂ¢n
 * RELATED USE CASES: UC-9
 * PURPOSE: Quáº£n lĂ½ profile, tĂ i liá»‡u Ä‘Ă£ upload, tĂ i liá»‡u Ä‘Ă£ lÆ°u vĂ  collection cĂ¡ nhĂ¢n.
 */
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

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-9 - Xem há»“ sÆ¡ cĂ¡ nhĂ¢n
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: User xem profile cá»§a chĂ­nh mĂ¬nh.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> ProfileRepository -> SessionManager.
     */
    public OperationResult<UserProfile> viewProfile() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chá»‰ User Ä‘Ă£ Ä‘Äƒng nháº­p Ä‘Æ°á»£c xem profile cĂ¡ nhĂ¢n.");
        }
        return OperationResult.ok("Há»“ sÆ¡ cĂ¡ nhĂ¢n.", profileRepository.findByUserId(user.getId()).orElse(null));
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-9 - Cáº­p nháº­t há»“ sÆ¡ cĂ¡ nhĂ¢n
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: User cáº­p nháº­t fullName vĂ  bio cá»§a chĂ­nh mĂ¬nh.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> ProfileRepository -> SessionManager.
     */
    public OperationResult<UserProfile> updateProfile(String fullName, String bio) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chá»‰ User Ä‘Ă£ Ä‘Äƒng nháº­p Ä‘Æ°á»£c cáº­p nháº­t profile.");
        }
        if (InputValidator.isBlank(fullName)) {
            return OperationResult.fail("FullName khĂ´ng Ä‘Æ°á»£c trá»‘ng.");
        }
        UserProfile profile = profileRepository.findByUserId(user.getId())
                .orElse(new UserProfile(IdGenerator.nextId("PRO"), user.getId(), fullName.trim(), ""));
        profile.setFullName(fullName.trim());
        profile.setBio(bio == null ? "" : bio.trim());
        profileRepository.save(profile);
        return OperationResult.ok("Cáº­p nháº­t profile thĂ nh cĂ´ng.", profile);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-9 - Xem tĂ i liá»‡u mĂ¬nh upload
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: User xem tĂ i liá»‡u do chĂ­nh mĂ¬nh upload, bao gá»“m lĂ½ do tá»« chá»‘i náº¿u REJECTED.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> DocumentRepository -> SessionManager.
     */
    public OperationResult<List<DocumentItem>> myUploadedDocuments() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chá»‰ User Ä‘Ă£ Ä‘Äƒng nháº­p Ä‘Æ°á»£c xem tĂ i liá»‡u mĂ¬nh upload.");
        }
        return OperationResult.ok("Danh sĂ¡ch tĂ i liá»‡u Ä‘Ă£ upload.", documentRepository.findByUploader(user.getId()));
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-9 - LÆ°u tĂ i liá»‡u vĂ o thÆ° viá»‡n cĂ¡ nhĂ¢n
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: User lÆ°u tĂ i liá»‡u APPROVED vĂ o thÆ° viá»‡n cĂ¡ nhĂ¢n cá»§a chĂ­nh mĂ¬nh.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> PersonalLibraryRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<PersonalLibrary> saveApprovedDocument(String documentId) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chá»‰ User Ä‘Ă£ Ä‘Äƒng nháº­p Ä‘Æ°á»£c lÆ°u tĂ i liá»‡u.");
        }
        DocumentItem document = documentRepository.findById(documentId).orElse(null);
        if (document == null || document.getStatus() != DocumentStatus.APPROVED) {
            return OperationResult.fail("Chá»‰ Ä‘Æ°á»£c lÆ°u tĂ i liá»‡u APPROVED.");
        }
        PersonalLibrary library = personalLibraryRepository.getOrCreate(user.getId());
        if (!library.getSavedDocumentIds().contains(documentId)) {
            library.getSavedDocumentIds().add(documentId);
        }
        personalLibraryRepository.save(library);
        return OperationResult.ok("LÆ°u tĂ i liá»‡u vĂ o thÆ° viá»‡n cĂ¡ nhĂ¢n thĂ nh cĂ´ng.", library);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-9 - Xem thÆ° viá»‡n cĂ¡ nhĂ¢n
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: User xem cĂ¡c tĂ i liá»‡u Ä‘Ă£ lÆ°u trong thÆ° viá»‡n cĂ¡ nhĂ¢n.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> PersonalLibraryRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<List<DocumentItem>> viewSavedDocuments() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chá»‰ User Ä‘Ă£ Ä‘Äƒng nháº­p Ä‘Æ°á»£c xem thÆ° viá»‡n cĂ¡ nhĂ¢n.");
        }
        PersonalLibrary library = personalLibraryRepository.getOrCreate(user.getId());
        List<DocumentItem> documents = library.getSavedDocumentIds().stream()
                .map(id -> documentRepository.findById(id).orElse(null))
                .filter(document -> document != null)
                .collect(Collectors.toList());
        return OperationResult.ok("Danh sĂ¡ch tĂ i liá»‡u Ä‘Ă£ lÆ°u.", documents);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-9 - Táº¡o collection
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: User táº¡o collection Ä‘Æ¡n giáº£n PRIVATE/SHARED trong thÆ° viá»‡n cĂ¡ nhĂ¢n.
     * SEQUENCE NOTE: ConsoleView -> PersonalLibraryController -> PersonalLibraryService -> PersonalLibraryRepository -> SessionManager.
     */
    public OperationResult<Collection> createCollection(String name, CollectionVisibility visibility) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chá»‰ User Ä‘Ă£ Ä‘Äƒng nháº­p Ä‘Æ°á»£c táº¡o collection.");
        }
        if (InputValidator.isBlank(name)) {
            return OperationResult.fail("TĂªn collection khĂ´ng Ä‘Æ°á»£c trá»‘ng.");
        }
        Collection collection = new Collection(IdGenerator.nextId("COL"), user.getId(), name.trim(), visibility);
        PersonalLibrary library = personalLibraryRepository.getOrCreate(user.getId());
        library.getCollections().add(collection);
        personalLibraryRepository.save(library);
        return OperationResult.ok("Táº¡o collection thĂ nh cĂ´ng.", collection);
    }

    private UserAccount requireUser() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.getRole() == Role.USER ? actor : null;
    }
}
