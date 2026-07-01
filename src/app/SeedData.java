package app;

import model.ActivityLog;
import model.Comment;
import model.DocumentItem;
import model.Rating;
import model.RequestPost;
import model.UserAccount;
import model.UserProfile;
import model.enums.AccountStatus;
import model.enums.DocumentStatus;
import model.enums.RequestStatus;
import model.enums.Role;
import util.IdGenerator;
import util.PasswordUtil;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Seed data
 * PURPOSE: Sinh dữ liệu mẫu in-memory cho phần 1 gồm user, document, request post, comment, rating và activity log.
 */
public class SeedData {
    private SeedData() {
    }

    public static void load(AppContext context) {
        UserAccount admin = new UserAccount("USR-ADMIN", "admin", "admin@demo.edu.vn", PasswordUtil.hash("admin123"), Role.ADMIN, AccountStatus.ACTIVE);
        UserAccount moderator = new UserAccount("USR-MOD", "moderator", "moderator@demo.edu.vn", PasswordUtil.hash("moderator123"), Role.MODERATOR, AccountStatus.ACTIVE);
        UserAccount user1 = new UserAccount("USR-U1", "user1", "user1@demo.edu.vn", PasswordUtil.hash("user12345"), Role.USER, AccountStatus.ACTIVE);
        UserAccount user2 = new UserAccount("USR-U2", "user2", "user2@demo.edu.vn", PasswordUtil.hash("user12345"), Role.USER, AccountStatus.ACTIVE);
        context.userRepository.save(admin);
        context.userRepository.save(moderator);
        context.userRepository.save(user1);
        context.userRepository.save(user2);

        context.profileRepository.save(new UserProfile("PRO-ADMIN", admin.id, "Admin hệ thống", "Quản trị viên mẫu"));
        context.profileRepository.save(new UserProfile("PRO-MOD", moderator.id, "Moderator nội dung", "Kiểm duyệt tài liệu và forum"));
        context.profileRepository.save(new UserProfile("PRO-U1", user1.id, "Người dùng Một", "Sinh viên chia sẻ tài liệu"));
        context.profileRepository.save(new UserProfile("PRO-U2", user2.id, "Người dùng Hai", "Sinh viên tìm tài liệu học tập"));

        DocumentItem doc1 = new DocumentItem("DOC-APP-1", "Giáo trình Phân tích thiết kế HTTT", "Tổng quan UML và quy trình phân tích", "Công nghệ thông tin", "Khoa CNTT", Arrays.asList("uml", "use case", "activity"), 2025, "pttkhttt.pdf", user1.id, DocumentStatus.APPROVED);
        doc1.approvedBy = moderator.id;
        doc1.approvedAt = LocalDateTime.now().minusDays(5);
        doc1.downloadCount = 12;

        DocumentItem doc2 = new DocumentItem("DOC-APP-2", "Mẫu đặc tả Use Case", "Tài liệu hướng dẫn viết use case chi tiết", "Phân tích hệ thống", "Nhóm 17", Arrays.asList("use case", "template"), 2026, "use-case-template.docx", user2.id, DocumentStatus.APPROVED);
        doc2.approvedBy = moderator.id;
        doc2.approvedAt = LocalDateTime.now().minusDays(2);
        doc2.downloadCount = 7;

        DocumentItem doc3 = new DocumentItem("DOC-PEN-1", "Slide Sequence Diagram", "Slide đang chờ kiểm duyệt", "UML", "Nguyễn Xuân Đại", Arrays.asList("sequence", "uml"), 2026, "sequence.pptx", user1.id, DocumentStatus.PENDING_REVIEW);

        DocumentItem doc4 = new DocumentItem("DOC-REJ-1", "Tài liệu thiếu nguồn", "Bị từ chối do metadata chưa rõ", "Khác", "Không rõ", Arrays.asList("draft"), 2024, "draft.txt", user2.id, DocumentStatus.REJECTED);
        doc4.rejectionReason = "Thiếu nguồn và mô tả chưa đủ rõ.";

        context.documentRepository.save(doc1);
        context.documentRepository.save(doc2);
        context.documentRepository.save(doc3);
        context.documentRepository.save(doc4);

        RequestPost req1 = new RequestPost("REQ-PEN-1", "Cần tài liệu Class Diagram", "Ai có tài liệu class diagram cho hệ thống thư viện không?", user1.id, RequestStatus.PENDING_REVIEW);
        RequestPost req2 = new RequestPost("REQ-OPEN-1", "Xin mẫu Activity Diagram", "Cần mẫu activity diagram cho đăng nhập.", user2.id, RequestStatus.OPEN);
        RequestPost req3 = new RequestPost("REQ-REJ-1", "Bài yêu cầu spam", "Nội dung không phù hợp.", user2.id, RequestStatus.REJECTED);
        req3.rejectionReason = "Nội dung không đúng mục đích forum yêu cầu tài liệu.";
        context.requestPostRepository.save(req1);
        context.requestPostRepository.save(req2);
        context.requestPostRepository.save(req3);

        context.commentRepository.save(new Comment("COM-1", user1.id, "DOCUMENT", doc1.id, "Tài liệu rất hữu ích cho phần Use Case."));
        context.commentRepository.save(new Comment("COM-2", user2.id, "REQUEST", req2.id, "Mình cũng đang cần tài liệu này."));
        context.ratingRepository.save(new Rating(IdGenerator.nextId("RAT"), doc1.id, user1.id, 5));
        context.ratingRepository.save(new Rating(IdGenerator.nextId("RAT"), doc1.id, user2.id, 4));
        context.activityLogRepository.save(new ActivityLog(IdGenerator.nextId("LOG"), "SYSTEM", "SEED_DATA", "APP", "CONSOLE", "Khởi tạo dữ liệu mẫu cho phần 1."));
    }
}