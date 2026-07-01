# TEAM_ASSIGNMENT

| Thành viên | Use Case phụ trách | Nhóm chức năng | Class chính |
|---|---|---|---|
| Nguyễn Xuân Đại | UC-1, UC-5 | Đăng ký, upload và kiểm duyệt tài liệu | `RegistrationController`, `RegistrationService`, `DocumentUploadController`, `DocumentUploadService`, `DocumentReviewController`, `DocumentReviewService` |
| Huỳnh Duy Tâm | UC-2, UC-3 | Khôi phục mật khẩu, đăng nhập/đăng xuất, session | `AuthController`, `AuthService`, `PasswordRecoveryController`, `PasswordRecoveryService`, `OtpService`, `SessionManager` |
| Nguyễn Minh Luân | UC-4, UC-8 | Quản trị người dùng, phân quyền, báo cáo/thống kê | `UserManagementController`, `UserManagementService`, `ReportController`, `ReportService`, `ActivityLogService` |
| Hồ Nguyễn Quốc Nam | UC-6, UC-7 | Tra cứu, khai thác tài liệu, bình luận, đánh giá | `DocumentSearchController`, `DocumentSearchService`, `DocumentInteractionController`, `CommentService`, `RatingService` |
| Tạ Văn Huy | UC-9, UC-10 | Hồ sơ/thư viện cá nhân, forum yêu cầu tài liệu, kiểm soát nội dung | `PersonalLibraryController`, `PersonalLibraryService`, `RequestPostController`, `RequestPostService`, `ForumModerationController`, `ForumModerationService` |

## Ghi chú

- `MainMenuView` điều phối menu console theo role để tích hợp toàn bộ use case.
- Model, repository, util và seed data dùng chung cho Team 17.