package repository;

import model.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Repository in-memory dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class CommentRepository {
    private final List<Comment> comments = new ArrayList<>();

    public void save(Comment comment) {
        findById(comment.id).ifPresent(comments::remove);
        comments.add(comment);
    }

    public Optional<Comment> findById(String id) {
        return comments.stream().filter(comment -> comment.id.equalsIgnoreCase(id)).findFirst();
    }

    public List<Comment> findByTarget(String targetType, String targetId) {
        return comments.stream()
                .filter(comment -> comment.targetType.equals(targetType) && comment.targetId.equals(targetId))
                .filter(comment -> !comment.deleted && !comment.hidden)
                .collect(Collectors.toList());
    }

    public List<Comment> findAll() {
        return new ArrayList<>(comments);
    }
}
