package repository;

import model.RequestPost;
import model.enums.RequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Repository in-memory dùng chung
 * PURPOSE: Thành phần nền tảng của phần 1, phục vụ mô phỏng dữ liệu và luồng nghiệp vụ ở các phần sau.
 */
public class RequestPostRepository {
    private final List<RequestPost> posts = new ArrayList<>();

    public void save(RequestPost post) {
        findById(post.id).ifPresent(posts::remove);
        posts.add(post);
    }

    public Optional<RequestPost> findById(String id) {
        return posts.stream().filter(post -> post.id.equalsIgnoreCase(id)).findFirst();
    }

    public List<RequestPost> findAll() {
        return new ArrayList<>(posts);
    }

    public List<RequestPost> findByCreator(String creatorId) {
        return posts.stream().filter(post -> post.creatorId.equals(creatorId)).collect(Collectors.toList());
    }

    public List<RequestPost> findByStatus(RequestStatus status) {
        return posts.stream().filter(post -> post.status == status && !post.deleted).collect(Collectors.toList());
    }

    public List<RequestPost> findPublicPosts() {
        return posts.stream()
                .filter(post -> !post.deleted && !post.hidden)
                .filter(post -> post.status == RequestStatus.OPEN || post.status == RequestStatus.FULFILLED)
                .collect(Collectors.toList());
    }
}
