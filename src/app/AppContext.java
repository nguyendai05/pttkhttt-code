package app;

import controller.AuthController;
import controller.DocumentInteractionController;
import controller.DocumentReviewController;
import controller.DocumentSearchController;
import controller.DocumentUploadController;
import controller.ForumModerationController;
import controller.PasswordRecoveryController;
import controller.PersonalLibraryController;
import controller.RegistrationController;
import controller.ReportController;
import controller.RequestPostController;
import controller.UserManagementController;
import repository.ActivityLogRepository;
import repository.CommentRepository;
import repository.DocumentRepository;
import repository.OtpRepository;
import repository.PersonalLibraryRepository;
import repository.ProfileRepository;
import repository.RatingRepository;
import repository.RequestPostRepository;
import repository.UserRepository;
import service.ActivityLogService;
import service.AuthService;
import service.CommentService;
import service.DocumentReviewService;
import service.DocumentSearchService;
import service.DocumentUploadService;
import service.ForumModerationService;
import service.OtpService;
import service.PasswordRecoveryService;
import service.PersonalLibraryService;
import service.RatingService;
import service.RegistrationService;
import service.ReportService;
import service.RequestPostService;
import service.SessionManager;
import service.UserManagementService;
import view.AuthConsoleView;
import view.DocumentConsoleView;


public class AppContext {
    private UserRepository userRepository = new UserRepository();
    private ProfileRepository profileRepository = new ProfileRepository();
    private DocumentRepository documentRepository = new DocumentRepository();
    private CommentRepository commentRepository = new CommentRepository();
    private RatingRepository ratingRepository = new RatingRepository();
    private PersonalLibraryRepository personalLibraryRepository = new PersonalLibraryRepository();
    private RequestPostRepository requestPostRepository = new RequestPostRepository();
    private ActivityLogRepository activityLogRepository = new ActivityLogRepository();
    private OtpRepository otpRepository = new OtpRepository();

    private SessionManager sessionManager = new SessionManager();
    private ActivityLogService activityLogService = new ActivityLogService(activityLogRepository);

    private RegistrationService registrationService = new RegistrationService(userRepository, profileRepository);
    private AuthService authService = new AuthService(userRepository, activityLogRepository, sessionManager);
    private OtpService otpService = new OtpService(otpRepository);
    private PasswordRecoveryService passwordRecoveryService = new PasswordRecoveryService(userRepository, otpService);

    private DocumentUploadService documentUploadService = new DocumentUploadService(documentRepository, sessionManager);
    private DocumentReviewService documentReviewService = new DocumentReviewService(documentRepository, activityLogRepository, sessionManager);
    private DocumentSearchService documentSearchService = new DocumentSearchService(documentRepository, sessionManager);
    private RatingService ratingService = new RatingService(ratingRepository, documentRepository, sessionManager);
    private CommentService commentService = new CommentService(commentRepository, documentRepository, activityLogRepository, sessionManager);

    private UserManagementService userManagementService = new UserManagementService(userRepository, sessionManager, activityLogService);
    private ReportService reportService = new ReportService(userRepository, documentRepository, requestPostRepository, activityLogService, sessionManager);
    private PersonalLibraryService personalLibraryService = new PersonalLibraryService(profileRepository, documentRepository, personalLibraryRepository, sessionManager);
    private RequestPostService requestPostService = new RequestPostService(requestPostRepository, documentRepository, commentRepository, activityLogService, sessionManager);
    private ForumModerationService forumModerationService = new ForumModerationService(requestPostRepository, commentRepository, activityLogRepository, sessionManager);

    private RegistrationController registrationController = new RegistrationController(registrationService);
    private AuthController authController = new AuthController(authService);
    private PasswordRecoveryController passwordRecoveryController = new PasswordRecoveryController(passwordRecoveryService);

    private DocumentUploadController documentUploadController = new DocumentUploadController(documentUploadService);
    private DocumentReviewController documentReviewController = new DocumentReviewController(documentReviewService);
    private DocumentSearchController documentSearchController = new DocumentSearchController(documentSearchService);
    private DocumentInteractionController documentInteractionController = new DocumentInteractionController(commentService, ratingService);

    private UserManagementController userManagementController = new UserManagementController(userManagementService);
    private ReportController reportController = new ReportController(reportService);
    private PersonalLibraryController personalLibraryController = new PersonalLibraryController(personalLibraryService);
    private RequestPostController requestPostController = new RequestPostController(requestPostService);
    private ForumModerationController forumModerationController = new ForumModerationController(forumModerationService);

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ProfileRepository getProfileRepository() {
        return profileRepository;
    }

    public DocumentRepository getDocumentRepository() {
        return documentRepository;
    }

    public CommentRepository getCommentRepository() {
        return commentRepository;
    }

    public RatingRepository getRatingRepository() {
        return ratingRepository;
    }

    public PersonalLibraryRepository getPersonalLibraryRepository() {
        return personalLibraryRepository;
    }

    public RequestPostRepository getRequestPostRepository() {
        return requestPostRepository;
    }

    public ActivityLogRepository getActivityLogRepository() {
        return activityLogRepository;
    }

    public OtpRepository getOtpRepository() {
        return otpRepository;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public ActivityLogService getActivityLogService() {
        return activityLogService;
    }

    public RegistrationService getRegistrationService() {
        return registrationService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public OtpService getOtpService() {
        return otpService;
    }

    public PasswordRecoveryService getPasswordRecoveryService() {
        return passwordRecoveryService;
    }

    public DocumentUploadService getDocumentUploadService() {
        return documentUploadService;
    }

    public DocumentReviewService getDocumentReviewService() {
        return documentReviewService;
    }

    public DocumentSearchService getDocumentSearchService() {
        return documentSearchService;
    }

    public RatingService getRatingService() {
        return ratingService;
    }

    public CommentService getCommentService() {
        return commentService;
    }

    public UserManagementService getUserManagementService() {
        return userManagementService;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public PersonalLibraryService getPersonalLibraryService() {
        return personalLibraryService;
    }

    public RequestPostService getRequestPostService() {
        return requestPostService;
    }

    public ForumModerationService getForumModerationService() {
        return forumModerationService;
    }

    public RegistrationController getRegistrationController() {
        return registrationController;
    }

    public AuthController getAuthController() {
        return authController;
    }

    public PasswordRecoveryController getPasswordRecoveryController() {
        return passwordRecoveryController;
    }

    public DocumentUploadController getDocumentUploadController() {
        return documentUploadController;
    }

    public DocumentReviewController getDocumentReviewController() {
        return documentReviewController;
    }

    public DocumentSearchController getDocumentSearchController() {
        return documentSearchController;
    }

    public DocumentInteractionController getDocumentInteractionController() {
        return documentInteractionController;
    }

    public UserManagementController getUserManagementController() {
        return userManagementController;
    }

    public ReportController getReportController() {
        return reportController;
    }

    public PersonalLibraryController getPersonalLibraryController() {
        return personalLibraryController;
    }

    public RequestPostController getRequestPostController() {
        return requestPostController;
    }

    public ForumModerationController getForumModerationController() {
        return forumModerationController;
    }

    public AuthConsoleView createAuthConsoleView() {
        return new AuthConsoleView(registrationController, authController, passwordRecoveryController);
    }

    public DocumentConsoleView createDocumentConsoleView() {
        return new DocumentConsoleView(documentUploadController, documentReviewController, documentSearchController, documentInteractionController);
    }
}
