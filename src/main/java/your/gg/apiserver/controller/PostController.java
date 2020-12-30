package your.gg.apiserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import your.gg.apiserver.domain.Post;
import your.gg.apiserver.exception.NotFoundException;
import your.gg.apiserver.repository.PostRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/posts/") //전체 post 검색
    public Map<String, List<Post>> retrieveAllPosts() {
        Map<String, List<Post>>posts = new HashMap<>();
        posts.put("posts", postRepository.findAllByOrderBySeqDesc());
        if (posts == null) {
            throw new NotFoundException("posts not found");
        }
        return posts;
    }

    @GetMapping("/posts/detail/{id}") // post 상세보기
    public Map<String, Post> retrieveAllPostsByUser(@PathVariable int id) {
        Optional<Post> post = postRepository.findById(id);
        Post getPost = post.get();
        Map<String, Post>posts = new HashMap<>();
        posts.put("posts", getPost);
        return posts;
    }

    @PutMapping("/posts/view/{id}")// 조회수 +1
    public ResponseEntity<Post> viewCtnUp(@PathVariable int id){
        Optional<Post> isPost = postRepository.findById(id);
        if (!isPost.isPresent()) {
            throw new NotFoundException(String.format("ID[%s} not found", id));
        }
        Post postData = isPost.get();
        postData.setViewsCtn(postData.getViewsCtn()+1);
        Post post = postRepository.save(postData);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getSeq())
                .toUri();
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/posts/write") // post 등록
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) {
        post.setDate(new Date());
        post.setRcmCtn(0);
        post.setReplyCtn(0);
        post.setViewsCtn(0);
        Post savedPost = postRepository.save(post);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getSeq())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/posts/{id}") // post 삭제
    public void deletePost(@PathVariable int id) {
        Optional<Post> post = postRepository.findById(id);
        if (!post.isPresent()) {
            throw new NotFoundException(String.format("ID[%s} not found", id));
        } else {
            postRepository.deleteById(id);
        }
    }

    @PutMapping("/posts/{id}") // post 수정
    public ResponseEntity<Post> updatePost(@PathVariable int id, @Valid @RequestBody Post post){
        Optional<Post> isPost = postRepository.findById(id);
        if (!isPost.isPresent()) {
            throw new NotFoundException(String.format("ID[%s} not found", id));
        }
        //Post postData;
        Post postData = post;
        //postData.setRcmCtn(postData.getRcmCtn()+1);
        Post updatedPost = postRepository.save(postData);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedPost.getSeq())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/posts/rcm/{id}")// 추천수 +1
    public ResponseEntity<Post> rcmCtnUp(@PathVariable int id){
        Optional<Post> isPost = postRepository.findById(id);
        if (!isPost.isPresent()) {
            throw new NotFoundException(String.format("ID[%s} not found", id));
        }
        Post postData = isPost.get();
        postData.setRcmCtn(postData.getRcmCtn()+1);
        Post post = postRepository.save(postData);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getSeq())
                .toUri();
        return ResponseEntity.status(200).build();
    }

    @PutMapping("/posts/reply/{id}")// 댓글수 +1
    public ResponseEntity<Post> replyCtnUp(@PathVariable int id){
        Optional<Post> isPost = postRepository.findById(id);
        if (!isPost.isPresent()) {
            throw new NotFoundException(String.format("ID[%s} not found", id));
        }
        Post postData = isPost.get();
        postData.setReplyCtn(postData.getReplyCtn()+1);
        Post post = postRepository.save(postData);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getSeq())
                .toUri();
        return ResponseEntity.status(200).build();
    }

}
