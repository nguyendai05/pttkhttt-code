package repository;

import model.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class CommentRepository {
    private List<Comment> comments = new ArrayList<>();

    public void save(Comment comment) {
        findById(comment.getId()).ifPresent(comments::remove);
        comments.add(comment);
    }

    public Optional<Comment> findById(String id) {
        return comments.stream().filter(comment -> comment.getId().equalsIgnoreCase(id)).findFirst();
    }

    public List<Comment> findByTarget(String targetType, String targetId) {
        return comments.stream()
                .filter(comment -> comment.getTargetType().equals(targetType) && comment.getTargetId().equals(targetId))
                .filter(comment -> !comment.isDeleted() && !comment.isHidden())
                .collect(Collectors.toList());
    }

    public List<Comment> findAll() {
        return new ArrayList<>(comments);
    }
}
