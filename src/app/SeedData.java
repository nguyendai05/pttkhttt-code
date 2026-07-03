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
        context.getUserRepository().save(admin);
        context.getUserRepository().save(moderator);
        context.getUserRepository().save(user1);
        context.getUserRepository().save(user2);

        context.getProfileRepository().save(new UserProfile("PRO-ADMIN", admin.getId(), "Admin hệ thống", "Quản trị viên mẫu"));
        context.getProfileRepository().save(new UserProfile("PRO-MOD", moderator.getId(), "Moderator nội dung", "Kiểm duyệt tài liệu và forum"));
        context.getProfileRepository().save(new UserProfile("PRO-U1", user1.getId(), "Người dùng Một", "Sinh viên chia sẻ tài liệu"));
        context.getProfileRepository().save(new UserProfile("PRO-U2", user2.getId(), "Người dùng Hai", "Sinh viên tìm tài liệu học tập"));

        DocumentItem doc1 = new DocumentItem("DOC-APP-1", "Giáo trình Phân tích thiết kế HTTT", "Tổng quan UML và quy trình phân tích", "Công nghệ thông tin", "Khoa CNTT", Arrays.asList("uml", "use case", "activity"), 2025, "pttkhttt.pdf", user1.getId(), DocumentStatus.APPROVED);
        doc1.setApprovedBy(moderator.getId());
        doc1.setApprovedAt(LocalDateTime.now().minusDays(5));
        doc1.setDownloadCount(12);

        DocumentItem doc2 = new DocumentItem("DOC-APP-2", "Mẫu đặc tả Use Case", "Tài liệu hướng dẫn viết use case chi tiết", "Phân tích hệ thống", "Nhóm 17", Arrays.asList("use case", "template"), 2026, "use-case-template.docx", user2.getId(), DocumentStatus.APPROVED);
        doc2.setApprovedBy(moderator.getId());
        doc2.setApprovedAt(LocalDateTime.now().minusDays(2));
        doc2.setDownloadCount(7);

        DocumentItem doc3 = new DocumentItem("DOC-PEN-1", "Slide Sequence Diagram", "Slide đang chờ kiểm duyệt", "UML", "Nguyễn Xuân Đại", Arrays.asList("sequence", "uml"), 2026, "sequence.pptx", user1.getId(), DocumentStatus.PENDING_REVIEW);

        DocumentItem doc4 = new DocumentItem("DOC-REJ-1", "Tài liệu thiếu nguồn", "Bị từ chối do metadata chưa rõ", "Khác", "Không rõ", Arrays.asList("draft"), 2024, "draft.txt", user2.getId(), DocumentStatus.REJECTED);
        doc4.setRejectionReason("Thiếu nguồn và mô tả chưa đủ rõ.");

        context.getDocumentRepository().save(doc1);
        context.getDocumentRepository().save(doc2);
        context.getDocumentRepository().save(doc3);
        context.getDocumentRepository().save(doc4);

        RequestPost req1 = new RequestPost("REQ-PEN-1", "Cần tài liệu Class Diagram", "Ai có tài liệu class diagram cho hệ thống thư viện không?", user1.getId(), RequestStatus.PENDING_REVIEW);
        RequestPost req2 = new RequestPost("REQ-OPEN-1", "Xin mẫu Activity Diagram", "Cần mẫu activity diagram cho đăng nhập.", user2.getId(), RequestStatus.OPEN);
        RequestPost req3 = new RequestPost("REQ-REJ-1", "Bài yêu cầu spam", "Nội dung không phù hợp.", user2.getId(), RequestStatus.REJECTED);
        req3.setRejectionReason("Nội dung không đúng mục đích forum yêu cầu tài liệu.");
        context.getRequestPostRepository().save(req1);
        context.getRequestPostRepository().save(req2);
        context.getRequestPostRepository().save(req3);

        context.getCommentRepository().save(new Comment("COM-1", user1.getId(), "DOCUMENT", doc1.getId(), "Tài liệu rất hữu ích cho phần Use Case."));
        context.getCommentRepository().save(new Comment("COM-2", user2.getId(), "REQUEST", req2.getId(), "Mình cũng đang cần tài liệu này."));
        context.getRatingRepository().save(new Rating(IdGenerator.nextId("RAT"), doc1.getId(), user1.getId(), 5));
        context.getRatingRepository().save(new Rating(IdGenerator.nextId("RAT"), doc1.getId(), user2.getId(), 4));
        context.getActivityLogRepository().save(new ActivityLog(IdGenerator.nextId("LOG"), "SYSTEM", "SEED_DATA", "APP", "CONSOLE", "Khởi tạo dữ liệu mẫu cho phần 1."));
    }
}