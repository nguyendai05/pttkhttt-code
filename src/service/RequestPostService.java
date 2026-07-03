package service;

import model.Comment;
import model.DocumentItem;
import model.RequestPost;
import model.UserAccount;
import model.enums.DocumentStatus;
import model.enums.RequestStatus;
import model.enums.Role;
import repository.CommentRepository;
import repository.DocumentRepository;
import repository.RequestPostRepository;
import util.IdGenerator;
import util.InputValidator;
import util.OperationResult;

import java.util.List;

/**
 * OWNER: Táº¡ VÄƒn Huy
 * FEATURE GROUP: Forum yĂªu cáº§u tĂ i liá»‡u
 * RELATED USE CASES: UC-10a, UC-10b
 * PURPOSE: Táº¡o/quáº£n lĂ½ bĂ i yĂªu cáº§u cá»§a User vĂ  duyá»‡t bĂ i yĂªu cáº§u bá»Ÿi Admin.
 */
public class RequestPostService {
    private RequestPostRepository requestPostRepository;
    private DocumentRepository documentRepository;
    private CommentRepository commentRepository;
    private ActivityLogService activityLogService;
    private SessionManager sessionManager;

    public RequestPostService(RequestPostRepository requestPostRepository, DocumentRepository documentRepository,
                              CommentRepository commentRepository, ActivityLogService activityLogService,
                              SessionManager sessionManager) {
        this.requestPostRepository = requestPostRepository;
        this.documentRepository = documentRepository;
        this.commentRepository = commentRepository;
        this.activityLogService = activityLogService;
        this.sessionManager = sessionManager;
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10a - Táº¡o bĂ i yĂªu cáº§u tĂ i liá»‡u
     * ACTOR: User
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: User táº¡o RequestPost má»›i á»Ÿ tráº¡ng thĂ¡i PENDING_REVIEW.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<RequestPost> createPost(String title, String content) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chá»‰ User Ä‘Ă£ Ä‘Äƒng nháº­p Ä‘Æ°á»£c táº¡o bĂ i yĂªu cáº§u.");
        }
        if (InputValidator.isBlank(title) || InputValidator.isBlank(content)) {
            return OperationResult.fail("Title vĂ  content khĂ´ng Ä‘Æ°á»£c trá»‘ng.");
        }
        RequestPost post = new RequestPost(IdGenerator.nextId("REQ"), title.trim(), content.trim(), user.getId(), RequestStatus.PENDING_REVIEW);
        requestPostRepository.save(post);
        return OperationResult.ok("Táº¡o bĂ i yĂªu cáº§u thĂ nh cĂ´ng. BĂ i Ä‘ang chá» Admin duyá»‡t.", post);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10a - Xem bĂ i yĂªu cáº§u cá»§a tĂ´i
     * ACTOR: User
     * FLOW: Basic Flow
     * PURPOSE: User xem danh sĂ¡ch bĂ i yĂªu cáº§u do chĂ­nh mĂ¬nh táº¡o.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<List<RequestPost>> myPosts() {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chá»‰ User Ä‘Ă£ Ä‘Äƒng nháº­p Ä‘Æ°á»£c xem bĂ i cá»§a mĂ¬nh.");
        }
        return OperationResult.ok("BĂ i yĂªu cáº§u cá»§a tĂ´i.", requestPostRepository.findByCreator(user.getId()));
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10a - Sá»­a bĂ i yĂªu cáº§u cá»§a tĂ´i
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: User sá»­a bĂ i cá»§a chĂ­nh mĂ¬nh náº¿u bĂ i chÆ°a FULFILLED/HIDDEN/DELETED; bĂ i OPEN chuyá»ƒn láº¡i PENDING_REVIEW.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<RequestPost> updateOwnPost(String postId, String title, String content) {
        UserAccount user = requireUser();
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (user == null || post == null || !post.getCreatorId().equals(user.getId())) {
            return OperationResult.fail("Chá»‰ chá»§ bĂ i Ä‘Æ°á»£c sá»­a bĂ i yĂªu cáº§u.");
        }
        if (post.getStatus() == RequestStatus.FULFILLED || post.getStatus() == RequestStatus.HIDDEN || post.getStatus() == RequestStatus.DELETED) {
            return OperationResult.fail("BĂ i hiá»‡n táº¡i khĂ´ng cĂ²n há»£p lá»‡ Ä‘á»ƒ sá»­a.");
        }
        if (InputValidator.isBlank(title) || InputValidator.isBlank(content)) {
            return OperationResult.fail("Title vĂ  content khĂ´ng Ä‘Æ°á»£c trá»‘ng.");
        }
        post.setTitle(title.trim());
        post.setContent(content.trim());
        if (post.getStatus() == RequestStatus.OPEN) {
            post.setStatus(RequestStatus.PENDING_REVIEW);
        }
        requestPostRepository.save(post);
        return OperationResult.ok("Sá»­a bĂ i yĂªu cáº§u thĂ nh cĂ´ng.", post);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10a - XĂ³a bĂ i yĂªu cáº§u cá»§a tĂ´i
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: User xĂ³a má»m bĂ i cá»§a chĂ­nh mĂ¬nh náº¿u bĂ i cĂ²n há»£p lá»‡.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<RequestPost> deleteOwnPost(String postId) {
        UserAccount user = requireUser();
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (user == null || post == null || !post.getCreatorId().equals(user.getId())) {
            return OperationResult.fail("Chá»‰ chá»§ bĂ i Ä‘Æ°á»£c xĂ³a bĂ i yĂªu cáº§u.");
        }
        if (post.getStatus() == RequestStatus.FULFILLED) {
            return OperationResult.fail("BĂ i FULFILLED khĂ´ng Ä‘Æ°á»£c xĂ³a trong mĂ´ phá»ng nĂ y.");
        }
        post.setDeleted(true);
        post.setStatus(RequestStatus.DELETED);
        requestPostRepository.save(post);
        return OperationResult.ok("XĂ³a bĂ i yĂªu cáº§u thĂ nh cĂ´ng.", post);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10a - BĂ¬nh luáº­n vĂ o bĂ i OPEN
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: User bĂ¬nh luáº­n má»™t cáº¥p vĂ o bĂ i yĂªu cáº§u Ä‘ang OPEN.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> CommentRepository/RequestPostRepository -> SessionManager.
     */
    public OperationResult<Comment> commentOpenPost(String postId, String content) {
        UserAccount user = requireUser();
        if (user == null) {
            return OperationResult.fail("Chá»‰ User Ä‘Ă£ Ä‘Äƒng nháº­p Ä‘Æ°á»£c bĂ¬nh luáº­n bĂ i yĂªu cáº§u.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null || post.getStatus() != RequestStatus.OPEN || post.isHidden() || post.isDeleted()) {
            return OperationResult.fail("Chá»‰ Ä‘Æ°á»£c bĂ¬nh luáº­n vĂ o bĂ i OPEN Ä‘ang hiá»ƒn thá»‹.");
        }
        if (InputValidator.isBlank(content)) {
            return OperationResult.fail("Ná»™i dung bĂ¬nh luáº­n khĂ´ng Ä‘Æ°á»£c trá»‘ng.");
        }
        Comment comment = new Comment(IdGenerator.nextId("COM"), user.getId(), "REQUEST", post.getId(), content.trim());
        commentRepository.save(comment);
        return OperationResult.ok("BĂ¬nh luáº­n vĂ o bĂ i yĂªu cáº§u thĂ nh cĂ´ng.", comment);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10a - ÄĂ¡nh dáº¥u yĂªu cáº§u Ä‘Ă£ Ä‘Ă¡p á»©ng
     * ACTOR: User
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Chá»§ bĂ i chuyá»ƒn bĂ i OPEN sang FULFILLED vĂ  cĂ³ thá»ƒ liĂªn káº¿t documentId APPROVED.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository/DocumentRepository -> SessionManager.
     */
    public OperationResult<RequestPost> markFulfilled(String postId, String documentId) {
        UserAccount user = requireUser();
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (user == null || post == null || !post.getCreatorId().equals(user.getId())) {
            return OperationResult.fail("Chá»‰ chá»§ bĂ i Ä‘Æ°á»£c Ä‘Ă¡nh dáº¥u FULFILLED.");
        }
        if (post.getStatus() != RequestStatus.OPEN) {
            return OperationResult.fail("Chá»‰ bĂ i OPEN má»›i Ä‘Æ°á»£c Ä‘Ă¡nh dáº¥u FULFILLED.");
        }
        if (!InputValidator.isBlank(documentId)) {
            DocumentItem document = documentRepository.findById(documentId).orElse(null);
            if (document == null || document.getStatus() != DocumentStatus.APPROVED) {
                return OperationResult.fail("documentId liĂªn káº¿t pháº£i lĂ  tĂ i liá»‡u APPROVED.");
            }
            post.setLinkedDocumentId(documentId.trim());
        }
        post.setStatus(RequestStatus.FULFILLED);
        requestPostRepository.save(post);
        return OperationResult.ok("ÄĂ¡nh dáº¥u bĂ i yĂªu cáº§u FULFILLED thĂ nh cĂ´ng.", post);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10b - Duyá»‡t bĂ i yĂªu cáº§u tĂ i liá»‡u
     * ACTOR: Admin
     * FLOW: Basic Flow
     * PURPOSE: Admin chuyá»ƒn bĂ i PENDING_REVIEW sang OPEN Ä‘á»ƒ hiá»ƒn thá»‹ cĂ´ng khai vĂ  ghi ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<RequestPost> approvePost(String postId) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chá»‰ Admin Ä‘Æ°á»£c duyá»‡t bĂ i yĂªu cáº§u.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null || post.getStatus() != RequestStatus.PENDING_REVIEW) {
            return OperationResult.fail("BĂ i khĂ´ng tá»“n táº¡i hoáº·c khĂ´ng á»Ÿ tráº¡ng thĂ¡i PENDING_REVIEW.");
        }
        post.setStatus(RequestStatus.OPEN);
        requestPostRepository.save(post);
        activityLogService.log(admin.getId(), "APPROVE_REQUEST", "REQUEST", post.getId(), "Admin duyá»‡t bĂ i yĂªu cáº§u tĂ i liá»‡u.");
        return OperationResult.ok("Duyá»‡t bĂ i yĂªu cáº§u thĂ nh cĂ´ng.", post);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10b - Tá»« chá»‘i bĂ i yĂªu cáº§u tĂ i liá»‡u
     * ACTOR: Admin
     * FLOW: Alternative Flow / Exception Flow
     * PURPOSE: Admin chuyá»ƒn bĂ i PENDING_REVIEW sang REJECTED, báº¯t buá»™c lĂ½ do vĂ  ghi ActivityLog.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository/ActivityLogRepository -> SessionManager.
     */
    public OperationResult<RequestPost> rejectPost(String postId, String reason) {
        UserAccount admin = requireAdmin();
        if (admin == null) {
            return OperationResult.fail("Chá»‰ Admin Ä‘Æ°á»£c tá»« chá»‘i bĂ i yĂªu cáº§u.");
        }
        if (InputValidator.isBlank(reason)) {
            return OperationResult.fail("LĂ½ do tá»« chá»‘i lĂ  báº¯t buá»™c.");
        }
        RequestPost post = requestPostRepository.findById(postId).orElse(null);
        if (post == null || post.getStatus() != RequestStatus.PENDING_REVIEW) {
            return OperationResult.fail("BĂ i khĂ´ng tá»“n táº¡i hoáº·c khĂ´ng á»Ÿ tráº¡ng thĂ¡i PENDING_REVIEW.");
        }
        post.setStatus(RequestStatus.REJECTED);
        post.setRejectionReason(reason.trim());
        requestPostRepository.save(post);
        activityLogService.log(admin.getId(), "REJECT_REQUEST", "REQUEST", post.getId(), reason.trim());
        return OperationResult.ok("Tá»« chá»‘i bĂ i yĂªu cáº§u thĂ nh cĂ´ng.", post);
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10b - Hiá»ƒn thá»‹ bĂ i yĂªu cáº§u cĂ´ng khai
     * ACTOR: User/Guest/Admin/Moderator
     * FLOW: Basic Flow
     * PURPOSE: Tráº£ vá» cĂ¡c bĂ i OPEN/FULFILLED Ä‘á»ƒ hiá»ƒn thá»‹ cĂ´ng khai trĂªn forum.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public List<RequestPost> publicPosts() {
        return requestPostRepository.findPublicPosts();
    }

    /**
     * OWNER: Táº¡ VÄƒn Huy
     * USE CASE: UC-10b - Xem bĂ i yĂªu cáº§u chá» duyá»‡t
     * ACTOR: Admin
     * FLOW: Basic Flow / Exception Flow
     * PURPOSE: Admin xem danh sĂ¡ch bĂ i PENDING_REVIEW Ä‘á»ƒ duyá»‡t hoáº·c tá»« chá»‘i.
     * SEQUENCE NOTE: ConsoleView -> RequestPostController -> RequestPostService -> RequestPostRepository -> SessionManager.
     */
    public OperationResult<List<RequestPost>> pendingPosts() {
        if (requireAdmin() == null) {
            return OperationResult.fail("Chá»‰ Admin Ä‘Æ°á»£c xem bĂ i yĂªu cáº§u chá» duyá»‡t.");
        }
        return OperationResult.ok("Danh sĂ¡ch bĂ i yĂªu cáº§u PENDING_REVIEW.", requestPostRepository.findByStatus(RequestStatus.PENDING_REVIEW));
    }

    private UserAccount requireUser() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.getRole() == Role.USER ? actor : null;
    }

    private UserAccount requireAdmin() {
        UserAccount actor = sessionManager.getCurrentUser().orElse(null);
        return actor != null && actor.getRole() == Role.ADMIN ? actor : null;
    }
}
