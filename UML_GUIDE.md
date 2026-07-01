# UML GUIDE — Hệ thống Quản lý & Chia sẻ Tài liệu

> Tài liệu hỗ trợ vẽ **Use Case Diagram**, **Activity Diagram** và **Sequence Diagram**.
> Dựa trên mã nguồn Java Console MVC — Team 17.

---

## MỤC LỤC

| UC | Tên | Thành viên |
|---|---|---|
| UC-1 | Đăng ký tài khoản | Nguyễn Xuân Đại |
| UC-2 | Đăng nhập / Đăng xuất | Huỳnh Duy Tâm |
| UC-3 | Khôi phục mật khẩu | Huỳnh Duy Tâm |
| UC-4 | Quản trị người dùng | Nguyễn Minh Luân |
| UC-5 | Upload & Kiểm duyệt tài liệu | Nguyễn Xuân Đại |
| UC-6 | Tra cứu & Tải xuống tài liệu | Hồ Nguyễn Quốc Nam |
| UC-7 | Bình luận & Đánh giá tài liệu | Hồ Nguyễn Quốc Nam |
| UC-8 | Báo cáo / Thống kê & Activity Log | Nguyễn Minh Luân |
| UC-9 | Hồ sơ & Thư viện cá nhân | Tạ Văn Huy |
| UC-10 | Forum yêu cầu tài liệu & Kiểm soát nội dung | Tạ Văn Huy |

---

## ACTORS TỔNG QUAN

| Actor | Mô tả | Use Cases |
|---|---|---|
| **Guest** | Người dùng chưa đăng nhập | UC-1, UC-2, UC-3, UC-6 (xem cơ bản) |
| **User** | Người dùng đã đăng nhập, role USER | UC-2, UC-5 (upload), UC-6, UC-7, UC-9, UC-10 (tạo/bình luận) |
| **Moderator** | Kiểm duyệt viên | UC-2, UC-5 (review), UC-6, UC-10 (kiểm soát nội dung) |
| **Admin** | Quản trị viên | UC-2, UC-4, UC-8, UC-10 (duyệt bài request) |

---

## CLASS TỔNG QUAN

### Tầng View
| Class | File | Vai trò |
|---|---|---|
| `AuthConsoleView` | `view/AuthConsoleView.java` | Nhập liệu đăng ký, đăng nhập, khôi phục MK |
| `DocumentConsoleView` | `view/DocumentConsoleView.java` | Nhập liệu tài liệu, bình luận, đánh giá |
| `MainMenuView` | `view/MainMenuView.java` | Menu chính, điều phối theo role |

### Tầng Controller
| Class | File |
|---|---|
| `RegistrationController` | `controller/RegistrationController.java` |
| `AuthController` | `controller/AuthController.java` |
| `PasswordRecoveryController` | `controller/PasswordRecoveryController.java` |
| `UserManagementController` | `controller/UserManagementController.java` |
| `DocumentUploadController` | `controller/DocumentUploadController.java` |
| `DocumentReviewController` | `controller/DocumentReviewController.java` |
| `DocumentSearchController` | `controller/DocumentSearchController.java` |
| `DocumentInteractionController` | `controller/DocumentInteractionController.java` |
| `ReportController` | `controller/ReportController.java` |
| `PersonalLibraryController` | `controller/PersonalLibraryController.java` |
| `RequestPostController` | `controller/RequestPostController.java` |
| `ForumModerationController` | `controller/ForumModerationController.java` |

### Tầng Service
| Class | File |
|---|---|
| `RegistrationService` | `service/RegistrationService.java` |
| `AuthService` | `service/AuthService.java` |
| `SessionManager` | `service/SessionManager.java` |
| `PasswordRecoveryService` | `service/PasswordRecoveryService.java` |
| `OtpService` | `service/OtpService.java` |
| `UserManagementService` | `service/UserManagementService.java` |
| `DocumentUploadService` | `service/DocumentUploadService.java` |
| `DocumentReviewService` | `service/DocumentReviewService.java` |
| `DocumentSearchService` | `service/DocumentSearchService.java` |
| `CommentService` | `service/CommentService.java` |
| `RatingService` | `service/RatingService.java` |
| `ReportService` | `service/ReportService.java` |
| `ActivityLogService` | `service/ActivityLogService.java` |
| `PersonalLibraryService` | `service/PersonalLibraryService.java` |
| `RequestPostService` | `service/RequestPostService.java` |
| `ForumModerationService` | `service/ForumModerationService.java` |

### Tầng Repository
| Class | File |
|---|---|
| `UserRepository` | `repository/UserRepository.java` |
| `ProfileRepository` | `repository/ProfileRepository.java` |
| `DocumentRepository` | `repository/DocumentRepository.java` |
| `CommentRepository` | `repository/CommentRepository.java` |
| `RatingRepository` | `repository/RatingRepository.java` |
| `OtpRepository` | `repository/OtpRepository.java` |
| `ActivityLogRepository` | `repository/ActivityLogRepository.java` |
| `PersonalLibraryRepository` | `repository/PersonalLibraryRepository.java` |
| `RequestPostRepository` | `repository/RequestPostRepository.java` |

---

# PHẦN 1 — Nguyễn Xuân Đại (UC-1, UC-5)

---

## UC-1: Đăng ký tài khoản

### Thông tin chung

| Mục | Nội dung |
|---|---|
| **Actor** | Guest |
| **Controller** | `RegistrationController` |
| **Service** | `RegistrationService`, `ActivityLogService` |
| **Repository** | `UserRepository`, `ProfileRepository`, `ActivityLogRepository` |
| **View** | `AuthConsoleView` |
| **Method chính** | `RegistrationController.register(username, email, password, confirmPassword, fullName)` → `RegistrationService.register(...)` |

### Basic Flow

1. Guest chọn "Đăng ký" từ menu.
2. `AuthConsoleView` hiển thị form nhập: username, email, password, confirmPassword, fullName.
3. `AuthConsoleView` gọi `RegistrationController.register(...)`.
4. `RegistrationController` chuyển tiếp sang `RegistrationService.register(...)`.
5. Service validate input (username/email không trống, email hợp lệ, password ≥ 6 ký tự, confirmPassword khớp, fullName không trống).
6. Service kiểm tra trùng lặp: `UserRepository.findByUsername()`, `UserRepository.findByEmail()`.
7. Service tạo `UserAccount` (role=USER, status=ACTIVE) → `UserRepository.save()`.
8. Service tạo `UserProfile` → `ProfileRepository.save()`.
9. Service ghi log → `ActivityLogService.log()`.
10. Trả `OperationResult.success("Đăng ký thành công!")`.
11. View hiển thị kết quả.

### Alternative Flow

- **3a.** Username đã tồn tại → trả `failure("Username đã tồn tại")`.
- **3b.** Email đã được đăng ký → trả `failure("Email đã được đăng ký")`.

### Exception Flow

- **E1.** Username trống → `failure("Username không được để trống")`.
- **E2.** Email trống/không hợp lệ → `failure("Email không hợp lệ")`.
- **E3.** Password < 6 ký tự → `failure("Mật khẩu phải có ít nhất 6 ký tự")`.
- **E4.** ConfirmPassword không khớp → `failure("Mật khẩu xác nhận không khớp")`.
- **E5.** FullName trống → `failure("Họ tên không được để trống")`.

### Sequence Message Chain

```
Guest → AuthConsoleView: chọn Đăng ký
AuthConsoleView → AuthConsoleView: nhập username, email, password, confirmPassword, fullName
AuthConsoleView → RegistrationController: register(username, email, password, confirmPassword, fullName)
RegistrationController → RegistrationService: register(username, email, password, confirmPassword, fullName)
RegistrationService → InputValidator: isValidEmail(email)
InputValidator → RegistrationService: true/false
RegistrationService → UserRepository: findByUsername(username)
UserRepository → RegistrationService: null / UserAccount
RegistrationService → UserRepository: findByEmail(email)
UserRepository → RegistrationService: null / UserAccount
RegistrationService → PasswordUtil: hash(password)
PasswordUtil → RegistrationService: hashedPassword
RegistrationService → UserRepository: save(UserAccount)
RegistrationService → ProfileRepository: save(UserProfile)
RegistrationService → ActivityLogService: log(userId, "REGISTER", detail)
ActivityLogService → ActivityLogRepository: save(ActivityLog)
RegistrationService → RegistrationController: OperationResult
RegistrationController → AuthConsoleView: OperationResult
AuthConsoleView → Guest: hiển thị kết quả
```

### Activity Diagram

```
[Start]
  ↓
[Nhập username, email, password, confirmPassword, fullName]
  ↓
<Validate input hợp lệ?> ── No ──→ [Hiển thị lỗi] → [End]
  ↓ Yes
<Username đã tồn tại?> ── Yes ──→ [Hiển thị "Username đã tồn tại"] → [End]
  ↓ No
<Email đã tồn tại?> ── Yes ──→ [Hiển thị "Email đã được đăng ký"] → [End]
  ↓ No
[Hash password]
  ↓
[Tạo UserAccount → save]
  ↓
[Tạo UserProfile → save]
  ↓
[Ghi ActivityLog]
  ↓
[Hiển thị "Đăng ký thành công"]
  ↓
[End]
```

---

## UC-5: Upload & Kiểm duyệt tài liệu

### UC-5a: Upload tài liệu (User)

| Mục | Nội dung |
|---|---|
| **Actor** | User |
| **Controller** | `DocumentUploadController` |
| **Service** | `DocumentUploadService`, `ActivityLogService` |
| **Repository** | `DocumentRepository`, `UserRepository`, `ActivityLogRepository` |
| **View** | `DocumentConsoleView` |
| **Method chính** | `DocumentUploadController.uploadDocument(userId, title, description, category, fileName)` → `DocumentUploadService.uploadDocument(...)` |

### Basic Flow

1. User chọn "Upload tài liệu" từ menu.
2. `DocumentConsoleView` hiển thị form nhập: title, description, category, fileName.
3. View gọi `DocumentUploadController.uploadDocument(userId, ...)`.
4. Controller chuyển tiếp sang `DocumentUploadService.uploadDocument(...)`.
5. Service validate user tồn tại, role == USER.
6. Service validate input: title, description, category, fileName không trống.
7. Service kiểm tra định dạng file (pdf/docx/pptx/xlsx/txt).
8. Service tạo `DocumentItem` (status=PENDING_REVIEW) → `DocumentRepository.save()`.
9. Service ghi log → `ActivityLogService.log()`.
10. Trả `OperationResult.success("Upload thành công! Đang chờ kiểm duyệt.")`.

### Alternative Flow

- **5a.** User không phải role USER → `failure("Chỉ User mới được upload")`.

### Exception Flow

- **E1.** Title/Description/Category/FileName trống → `failure(...)`.
- **E2.** File không đúng định dạng → `failure("Chỉ chấp nhận pdf, docx, pptx, xlsx, txt")`.
- **E3.** User không tồn tại → `failure("Người dùng không tồn tại")`.

### Sequence Message Chain

```
User → DocumentConsoleView: chọn Upload tài liệu
DocumentConsoleView → DocumentConsoleView: nhập title, description, category, fileName
DocumentConsoleView → DocumentUploadController: uploadDocument(userId, title, desc, category, fileName)
DocumentUploadController → DocumentUploadService: uploadDocument(userId, title, desc, category, fileName)
DocumentUploadService → UserRepository: findById(userId)
UserRepository → DocumentUploadService: UserAccount
DocumentUploadService → DocumentUploadService: validate input + kiểm tra định dạng file
DocumentUploadService → DocumentRepository: save(DocumentItem)
DocumentUploadService → ActivityLogService: log(userId, "UPLOAD_DOC", detail)
ActivityLogService → ActivityLogRepository: save(ActivityLog)
DocumentUploadService → DocumentUploadController: OperationResult
DocumentUploadController → DocumentConsoleView: OperationResult
DocumentConsoleView → User: hiển thị kết quả
```

### Activity Diagram

```
[Start]
  ↓
[Nhập title, description, category, fileName]
  ↓
<User tồn tại & role = USER?> ── No ──→ [Hiển thị lỗi] → [End]
  ↓ Yes
<Input hợp lệ?> ── No ──→ [Hiển thị lỗi validate] → [End]
  ↓ Yes
<Định dạng file hợp lệ?> ── No ──→ [Hiển thị "File không hợp lệ"] → [End]
  ↓ Yes
[Tạo DocumentItem (PENDING_REVIEW) → save]
  ↓
[Ghi ActivityLog]
  ↓
[Hiển thị "Upload thành công"]
  ↓
[End]
```

---

### UC-5b: Kiểm duyệt tài liệu (Moderator/Admin)

| Mục | Nội dung |
|---|---|
| **Actor** | Moderator (hoặc Admin) |
| **Controller** | `DocumentReviewController` |
| **Service** | `DocumentReviewService`, `ActivityLogService` |
| **Repository** | `DocumentRepository`, `UserRepository`, `ActivityLogRepository` |
| **View** | `DocumentConsoleView` |
| **Method chính** | `approveDocument(moderatorId, documentId)`, `rejectDocument(moderatorId, documentId, reason)` |

### Basic Flow (Approve)

1. Moderator chọn "Kiểm duyệt tài liệu".
2. View gọi `DocumentReviewController.getPendingDocuments()` → hiển thị danh sách PENDING_REVIEW.
3. Moderator chọn tài liệu và chọn "Duyệt".
4. View gọi `DocumentReviewController.approveDocument(moderatorId, documentId)`.
5. Service validate moderator (role = MODERATOR hoặc ADMIN).
6. Service kiểm tra document tồn tại, status = PENDING_REVIEW.
7. Service cập nhật status → APPROVED, gán reviewedBy.
8. Service ghi log.
9. Trả `OperationResult.success("Đã duyệt tài liệu")`.

### Alternative Flow (Reject)

1. Moderator chọn "Từ chối" thay vì "Duyệt".
2. View yêu cầu nhập lý do.
3. View gọi `DocumentReviewController.rejectDocument(moderatorId, documentId, reason)`.
4. Service cập nhật status → REJECTED, gán rejectReason.

### Exception Flow

- **E1.** Không phải Moderator/Admin → `failure("Chỉ Moderator/Admin mới được duyệt")`.
- **E2.** Document không ở PENDING_REVIEW → `failure(...)`.
- **E3.** Reject mà không nhập lý do → `failure("Phải nhập lý do từ chối")`.

### Sequence Message Chain

```
Moderator → DocumentConsoleView: chọn Kiểm duyệt
DocumentConsoleView → DocumentReviewController: getPendingDocuments()
DocumentReviewController → DocumentReviewService: getPendingDocuments()
DocumentReviewService → DocumentRepository: findAll() + filter PENDING_REVIEW
DocumentRepository → DocumentReviewService: List<DocumentItem>
DocumentReviewService → DocumentReviewController: List<DocumentItem>
DocumentReviewController → DocumentConsoleView: danh sách pending
DocumentConsoleView → Moderator: hiển thị danh sách

Moderator → DocumentConsoleView: chọn Approve/Reject

  [Approve]
  DocumentConsoleView → DocumentReviewController: approveDocument(modId, docId)
  DocumentReviewController → DocumentReviewService: approveDocument(modId, docId)
  DocumentReviewService → UserRepository: findById(modId)
  DocumentReviewService → DocumentRepository: findById(docId)
  DocumentReviewService → DocumentReviewService: set status=APPROVED, reviewedBy=modId
  DocumentReviewService → ActivityLogService: log(modId, "APPROVE_DOC", detail)
  DocumentReviewService → DocumentReviewController: OperationResult

  [Reject]
  DocumentConsoleView → DocumentReviewController: rejectDocument(modId, docId, reason)
  DocumentReviewController → DocumentReviewService: rejectDocument(modId, docId, reason)
  DocumentReviewService → DocumentReviewService: set status=REJECTED, rejectReason
  DocumentReviewService → ActivityLogService: log(modId, "REJECT_DOC", detail)
  DocumentReviewService → DocumentReviewController: OperationResult
```

### Activity Diagram

```
[Start]
  ↓
[Lấy danh sách tài liệu PENDING_REVIEW]
  ↓
<Có tài liệu chờ duyệt?> ── No ──→ [Hiển thị "Không có tài liệu"] → [End]
  ↓ Yes
[Hiển thị danh sách, Moderator chọn tài liệu]
  ↓
<Approve hay Reject?>
  ↓ Approve                    ↓ Reject
[Set APPROVED]              [Nhập lý do]
[Set reviewedBy]              ↓
  ↓                        <Lý do trống?> ── Yes → [Lỗi] → [End]
  ↓                           ↓ No
  ↓                        [Set REJECTED + rejectReason]
  ↓←──────────────────────────↓
[Ghi ActivityLog]
  ↓
[Hiển thị kết quả]
  ↓
[End]
```

---

# PHẦN 2 — Huỳnh Duy Tâm (UC-2, UC-3)

---

## UC-2: Đăng nhập / Đăng xuất

### Thông tin chung

| Mục | Nội dung |
|---|---|
| **Actor** | Guest (đăng nhập), User/Moderator/Admin (đăng xuất) |
| **Controller** | `AuthController` |
| **Service** | `AuthService`, `SessionManager`, `ActivityLogService` |
| **Repository** | `UserRepository`, `ActivityLogRepository` |
| **View** | `AuthConsoleView` |
| **Method chính** | `AuthController.login(usernameOrEmail, password)` → `AuthService.login(...)`, `AuthController.logout()` → `AuthService.logout()` |

### Basic Flow — Đăng nhập

1. Guest chọn "Đăng nhập".
2. `AuthConsoleView` nhập usernameOrEmail, password.
3. View gọi `AuthController.login(usernameOrEmail, password)`.
4. Controller chuyển tiếp sang `AuthService.login(...)`.
5. Service validate input không trống.
6. Service tìm user: `UserRepository.findByUsername()` → nếu null → `findByEmail()`.
7. Service kiểm tra `AccountStatus != LOCKED`.
8. Service verify password: `PasswordUtil.verify(password, passwordHash)`.
9. Service tạo session: `SessionManager.login(user)`.
10. Service ghi log → `ActivityLogService.log()`.
11. Trả `OperationResult.success("Đăng nhập thành công!")`.
12. View chuyển sang menu theo role.

### Basic Flow — Đăng xuất

1. User chọn "Đăng xuất".
2. View gọi `AuthController.logout()`.
3. `AuthService.logout()` ghi log, gọi `SessionManager.logout()`.
4. Trả `OperationResult.success("Đã đăng xuất")`.
5. View quay lại menu Guest.

### Alternative Flow

- **6a.** Tài khoản không tồn tại → `failure("Tài khoản không tồn tại")`.

### Exception Flow

- **E1.** Input trống → `failure("Vui lòng nhập username/email")`.
- **E2.** Tài khoản bị khóa → `failure("Tài khoản đã bị khóa")`.
- **E3.** Mật khẩu sai → `failure("Mật khẩu không đúng")`.
- **E4.** Đăng xuất khi chưa đăng nhập → `failure("Chưa có phiên đăng nhập")`.

### Sequence Message Chain — Đăng nhập

```
Guest → AuthConsoleView: chọn Đăng nhập
AuthConsoleView → AuthConsoleView: nhập usernameOrEmail, password
AuthConsoleView → AuthController: login(usernameOrEmail, password)
AuthController → AuthService: login(usernameOrEmail, password)
AuthService → UserRepository: findByUsername(usernameOrEmail)
UserRepository → AuthService: null / UserAccount
  [nếu null]
  AuthService → UserRepository: findByEmail(usernameOrEmail)
  UserRepository → AuthService: null / UserAccount
AuthService → AuthService: kiểm tra status != LOCKED
AuthService → PasswordUtil: verify(password, passwordHash)
PasswordUtil → AuthService: true/false
AuthService → SessionManager: login(user)
AuthService → ActivityLogService: log(userId, "LOGIN", detail)
ActivityLogService → ActivityLogRepository: save(ActivityLog)
AuthService → AuthController: OperationResult
AuthController → AuthConsoleView: OperationResult
AuthConsoleView → Guest: hiển thị kết quả, chuyển menu theo role
```

### Sequence Message Chain — Đăng xuất

```
User → MainMenuView: chọn Đăng xuất
MainMenuView → AuthController: logout()
AuthController → AuthService: logout()
AuthService → SessionManager: getCurrentUser()
SessionManager → AuthService: UserAccount
AuthService → ActivityLogService: log(userId, "LOGOUT", detail)
AuthService → SessionManager: logout()
AuthService → AuthController: OperationResult
AuthController → MainMenuView: OperationResult
MainMenuView → User: hiển thị "Đã đăng xuất", quay về menu Guest
```

### Activity Diagram — Đăng nhập

```
[Start]
  ↓
[Nhập usernameOrEmail, password]
  ↓
<Input trống?> ── Yes ──→ [Lỗi: input trống] → [End]
  ↓ No
[Tìm user bằng username]
  ↓
<Tìm thấy?> ── No ──→ [Tìm bằng email]
  ↓ Yes                     ↓
  ↓                  <Tìm thấy?> ── No → [Lỗi: không tồn tại] → [End]
  ↓←────────────────── Yes ──↓
<Tài khoản bị LOCKED?> ── Yes ──→ [Lỗi: bị khóa] → [End]
  ↓ No
<Password đúng?> ── No ──→ [Lỗi: sai mật khẩu] → [End]
  ↓ Yes
[Tạo session: SessionManager.login(user)]
  ↓
[Ghi ActivityLog]
  ↓
[Hiển thị "Đăng nhập thành công", chuyển menu role]
  ↓
[End]
```

---

## UC-3: Khôi phục mật khẩu

### Thông tin chung

| Mục | Nội dung |
|---|---|
| **Actor** | Guest |
| **Controller** | `PasswordRecoveryController` |
| **Service** | `PasswordRecoveryService`, `OtpService`, `ActivityLogService` |
| **Repository** | `UserRepository`, `OtpRepository`, `ActivityLogRepository` |
| **View** | `AuthConsoleView` |
| **Method chính** | `requestOtp(email)`, `verifyOtp(email, otpCode)`, `resetPassword(email, newPassword, confirmPassword)` |

### Basic Flow

1. Guest chọn "Quên mật khẩu".
2. **Bước 1 — Yêu cầu OTP**: Nhập email → `PasswordRecoveryController.requestOtp(email)`.
3. Service validate email hợp lệ, tìm user bằng email.
4. `OtpService.generateOtp(email)` → tạo mã 6 số, lưu `OtpRepository`, in OTP ra console.
5. Trả `success("OTP đã gửi")`.
6. **Bước 2 — Xác thực OTP**: Nhập OTP → `PasswordRecoveryController.verifyOtp(email, otpCode)`.
7. `OtpService.verifyOtp(email, otpCode)` → kiểm tra mã + hạn 5 phút.
8. Nếu đúng → đặt `verifiedEmail = email`, trả `success`.
9. **Bước 3 — Đặt lại mật khẩu**: Nhập newPassword, confirmPassword → `PasswordRecoveryController.resetPassword(email, newPassword, confirmPassword)`.
10. Service kiểm tra `verifiedEmail == email`, validate password.
11. Cập nhật `user.setPasswordHash(hash(newPassword))`.
12. Xóa `verifiedEmail`, ghi log.
13. Trả `success("Đặt lại mật khẩu thành công!")`.

### Alternative Flow

- **3a.** Email không tồn tại trong hệ thống → `failure("Không tìm thấy tài khoản")`.

### Exception Flow

- **E1.** Email trống/không hợp lệ → `failure(...)`.
- **E2.** OTP sai hoặc hết hạn → `failure("OTP không đúng hoặc đã hết hạn")`.
- **E3.** Chưa verify OTP mà reset password → `failure("Bạn chưa xác thực OTP")`.
- **E4.** Password mới < 6 ký tự → `failure(...)`.
- **E5.** ConfirmPassword không khớp → `failure(...)`.

### Sequence Message Chain

```
Guest → AuthConsoleView: chọn Quên mật khẩu

--- Bước 1: Yêu cầu OTP ---
AuthConsoleView → AuthConsoleView: nhập email
AuthConsoleView → PasswordRecoveryController: requestOtp(email)
PasswordRecoveryController → PasswordRecoveryService: requestOtp(email)
PasswordRecoveryService → InputValidator: isValidEmail(email)
PasswordRecoveryService → UserRepository: findByEmail(email)
UserRepository → PasswordRecoveryService: UserAccount / null
PasswordRecoveryService → OtpService: generateOtp(email)
OtpService → OtpGenerator: generate()
OtpGenerator → OtpService: "123456" (6 số)
OtpService → OtpRepository: save(OtpRequest)
OtpService → PasswordRecoveryService: otpCode
PasswordRecoveryService → Console: in "[MÔ PHỎNG EMAIL] OTP: 123456"
PasswordRecoveryService → ActivityLogService: log(userId, "REQUEST_OTP", detail)
PasswordRecoveryService → PasswordRecoveryController: OperationResult.success
PasswordRecoveryController → AuthConsoleView: OperationResult

--- Bước 2: Xác thực OTP ---
AuthConsoleView → AuthConsoleView: nhập otpCode
AuthConsoleView → PasswordRecoveryController: verifyOtp(email, otpCode)
PasswordRecoveryController → PasswordRecoveryService: verifyOtp(email, otpCode)
PasswordRecoveryService → OtpService: verifyOtp(email, otpCode)
OtpService → OtpRepository: findByEmail(email)
OtpRepository → OtpService: OtpRequest
OtpService → OtpService: kiểm tra code + expiresAt
OtpService → PasswordRecoveryService: true/false
PasswordRecoveryService → PasswordRecoveryService: verifiedEmail = email
PasswordRecoveryService → PasswordRecoveryController: OperationResult
PasswordRecoveryController → AuthConsoleView: OperationResult

--- Bước 3: Đặt lại mật khẩu ---
AuthConsoleView → AuthConsoleView: nhập newPassword, confirmPassword
AuthConsoleView → PasswordRecoveryController: resetPassword(email, newPwd, confirmPwd)
PasswordRecoveryController → PasswordRecoveryService: resetPassword(email, newPwd, confirmPwd)
PasswordRecoveryService → PasswordRecoveryService: kiểm tra verifiedEmail == email
PasswordRecoveryService → UserRepository: findByEmail(email)
PasswordRecoveryService → PasswordUtil: hash(newPassword)
PasswordRecoveryService → UserAccount: setPasswordHash(hashed)
PasswordRecoveryService → PasswordRecoveryService: verifiedEmail = null
PasswordRecoveryService → ActivityLogService: log(userId, "RESET_PASSWORD", detail)
PasswordRecoveryService → PasswordRecoveryController: OperationResult.success
PasswordRecoveryController → AuthConsoleView: OperationResult
AuthConsoleView → Guest: hiển thị "Đặt lại mật khẩu thành công"
```

### Activity Diagram

```
[Start]
  ↓
[Nhập email]
  ↓
<Email hợp lệ?> ── No ──→ [Lỗi email] → [End]
  ↓ Yes
<Tài khoản tồn tại?> ── No ──→ [Lỗi: không tìm thấy] → [End]
  ↓ Yes
[Tạo OTP 6 số, lưu OtpRepository]
  ↓
[In OTP ra console (mô phỏng gửi email)]
  ↓
[Nhập OTP]
  ↓
<OTP đúng & chưa hết hạn?> ── No ──→ [Lỗi OTP] → [End]
  ↓ Yes
[Đánh dấu verifiedEmail]
  ↓
[Nhập newPassword, confirmPassword]
  ↓
<Password ≥ 6 ký tự & khớp?> ── No ──→ [Lỗi password] → [End]
  ↓ Yes
[Hash password mới → cập nhật UserAccount]
  ↓
[Xóa verifiedEmail, ghi ActivityLog]
  ↓
[Hiển thị "Thành công"]
  ↓
[End]
```

---

# PHẦN 3 — Nguyễn Minh Luân (UC-4, UC-8)

---

## UC-4: Quản trị người dùng

### Thông tin chung

| Mục | Nội dung |
|---|---|
| **Actor** | Admin |
| **Controller** | `UserManagementController` |
| **Service** | `UserManagementService`, `ActivityLogService` |
| **Repository** | `UserRepository`, `ActivityLogRepository` |
| **View** | `MainMenuView` |
| **Method chính** | `getAllUsers()`, `searchUsers(keyword)`, `changeUserRole(adminId, targetId, newRole)`, `toggleUserLock(adminId, targetId)` |

### Basic Flow — Xem & Tìm kiếm người dùng

1. Admin chọn "Quản trị người dùng".
2. View gọi `UserManagementController.getAllUsers()` hoặc `searchUsers(keyword)`.
3. Service trả về `List<UserAccount>` (filter theo keyword nếu có).
4. View hiển thị danh sách.

### Basic Flow — Đổi Role

1. Admin chọn user, chọn "Đổi role".
2. View gọi `UserManagementController.changeUserRole(adminId, targetId, newRole)`.
3. Service validate admin role == ADMIN.
4. Service kiểm tra không tự đổi, không đổi Admin khác, newRole chỉ là USER/MODERATOR.
5. Service cập nhật `target.setRole(role)`, ghi log.
6. Trả `success("Đã đổi role")`.

### Basic Flow — Khóa/Mở khóa

1. Admin chọn user, chọn "Khóa/Mở khóa".
2. View gọi `UserManagementController.toggleUserLock(adminId, targetId)`.
3. Service toggle: ACTIVE ↔ LOCKED, ghi log.
4. Trả `success("Đã khóa/mở khóa")`.

### Alternative Flow

- **Đổi role**: Không thể gán ADMIN → `failure`.

### Exception Flow

- **E1.** Tự đổi role/khóa chính mình → `failure`.
- **E2.** Target là Admin → `failure("Không thể thay đổi Admin khác")`.
- **E3.** Role không hợp lệ → `failure`.
- **E4.** Không phải Admin thực hiện → `failure`.

### Sequence Message Chain

```
Admin → MainMenuView: chọn Quản trị người dùng

--- Xem danh sách ---
MainMenuView → UserManagementController: getAllUsers()
UserManagementController → UserManagementService: getAllUsers()
UserManagementService → UserRepository: findAll()
UserRepository → UserManagementService: List<UserAccount>
UserManagementService → UserManagementController: List<UserAccount>
UserManagementController → MainMenuView: List<UserAccount>
MainMenuView → Admin: hiển thị danh sách

--- Đổi Role ---
Admin → MainMenuView: chọn user, chọn Đổi role, nhập newRole
MainMenuView → UserManagementController: changeUserRole(adminId, targetId, newRole)
UserManagementController → UserManagementService: changeUserRole(adminId, targetId, newRole)
UserManagementService → UserRepository: findById(adminId)
UserManagementService → UserRepository: findById(targetId)
UserManagementService → UserManagementService: validate (không tự đổi, không đổi Admin, role hợp lệ)
UserManagementService → UserAccount(target): setRole(newRole)
UserManagementService → ActivityLogService: log(adminId, "CHANGE_ROLE", detail)
UserManagementService → UserManagementController: OperationResult
UserManagementController → MainMenuView: OperationResult

--- Khóa/Mở khóa ---
Admin → MainMenuView: chọn user, chọn Khóa/Mở khóa
MainMenuView → UserManagementController: toggleUserLock(adminId, targetId)
UserManagementController → UserManagementService: toggleUserLock(adminId, targetId)
UserManagementService → UserRepository: findById(adminId)
UserManagementService → UserRepository: findById(targetId)
UserManagementService → UserManagementService: toggle ACTIVE ↔ LOCKED
UserManagementService → ActivityLogService: log(adminId, "LOCK/UNLOCK_USER", detail)
UserManagementService → UserManagementController: OperationResult
UserManagementController → MainMenuView: OperationResult
MainMenuView → Admin: hiển thị kết quả
```

### Activity Diagram — Đổi Role

```
[Start]
  ↓
[Hiển thị danh sách user]
  ↓
[Admin chọn user, nhập newRole]
  ↓
<Admin hợp lệ?> ── No ──→ [Lỗi: không phải Admin] → [End]
  ↓ Yes
<Tự đổi chính mình?> ── Yes ──→ [Lỗi] → [End]
  ↓ No
<Target là Admin?> ── Yes ──→ [Lỗi: không thể đổi Admin] → [End]
  ↓ No
<newRole hợp lệ (USER/MODERATOR)?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
[Cập nhật role]
  ↓
[Ghi ActivityLog]
  ↓
[Hiển thị "Đã đổi role"]
  ↓
[End]
```

### Activity Diagram — Khóa/Mở khóa

```
[Start]
  ↓
[Hiển thị danh sách user]
  ↓
[Admin chọn user]
  ↓
<Admin hợp lệ?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
<Tự khóa chính mình?> ── Yes ──→ [Lỗi] → [End]
  ↓ No
<Target là Admin?> ── Yes ──→ [Lỗi] → [End]
  ↓ No
<Status hiện tại?>
  ↓ ACTIVE               ↓ LOCKED
[Set LOCKED]           [Set ACTIVE]
[Log LOCK_USER]        [Log UNLOCK_USER]
  ↓←───────────────────── ↓
[Hiển thị kết quả]
  ↓
[End]
```

---

## UC-8: Báo cáo / Thống kê & Activity Log

### Thông tin chung

| Mục | Nội dung |
|---|---|
| **Actor** | Admin |
| **Controller** | `ReportController` |
| **Service** | `ReportService`, `ActivityLogService` |
| **Repository** | `UserRepository`, `DocumentRepository`, `CommentRepository`, `RatingRepository`, `ActivityLogRepository` |
| **View** | `MainMenuView` |
| **Method chính** | `ReportController.getOverviewReport()` → `ReportService.getOverviewReport()`, `ActivityLogService.getRecentLogs(limit)` |

### Basic Flow — Xem báo cáo

1. Admin chọn "Xem báo cáo/thống kê".
2. View gọi `ReportController.getOverviewReport()`.
3. `ReportService.getOverviewReport()` tổng hợp dữ liệu từ 4 repository:
   - Tổng user, phân loại theo role (ADMIN/MODERATOR/USER).
   - Tổng document, phân loại theo status (APPROVED/PENDING/REJECTED).
   - Tổng lượt download.
   - Tổng comment, tổng rating.
4. Trả `Map<String, Object>`.
5. View hiển thị bảng thống kê.

### Basic Flow — Xem Activity Log

1. Admin chọn "Xem Activity Log".
2. View gọi `ActivityLogService.getRecentLogs(20)`.
3. Service lấy 20 log gần nhất từ `ActivityLogRepository`.
4. View hiển thị danh sách log.

### Sequence Message Chain

```
Admin → MainMenuView: chọn Xem báo cáo

--- Báo cáo tổng quan ---
MainMenuView → ReportController: getOverviewReport()
ReportController → ReportService: getOverviewReport()
ReportService → UserRepository: findAll()
ReportService → DocumentRepository: findAll()
ReportService → CommentRepository: findAll()
ReportService → RatingRepository: findAll()
ReportService → ReportService: tổng hợp thống kê
ReportService → ReportController: Map<String, Object>
ReportController → MainMenuView: Map<String, Object>
MainMenuView → Admin: hiển thị bảng thống kê

--- Activity Log ---
MainMenuView → ActivityLogService: getRecentLogs(20)
ActivityLogService → ActivityLogRepository: findAll()
ActivityLogRepository → ActivityLogService: List<ActivityLog>
ActivityLogService → ActivityLogService: lấy 20 bản ghi cuối
ActivityLogService → MainMenuView: List<ActivityLog>
MainMenuView → Admin: hiển thị danh sách log
```

### Activity Diagram

```
[Start]
  ↓
<Admin chọn Báo cáo hay Activity Log?>
  ↓ Báo cáo                    ↓ Activity Log
[Query UserRepository]       [Query ActivityLogRepository]
[Query DocumentRepository]   [Lấy N bản ghi cuối]
[Query CommentRepository]      ↓
[Query RatingRepository]     [Hiển thị danh sách log]
  ↓                            ↓
[Tổng hợp thống kê]           ↓
  ↓                            ↓
[Hiển thị bảng thống kê]      ↓
  ↓←───────────────────────────↓
[End]
```

---

# PHẦN 4 — Hồ Nguyễn Quốc Nam (UC-6, UC-7)

---

## UC-6: Tra cứu & Tải xuống tài liệu

### Thông tin chung

| Mục | Nội dung |
|---|---|
| **Actor** | Guest (xem cơ bản), User (xem chi tiết + tải xuống) |
| **Controller** | `DocumentSearchController` |
| **Service** | `DocumentSearchService`, `ActivityLogService` |
| **Repository** | `DocumentRepository`, `ActivityLogRepository` |
| **View** | `DocumentConsoleView` |
| **Method chính** | `listApprovedDocuments()`, `searchDocuments(keyword)`, `getDocumentDetail(docId, isLoggedIn)`, `simulateDownload(docId, userId)` |

### Basic Flow — Tra cứu

1. Actor chọn "Tra cứu tài liệu".
2. View nhập keyword (hoặc để trống xem tất cả).
3. View gọi `DocumentSearchController.searchDocuments(keyword)` hoặc `listApprovedDocuments()`.
4. Service filter tài liệu APPROVED theo keyword (title/description/category).
5. Trả `List<DocumentItem>`.
6. View hiển thị danh sách.

### Basic Flow — Xem chi tiết & Tải xuống

7. Actor chọn tài liệu → View gọi `getDocumentDetail(docId, isLoggedIn)`.
8. Service trả `DocumentItem` (chỉ trả nếu APPROVED).
9. View hiển thị chi tiết.
10. Nếu User đã đăng nhập → chọn "Tải xuống" → `simulateDownload(docId, userId)`.
11. Service tăng `downloadCount`, in mô phỏng download, ghi log.

### Alternative Flow

- **7a.** Document không tồn tại hoặc không APPROVED → trả `null`.

### Exception Flow

- **E1.** Guest cố tải xuống → View không cho phép (kiểm tra isLoggedIn).

### Sequence Message Chain

```
Actor → DocumentConsoleView: chọn Tra cứu tài liệu
DocumentConsoleView → DocumentConsoleView: nhập keyword
DocumentConsoleView → DocumentSearchController: searchDocuments(keyword)
DocumentSearchController → DocumentSearchService: searchApprovedDocuments(keyword)
DocumentSearchService → DocumentRepository: findAll()
DocumentRepository → DocumentSearchService: List<DocumentItem>
DocumentSearchService → DocumentSearchService: filter APPROVED + keyword
DocumentSearchService → DocumentSearchController: List<DocumentItem>
DocumentSearchController → DocumentConsoleView: List<DocumentItem>
DocumentConsoleView → Actor: hiển thị danh sách

Actor → DocumentConsoleView: chọn tài liệu
DocumentConsoleView → DocumentSearchController: getDocumentDetail(docId, isLoggedIn)
DocumentSearchController → DocumentSearchService: getDocumentDetail(docId, isLoggedIn)
DocumentSearchService → DocumentRepository: findById(docId)
DocumentRepository → DocumentSearchService: DocumentItem / null
DocumentSearchService → DocumentSearchController: DocumentItem / null
DocumentSearchController → DocumentConsoleView: DocumentItem
DocumentConsoleView → Actor: hiển thị chi tiết

  [Nếu User đã đăng nhập]
  Actor → DocumentConsoleView: chọn Tải xuống
  DocumentConsoleView → DocumentSearchController: simulateDownload(docId, userId)
  DocumentSearchController → DocumentSearchService: simulateDownload(docId, userId)
  DocumentSearchService → DocumentRepository: findById(docId)
  DocumentSearchService → DocumentItem: incrementDownloadCount()
  DocumentSearchService → Console: in "[MÔ PHỎNG] Đang tải xuống..."
  DocumentSearchService → ActivityLogService: log(userId, "DOWNLOAD_DOC", detail)
```

### Activity Diagram

```
[Start]
  ↓
[Nhập keyword (có thể trống)]
  ↓
[Tìm tài liệu APPROVED theo keyword]
  ↓
<Có kết quả?> ── No ──→ [Hiển thị "Không tìm thấy"] → [End]
  ↓ Yes
[Hiển thị danh sách tài liệu]
  ↓
[Actor chọn tài liệu]
  ↓
<Tài liệu tồn tại & APPROVED?> ── No ──→ [Hiển thị lỗi] → [End]
  ↓ Yes
[Hiển thị chi tiết tài liệu]
  ↓
<Actor đã đăng nhập?> ── No ──→ [Chỉ xem cơ bản] → [End]
  ↓ Yes
<Chọn tải xuống?>  ── No ──→ [End]
  ↓ Yes
[Tăng downloadCount]
  ↓
[Mô phỏng tải xuống]
  ↓
[Ghi ActivityLog]
  ↓
[End]
```

---

## UC-7: Bình luận & Đánh giá tài liệu

### Thông tin chung

| Mục | Nội dung |
|---|---|
| **Actor** | User |
| **Controller** | `DocumentInteractionController` |
| **Service** | `CommentService`, `RatingService`, `ActivityLogService` |
| **Repository** | `CommentRepository`, `RatingRepository`, `DocumentRepository`, `UserRepository`, `ActivityLogRepository` |
| **View** | `DocumentConsoleView` |
| **Method chính** | `addComment(userId, docId, content)`, `editComment(userId, cmtId, newContent)`, `deleteComment(userId, cmtId)`, `rateDocument(userId, docId, score)`, `getCommentsByDocument(docId)`, `getAverageRating(docId)` |

### Basic Flow — Thêm bình luận

1. User xem chi tiết tài liệu APPROVED.
2. Chọn "Bình luận" → nhập nội dung.
3. View gọi `DocumentInteractionController.addComment(userId, docId, content)`.
4. `CommentService.addComment(...)` validate user, document (APPROVED), content (không trống, ≤ 500 ký tự).
5. Tạo `Comment` → `CommentRepository.save()`, ghi log.
6. Trả `success`.

### Basic Flow — Sửa/Xóa bình luận

- Sửa: `editComment(userId, cmtId, newContent)` → kiểm tra quyền sở hữu → cập nhật content + updatedAt.
- Xóa: `deleteComment(userId, cmtId)` → kiểm tra quyền sở hữu → `CommentRepository.delete()`.

### Basic Flow — Đánh giá

1. User chọn "Đánh giá" → nhập điểm 1-5.
2. View gọi `DocumentInteractionController.rateDocument(userId, docId, score)`.
3. `RatingService.rateDocument(...)` kiểm tra đã có rating → cập nhật hoặc tạo mới.
4. Ghi log, trả `success`.

### Exception Flow

- **E1.** Document không APPROVED → `failure`.
- **E2.** Content trống hoặc > 500 ký tự → `failure`.
- **E3.** Score ngoài 1-5 → `failure`.
- **E4.** Sửa/xóa comment của người khác → `failure("Chỉ sửa/xóa bình luận của mình")`.

### Sequence Message Chain — Bình luận

```
User → DocumentConsoleView: chọn Bình luận
DocumentConsoleView → DocumentConsoleView: nhập nội dung
DocumentConsoleView → DocumentInteractionController: addComment(userId, docId, content)
DocumentInteractionController → CommentService: addComment(userId, docId, content)
CommentService → UserRepository: findById(userId)
CommentService → DocumentRepository: findById(docId)
CommentService → CommentService: validate content (không trống, ≤ 500)
CommentService → CommentRepository: save(Comment)
CommentService → ActivityLogService: log(userId, "ADD_COMMENT", detail)
CommentService → DocumentInteractionController: OperationResult
DocumentInteractionController → DocumentConsoleView: OperationResult
DocumentConsoleView → User: hiển thị kết quả
```

### Sequence Message Chain — Đánh giá

```
User → DocumentConsoleView: chọn Đánh giá, nhập score
DocumentConsoleView → DocumentInteractionController: rateDocument(userId, docId, score)
DocumentInteractionController → RatingService: rateDocument(userId, docId, score)
RatingService → UserRepository: findById(userId)
RatingService → DocumentRepository: findById(docId)
RatingService → RatingService: validate score 1-5, document APPROVED
RatingService → RatingRepository: findByUserAndDocument(userId, docId)
  [nếu đã tồn tại]
  RatingService → Rating(existing): setScore(score)
  [nếu chưa tồn tại]
  RatingService → RatingRepository: save(new Rating)
RatingService → ActivityLogService: log(userId, "RATE_DOC", detail)
RatingService → DocumentInteractionController: OperationResult
DocumentInteractionController → DocumentConsoleView: OperationResult
DocumentConsoleView → User: hiển thị kết quả
```

### Activity Diagram — Bình luận

```
[Start]
  ↓
[User xem chi tiết tài liệu APPROVED]
  ↓
[Nhập nội dung bình luận]
  ↓
<User tồn tại?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
<Document APPROVED?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
<Content hợp lệ (không trống, ≤ 500)?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
[Tạo Comment → save]
  ↓
[Ghi ActivityLog]
  ↓
[Hiển thị "Đã thêm bình luận"]
  ↓
[End]
```

### Activity Diagram — Đánh giá

```
[Start]
  ↓
[User chọn Đánh giá, nhập score]
  ↓
<User & Document hợp lệ?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
<Document APPROVED?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
<Score trong 1-5?> ── No ──→ [Lỗi: điểm phải 1-5] → [End]
  ↓ Yes
<Đã đánh giá trước đó?>
  ↓ Yes                    ↓ No
[Cập nhật score]        [Tạo Rating mới → save]
  ↓←────────────────────── ↓
[Ghi ActivityLog]
  ↓
[Hiển thị kết quả]
  ↓
[End]
```

---

# PHẦN 5 — Tạ Văn Huy (UC-9, UC-10)

---

## UC-9: Hồ sơ & Thư viện cá nhân

### Thông tin chung

| Mục | Nội dung |
|---|---|
| **Actor** | User |
| **Controller** | `PersonalLibraryController` |
| **Service** | `PersonalLibraryService`, `ActivityLogService` |
| **Repository** | `ProfileRepository`, `DocumentRepository`, `PersonalLibraryRepository`, `ActivityLogRepository` |
| **View** | `MainMenuView` |
| **Method chính** | `getProfile(userId)`, `updateProfile(userId, fullName, bio)`, `getUploadedDocuments(userId)`, `saveToLibrary(userId, docId)`, `getSavedDocuments(userId)`, `createCollection(userId, name, visibility)`, `getCollections(userId)`, `addToCollection(userId, collId, docId)` |

### Basic Flow — Xem/Cập nhật hồ sơ

1. User chọn "Hồ sơ cá nhân".
2. View gọi `PersonalLibraryController.getProfile(userId)` → hiển thị fullName, bio.
3. User chọn "Cập nhật" → nhập fullName, bio.
4. View gọi `PersonalLibraryController.updateProfile(userId, fullName, bio)`.
5. Service validate fullName không trống → cập nhật → ghi log.

### Basic Flow — Thư viện cá nhân

1. User chọn "Thư viện cá nhân".
2. **Xem tài liệu đã upload**: `getUploadedDocuments(userId)`.
3. **Lưu tài liệu**: `saveToLibrary(userId, docId)` → kiểm tra APPROVED, chưa có trong thư viện → thêm vào savedDocumentIds.
4. **Xem tài liệu đã lưu**: `getSavedDocuments(userId)`.
5. **Tạo collection**: `createCollection(userId, name, visibility)` → visibility = PRIVATE/SHARED.
6. **Thêm tài liệu vào collection**: `addToCollection(userId, collId, docId)`.

### Exception Flow

- **E1.** FullName trống khi update → `failure`.
- **E2.** Document không APPROVED khi save → `failure`.
- **E3.** Document đã có trong thư viện → `failure("Tài liệu đã có")`.
- **E4.** Visibility không phải PRIVATE/SHARED → `failure`.
- **E5.** Collection không thuộc về user → `failure`.
- **E6.** Document đã có trong collection → `failure`.

### Sequence Message Chain — Cập nhật hồ sơ

```
User → MainMenuView: chọn Hồ sơ cá nhân
MainMenuView → PersonalLibraryController: getProfile(userId)
PersonalLibraryController → PersonalLibraryService: getProfile(userId)
PersonalLibraryService → ProfileRepository: findByUserId(userId)
ProfileRepository → PersonalLibraryService: UserProfile
PersonalLibraryService → PersonalLibraryController: UserProfile
PersonalLibraryController → MainMenuView: UserProfile
MainMenuView → User: hiển thị hồ sơ

User → MainMenuView: chọn Cập nhật, nhập fullName, bio
MainMenuView → PersonalLibraryController: updateProfile(userId, fullName, bio)
PersonalLibraryController → PersonalLibraryService: updateProfile(userId, fullName, bio)
PersonalLibraryService → ProfileRepository: findByUserId(userId)
PersonalLibraryService → UserProfile: setFullName(), setBio()
PersonalLibraryService → ActivityLogService: log(userId, "UPDATE_PROFILE", detail)
PersonalLibraryService → PersonalLibraryController: OperationResult
PersonalLibraryController → MainMenuView: OperationResult
MainMenuView → User: hiển thị kết quả
```

### Sequence Message Chain — Lưu tài liệu vào thư viện

```
User → MainMenuView: chọn Lưu tài liệu, nhập docId
MainMenuView → PersonalLibraryController: saveToLibrary(userId, docId)
PersonalLibraryController → PersonalLibraryService: saveToLibrary(userId, docId)
PersonalLibraryService → DocumentRepository: findById(docId)
DocumentRepository → PersonalLibraryService: DocumentItem / null
PersonalLibraryService → PersonalLibraryService: kiểm tra APPROVED
PersonalLibraryService → PersonalLibraryRepository: findByUserId(userId)
  [nếu null → tạo mới PersonalLibrary → save]
PersonalLibraryService → PersonalLibraryService: kiểm tra trùng lặp
PersonalLibraryService → PersonalLibrary: getSavedDocumentIds().add(docId)
PersonalLibraryService → ActivityLogService: log(userId, "SAVE_DOC", detail)
PersonalLibraryService → PersonalLibraryController: OperationResult
PersonalLibraryController → MainMenuView: OperationResult
MainMenuView → User: hiển thị kết quả
```

### Sequence Message Chain — Tạo Collection

```
User → MainMenuView: nhập name, visibility
MainMenuView → PersonalLibraryController: createCollection(userId, name, visibility)
PersonalLibraryController → PersonalLibraryService: createCollection(userId, name, visibility)
PersonalLibraryService → PersonalLibraryService: validate name, visibility (PRIVATE/SHARED)
PersonalLibraryService → PersonalLibraryRepository: findByUserId(userId)
  [nếu null → tạo mới PersonalLibrary → save]
PersonalLibraryService → PersonalLibrary: getCollections().add(new Collection)
PersonalLibraryService → ActivityLogService: log(userId, "CREATE_COLLECTION", detail)
PersonalLibraryService → PersonalLibraryController: OperationResult
PersonalLibraryController → MainMenuView: OperationResult
```

### Activity Diagram — Lưu tài liệu

```
[Start]
  ↓
[User nhập docId]
  ↓
<Tài liệu tồn tại?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
<Tài liệu APPROVED?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
[Lấy PersonalLibrary (tạo mới nếu chưa có)]
  ↓
<Đã có trong thư viện?> ── Yes ──→ [Lỗi: đã lưu rồi] → [End]
  ↓ No
[Thêm docId vào savedDocumentIds]
  ↓
[Ghi ActivityLog]
  ↓
[Hiển thị "Đã lưu"]
  ↓
[End]
```

### Activity Diagram — Tạo Collection

```
[Start]
  ↓
[Nhập name, visibility]
  ↓
<Name trống?> ── Yes ──→ [Lỗi] → [End]
  ↓ No
<Visibility hợp lệ (PRIVATE/SHARED)?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
[Lấy/Tạo PersonalLibrary]
  ↓
[Tạo Collection → thêm vào collections]
  ↓
[Ghi ActivityLog]
  ↓
[Hiển thị "Đã tạo collection"]
  ↓
[End]
```

---

## UC-10: Forum yêu cầu tài liệu & Kiểm soát nội dung

### UC-10a: Forum yêu cầu tài liệu

| Mục | Nội dung |
|---|---|
| **Actor** | User (tạo/bình luận bài), Admin (duyệt bài) |
| **Controller** | `RequestPostController` |
| **Service** | `RequestPostService`, `ActivityLogService` |
| **Repository** | `RequestPostRepository`, `UserRepository`, `CommentRepository`, `ActivityLogRepository` |
| **View** | `MainMenuView` |
| **Method chính (User)** | `createRequestPost(userId, title, desc, category)`, `getMyRequestPosts(userId)`, `markFulfilled(userId, postId)`, `getOpenRequestPosts()`, `addPostComment(userId, postId, content)`, `editPostComment(...)`, `deletePostComment(...)` |
| **Method chính (Admin)** | `getPendingRequestPosts()`, `approveRequestPost(adminId, postId)`, `rejectRequestPost(adminId, postId, reason)` |

### Basic Flow — User tạo bài yêu cầu

1. User chọn "Tạo bài yêu cầu tài liệu".
2. Nhập title, description, category.
3. View gọi `RequestPostController.createRequestPost(userId, title, desc, category)`.
4. Service validate user (role=USER), validate input.
5. Tạo `RequestPost` (status=PENDING_APPROVAL) → `RequestPostRepository.save()`.
6. Ghi log, trả `success`.

### Basic Flow — Admin duyệt bài

1. Admin chọn "Duyệt bài yêu cầu".
2. View gọi `getPendingRequestPosts()` → hiển thị danh sách PENDING_APPROVAL.
3. Admin chọn Approve → `approveRequestPost(adminId, postId)` → set OPEN.
4. Hoặc Reject → `rejectRequestPost(adminId, postId, reason)` → set REJECTED + reason.

### Basic Flow — User bình luận bài OPEN

1. User xem bài OPEN → chọn bình luận → nhập nội dung.
2. `addPostComment(userId, postId, content)` → validate bài OPEN, content ≤ 500.
3. Tạo Comment (documentId = "POST:" + postId) → save.

### Basic Flow — User đánh dấu hoàn thành

1. User chọn bài của mình (status=OPEN) → markFulfilled.
2. Service kiểm tra quyền sở hữu + status OPEN → set FULFILLED.

### Exception Flow

- **E1.** Không phải User tạo bài → `failure`.
- **E2.** Input trống → `failure`.
- **E3.** Bình luận bài không OPEN → `failure`.
- **E4.** Admin không hợp lệ → `failure`.
- **E5.** Reject không nhập lý do → `failure`.
- **E6.** markFulfilled bài không phải của mình hoặc không OPEN → `failure`.

### Sequence Message Chain — Tạo bài

```
User → MainMenuView: chọn Tạo bài yêu cầu
MainMenuView → MainMenuView: nhập title, description, category
MainMenuView → RequestPostController: createRequestPost(userId, title, desc, category)
RequestPostController → RequestPostService: createPost(userId, title, desc, category)
RequestPostService → UserRepository: findById(userId)
RequestPostService → RequestPostService: validate input
RequestPostService → RequestPostRepository: save(RequestPost)
RequestPostService → ActivityLogService: log(userId, "CREATE_REQUEST", detail)
RequestPostService → RequestPostController: OperationResult
RequestPostController → MainMenuView: OperationResult
MainMenuView → User: hiển thị "Đã tạo, chờ Admin duyệt"
```

### Sequence Message Chain — Admin duyệt bài

```
Admin → MainMenuView: chọn Duyệt bài yêu cầu
MainMenuView → RequestPostController: getPendingRequestPosts()
RequestPostController → RequestPostService: getPendingPosts()
RequestPostService → RequestPostRepository: findAll() + filter PENDING_APPROVAL
RequestPostService → RequestPostController: List<RequestPost>
RequestPostController → MainMenuView: List<RequestPost>
MainMenuView → Admin: hiển thị danh sách

Admin → MainMenuView: chọn Approve
MainMenuView → RequestPostController: approveRequestPost(adminId, postId)
RequestPostController → RequestPostService: approvePost(adminId, postId)
RequestPostService → UserRepository: findById(adminId)
RequestPostService → RequestPostRepository: findById(postId)
RequestPostService → RequestPost: setStatus(OPEN)
RequestPostService → ActivityLogService: log(adminId, "APPROVE_REQUEST", detail)
RequestPostService → RequestPostController: OperationResult
RequestPostController → MainMenuView: OperationResult
MainMenuView → Admin: hiển thị "Đã duyệt"
```

### Activity Diagram — Tạo bài yêu cầu

```
[Start]
  ↓
[Nhập title, description, category]
  ↓
<User hợp lệ (role=USER)?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
<Input hợp lệ?> ── No ──→ [Lỗi validate] → [End]
  ↓ Yes
[Tạo RequestPost (PENDING_APPROVAL) → save]
  ↓
[Ghi ActivityLog]
  ↓
[Hiển thị "Chờ Admin duyệt"]
  ↓
[End]
```

### Activity Diagram — Admin duyệt bài

```
[Start]
  ↓
[Lấy danh sách PENDING_APPROVAL]
  ↓
<Có bài chờ duyệt?> ── No ──→ [Hiển thị "Không có"] → [End]
  ↓ Yes
[Hiển thị danh sách, Admin chọn bài]
  ↓
<Approve hay Reject?>
  ↓ Approve                ↓ Reject
[Set OPEN]              [Nhập lý do]
  ↓                        ↓
  ↓                     <Lý do trống?> ── Yes → [Lỗi] → [End]
  ↓                        ↓ No
  ↓                     [Set REJECTED + rejectReason]
  ↓←────────────────────── ↓
[Ghi ActivityLog]
  ↓
[Hiển thị kết quả]
  ↓
[End]
```

---

### UC-10b: Kiểm soát nội dung (Moderator)

| Mục | Nội dung |
|---|---|
| **Actor** | Moderator |
| **Controller** | `ForumModerationController` |
| **Service** | `ForumModerationService`, `ActivityLogService` |
| **Repository** | `RequestPostRepository`, `CommentRepository`, `UserRepository`, `ActivityLogRepository` |
| **View** | `MainMenuView` |
| **Method chính** | `getAllPosts()`, `getPostComments(postId)`, `hidePost(modId, postId, reason)`, `deletePost(modId, postId, reason)`, `hideComment(modId, cmtId, reason)`, `deleteComment(modId, cmtId, reason)` |

### Basic Flow

1. Moderator chọn "Kiểm soát nội dung forum".
2. View gọi `ForumModerationController.getAllPosts()` → hiển thị bài không bị HIDDEN/DELETED.
3. Moderator chọn bài → xem comment: `getPostComments(postId)`.
4. Moderator có thể:
   - **Ẩn bài**: `hidePost(modId, postId, reason)` → set HIDDEN.
   - **Xóa bài**: `deletePost(modId, postId, reason)` → set DELETED.
   - **Ẩn comment**: `hideComment(modId, cmtId, reason)` → set hidden=true.
   - **Xóa comment**: `deleteComment(modId, cmtId, reason)` → `CommentRepository.delete()`.

### Exception Flow

- **E1.** Không phải Moderator → `failure`.
- **E2.** Lý do trống → `failure("Phải nhập lý do")`.
- **E3.** Bài/comment không tồn tại → `failure`.

### Sequence Message Chain

```
Moderator → MainMenuView: chọn Kiểm soát nội dung
MainMenuView → ForumModerationController: getAllPosts()
ForumModerationController → ForumModerationService: getAllVisiblePosts()
ForumModerationService → RequestPostRepository: findAll()
ForumModerationService → ForumModerationService: filter != HIDDEN, != DELETED
ForumModerationService → ForumModerationController: List<RequestPost>
ForumModerationController → MainMenuView: List<RequestPost>
MainMenuView → Moderator: hiển thị danh sách bài

  [Ẩn bài]
  Moderator → MainMenuView: chọn Ẩn bài, nhập reason
  MainMenuView → ForumModerationController: hidePost(modId, postId, reason)
  ForumModerationController → ForumModerationService: hidePost(modId, postId, reason)
  ForumModerationService → UserRepository: findById(modId) → validate MODERATOR
  ForumModerationService → RequestPostRepository: findById(postId)
  ForumModerationService → RequestPost: setStatus(HIDDEN)
  ForumModerationService → ActivityLogService: log(modId, "HIDE_POST", detail)
  ForumModerationService → ForumModerationController: OperationResult

  [Xóa comment]
  Moderator → MainMenuView: chọn Xóa comment, nhập reason
  MainMenuView → ForumModerationController: deleteComment(modId, cmtId, reason)
  ForumModerationController → ForumModerationService: deleteComment(modId, cmtId, reason)
  ForumModerationService → UserRepository: findById(modId) → validate MODERATOR
  ForumModerationService → CommentRepository: findById(cmtId)
  ForumModerationService → CommentRepository: delete(cmtId)
  ForumModerationService → ActivityLogService: log(modId, "DELETE_COMMENT_MOD", detail)
  ForumModerationService → ForumModerationController: OperationResult
```

### Activity Diagram — Kiểm soát nội dung

```
[Start]
  ↓
[Lấy danh sách bài visible]
  ↓
[Hiển thị danh sách, Moderator chọn bài/comment]
  ↓
<Hành động?>
  ↓ Ẩn bài      ↓ Xóa bài      ↓ Ẩn comment     ↓ Xóa comment
[Nhập reason] [Nhập reason]   [Nhập reason]    [Nhập reason]
  ↓               ↓               ↓                ↓
<Reason trống?> ── Yes ──→ [Lỗi] → [End]
  ↓ No
<Moderator hợp lệ?> ── No ──→ [Lỗi] → [End]
  ↓ Yes
[Thực thi hành động (set HIDDEN/DELETED hoặc delete)]
  ↓
[Ghi ActivityLog]
  ↓
[Hiển thị kết quả]
  ↓
[End]
```

---

# TỔNG HỢP PHÂN CÔNG

## Nguyễn Xuân Đại

| UC | Diagram cần vẽ | Class chính |
|---|---|---|
| UC-1 | Use Case, Activity, Sequence | `RegistrationController`, `RegistrationService`, `UserRepository`, `ProfileRepository` |
| UC-5a | Use Case, Activity, Sequence | `DocumentUploadController`, `DocumentUploadService`, `DocumentRepository` |
| UC-5b | Use Case, Activity, Sequence | `DocumentReviewController`, `DocumentReviewService`, `DocumentRepository` |

## Huỳnh Duy Tâm

| UC | Diagram cần vẽ | Class chính |
|---|---|---|
| UC-2 (login) | Use Case, Activity, Sequence | `AuthController`, `AuthService`, `SessionManager`, `UserRepository` |
| UC-2 (logout) | Use Case, Activity, Sequence | `AuthController`, `AuthService`, `SessionManager` |
| UC-3 | Use Case, Activity, Sequence | `PasswordRecoveryController`, `PasswordRecoveryService`, `OtpService`, `OtpRepository`, `UserRepository` |

## Nguyễn Minh Luân

| UC | Diagram cần vẽ | Class chính |
|---|---|---|
| UC-4 (đổi role) | Use Case, Activity, Sequence | `UserManagementController`, `UserManagementService`, `UserRepository`, `ActivityLogService` |
| UC-4 (khóa/mở) | Use Case, Activity, Sequence | `UserManagementController`, `UserManagementService`, `UserRepository`, `ActivityLogService` |
| UC-8 | Use Case, Activity, Sequence | `ReportController`, `ReportService`, `ActivityLogService`, nhiều Repository |

## Hồ Nguyễn Quốc Nam

| UC | Diagram cần vẽ | Class chính |
|---|---|---|
| UC-6 | Use Case, Activity, Sequence | `DocumentSearchController`, `DocumentSearchService`, `DocumentRepository`, `ActivityLogService` |
| UC-7 (comment) | Use Case, Activity, Sequence | `DocumentInteractionController`, `CommentService`, `CommentRepository` |
| UC-7 (rating) | Use Case, Activity, Sequence | `DocumentInteractionController`, `RatingService`, `RatingRepository` |

## Tạ Văn Huy

| UC | Diagram cần vẽ | Class chính |
|---|---|---|
| UC-9 (profile) | Use Case, Activity, Sequence | `PersonalLibraryController`, `PersonalLibraryService`, `ProfileRepository` |
| UC-9 (library) | Use Case, Activity, Sequence | `PersonalLibraryController`, `PersonalLibraryService`, `PersonalLibraryRepository`, `DocumentRepository` |
| UC-10a (forum) | Use Case, Activity, Sequence | `RequestPostController`, `RequestPostService`, `RequestPostRepository`, `CommentRepository` |
| UC-10b (moderation) | Use Case, Activity, Sequence | `ForumModerationController`, `ForumModerationService`, `RequestPostRepository`, `CommentRepository` |

---

# GHI CHÚ CHUNG

1. **Mọi Use Case** đều có `ActivityLogService` ghi log hành động. Khi vẽ Sequence Diagram, nên bao gồm lifeline `ActivityLogService` ở cuối chuỗi message.
2. **SessionManager** xuất hiện trong UC-2 (đăng nhập/đăng xuất). Các UC khác truy cập user hiện tại qua `AuthController.getCurrentUser()`.
3. **OperationResult** là kiểu trả về chung cho tất cả method có logic nghiệp vụ. Luôn kiểm tra `isSuccess()` để quyết định hiển thị thông báo thành công hay lỗi.
4. **View** (ConsoleView / MainMenuView) đóng vai trò boundary — nhận input từ Actor, gọi Controller, hiển thị kết quả.
5. **Controller** là lớp điều phối mỏng — chỉ forward request sang Service, không chứa logic.
6. **Service** chứa toàn bộ business rules, validation và gọi Repository.
7. **Repository** là lớp truy cập dữ liệu in-memory bằng `List<>` / `Map<>`.
