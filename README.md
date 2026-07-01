# Hệ thống quản lý, chia sẻ tài liệu - Java Console MVC

Đây là prototype Java Console theo mô hình MVC phục vụ môn Phân tích và Thiết kế Hệ thống thông tin. Code dùng để mô phỏng nghiệp vụ, hỗ trợ vẽ Use Case Diagram, Activity Diagram và Sequence Diagram.

Prototype này không dùng GUI, không dùng database thật, không upload file thật, không gửi email thật và không dùng thư viện ngoài.

## Cấu trúc project

- `src/app`: khởi động chương trình, wiring dependency, seed data.
- `src/model`: entity dùng chung.
- `src/model/enums`: enum role và trạng thái.
- `src/repository`: repository in-memory bằng `List`/`Map`.
- `src/service`: xử lý business rules chính.
- `src/controller`: nhận request từ view và gọi service.
- `src/view`: menu console, nhập dữ liệu và in kết quả.
- `src/util`: tiện ích tạo id, validate input, hash password giả lập, OTP, format thời gian.

## Cách chạy Main.java

Yêu cầu JDK 8 trở lên.

```powershell
chcp 65001
New-Item -ItemType Directory -Force out
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out app.Main
```

Nếu terminal vẫn hiển thị sai dấu tiếng Việt, hãy đảm bảo terminal đang dùng UTF-8 và font hỗ trợ Unicode.

## Tài khoản mẫu

| Role | Username | Email | Password |
|---|---|---|---|
| Admin | `admin` | `admin@demo.edu.vn` | `admin123` |
| Moderator | `moderator` | `moderator@demo.edu.vn` | `moderator123` |
| User | `user1` | `user1@demo.edu.vn` | `user12345` |
| User | `user2` | `user2@demo.edu.vn` | `user12345` |

## Chức năng theo role

### Guest

- Đăng ký.
- Đăng nhập.
- Quên mật khẩu bằng OTP in ra console.
- Tra cứu tài liệu APPROVED ở mức thông tin cơ bản.

### User

- Tải lên tài liệu giả lập, tài liệu mới ở trạng thái `PENDING_REVIEW`.
- Tra cứu, xem chi tiết và tải xuống mô phỏng tài liệu APPROVED.
- Bình luận, sửa/xóa bình luận của chính mình, đánh giá tài liệu 1-5 sao.
- Xem/cập nhật profile, xem tài liệu đã upload, lưu tài liệu vào thư viện cá nhân, tạo collection `PRIVATE`/`SHARED`.
- Tạo và quản lý bài yêu cầu tài liệu, bình luận bài `OPEN`, đánh dấu `FULFILLED`.

### Moderator

- Kiểm duyệt tài liệu: xem `PENDING_REVIEW`, approve sang `APPROVED`, reject sang `REJECTED` kèm lý do.
- Tra cứu tài liệu.
- Kiểm soát nội dung forum: xem bài/comment, ẩn/xóa nội dung vi phạm kèm lý do.
- Xem/tìm kiếm danh sách người dùng, không được chỉnh sửa role/trạng thái.

### Admin

- Quản trị người dùng: xem/tìm kiếm, đổi role `USER`/`MODERATOR`, khóa/mở khóa tài khoản.
- Duyệt bài yêu cầu tài liệu: approve sang `OPEN`, reject sang `REJECTED` kèm lý do.
- Xem báo cáo/thống kê.
- Xem Activity Log gần đây.

## Ghi chú nghiệp vụ

- Repository chỉ lưu dữ liệu in-memory, dữ liệu reset khi chạy lại chương trình.
- Password được hash giả lập bằng `PasswordUtil`, không phải bảo mật production.
- OTP được in ra console để mô phỏng gửi email.
- Upload/download chỉ là mô phỏng bằng metadata và `downloadCount`.
- Code ưu tiên rõ luồng nghiệp vụ để team dễ thuyết trình và vẽ UML.