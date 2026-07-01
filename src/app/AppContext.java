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

/**
 * OWNER: Team 17
 * FEATURE GROUP: Application bootstrap
 * PURPOSE: Gom repository in-memory và wiring controller/service cho các phần 2, 3, 4.
 */
public class AppContext {
    public final UserRepository userRepository = new UserRepository();
    public final ProfileRepository profileRepository = new ProfileRepository();
    public final DocumentRepository documentRepository = new DocumentRepository();
    public final CommentRepository commentRepository = new CommentRepository();
    public final RatingRepository ratingRepository = new RatingRepository();
    public final PersonalLibraryRepository personalLibraryRepository = new PersonalLibraryRepository();
    public final RequestPostRepository requestPostRepository = new RequestPostRepository();
    public final ActivityLogRepository activityLogRepository = new ActivityLogRepository();
    public final OtpRepository otpRepository = new OtpRepository();

    public final SessionManager sessionManager = new SessionManager();
    public final ActivityLogService activityLogService = new ActivityLogService(activityLogRepository);

    public final RegistrationService registrationService = new RegistrationService(userRepository, profileRepository);
    public final AuthService authService = new AuthService(userRepository, activityLogRepository, sessionManager);
    public final OtpService otpService = new OtpService(otpRepository);
    public final PasswordRecoveryService passwordRecoveryService = new PasswordRecoveryService(userRepository, otpService);

    public final DocumentUploadService documentUploadService = new DocumentUploadService(documentRepository, sessionManager);
    public final DocumentReviewService documentReviewService = new DocumentReviewService(documentRepository, activityLogRepository, sessionManager);
    public final DocumentSearchService documentSearchService = new DocumentSearchService(documentRepository, sessionManager);
    public final RatingService ratingService = new RatingService(ratingRepository, documentRepository, sessionManager);
    public final CommentService commentService = new CommentService(commentRepository, documentRepository, activityLogRepository, sessionManager);

    public final UserManagementService userManagementService = new UserManagementService(userRepository, sessionManager, activityLogService);
    public final ReportService reportService = new ReportService(userRepository, documentRepository, requestPostRepository, activityLogService, sessionManager);
    public final PersonalLibraryService personalLibraryService = new PersonalLibraryService(profileRepository, documentRepository, personalLibraryRepository, sessionManager);
    public final RequestPostService requestPostService = new RequestPostService(requestPostRepository, documentRepository, commentRepository, activityLogService, sessionManager);
    public final ForumModerationService forumModerationService = new ForumModerationService(requestPostRepository, commentRepository, activityLogRepository, sessionManager);

    public final RegistrationController registrationController = new RegistrationController(registrationService);
    public final AuthController authController = new AuthController(authService);
    public final PasswordRecoveryController passwordRecoveryController = new PasswordRecoveryController(passwordRecoveryService);

    public final DocumentUploadController documentUploadController = new DocumentUploadController(documentUploadService);
    public final DocumentReviewController documentReviewController = new DocumentReviewController(documentReviewService);
    public final DocumentSearchController documentSearchController = new DocumentSearchController(documentSearchService);
    public final DocumentInteractionController documentInteractionController = new DocumentInteractionController(commentService, ratingService);

    public final UserManagementController userManagementController = new UserManagementController(userManagementService);
    public final ReportController reportController = new ReportController(reportService);
    public final PersonalLibraryController personalLibraryController = new PersonalLibraryController(personalLibraryService);
    public final RequestPostController requestPostController = new RequestPostController(requestPostService);
    public final ForumModerationController forumModerationController = new ForumModerationController(forumModerationService);

    public AuthConsoleView createAuthConsoleView() {
        return new AuthConsoleView(registrationController, authController, passwordRecoveryController);
    }

    public DocumentConsoleView createDocumentConsoleView() {
        return new DocumentConsoleView(documentUploadController, documentReviewController, documentSearchController, documentInteractionController);
    }
}