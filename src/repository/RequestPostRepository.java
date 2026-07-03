package repository;

import model.RequestPost;
import model.enums.RequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class RequestPostRepository {
    private List<RequestPost> posts = new ArrayList<>();

    public void save(RequestPost post) {
        findById(post.getId()).ifPresent(posts::remove);
        posts.add(post);
    }

    public Optional<RequestPost> findById(String id) {
        return posts.stream().filter(post -> post.getId().equalsIgnoreCase(id)).findFirst();
    }

    public List<RequestPost> findAll() {
        return new ArrayList<>(posts);
    }

    public List<RequestPost> findByCreator(String creatorId) {
        return posts.stream().filter(post -> post.getCreatorId().equals(creatorId)).collect(Collectors.toList());
    }

    public List<RequestPost> findByStatus(RequestStatus status) {
        return posts.stream().filter(post -> post.getStatus() == status && !post.isDeleted()).collect(Collectors.toList());
    }

    public List<RequestPost> findPublicPosts() {
        return posts.stream()
                .filter(post -> !post.isDeleted() && !post.isHidden())
                .filter(post -> post.getStatus() == RequestStatus.OPEN || post.getStatus() == RequestStatus.FULFILLED)
                .collect(Collectors.toList());
    }
}
