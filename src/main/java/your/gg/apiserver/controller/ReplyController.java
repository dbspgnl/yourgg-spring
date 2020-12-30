package your.gg.apiserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import your.gg.apiserver.domain.Post;
import your.gg.apiserver.domain.Reply;
import your.gg.apiserver.exception.NotFoundException;
import your.gg.apiserver.repository.ReplyRepository;
import your.gg.apiserver.repository.PostRepository;

import java.net.URI;
import java.util.*;

@RestController
public class ReplyController {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/reply")
    public Map<String, List<Reply>> retrieveAllReply() {
        Map<String, List<Reply>> replies = new HashMap<>();
        replies.put("result", replyRepository.findAll());
        return replies;
    }

    @GetMapping("/reply/{id}") // reply 상세보기
    public Map<String, List<Reply>> retrieveAllReplyBySeq(@PathVariable int id) {
        Map<String, List<Reply>> replies = new HashMap<>();
        List<Reply> reply = replyRepository.findAllByListNum(id);
        replies.put("result", reply);
        return replies;
    }

    @PostMapping("/reply/write") // reply 등록
    public ResponseEntity<Reply> createReply(@RequestBody Reply reply) {
        int listNum = reply.getListNum();
        replyCtnUp(listNum); // 댓글수 +1
        reply.setDate(new Date());
        reply.setListNum(listNum);
        reply.setGroupNum(1);
        reply.setGroupOrder(1);
        // reply db 저장
        Reply savedReply = replyRepository.save(reply);
        //if(savedReply != null) System.out.println("세이브 값이 널이 아니다.");
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedReply.getSeq())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/reply/{id}") // reply 삭제
    public void deleteReply(@PathVariable int id) {
        Optional<Reply> reply = replyRepository.findById(id);
        if (!reply.isPresent()) {
            throw new NotFoundException(String.format("ID[%s} not found", id));
        } else {
            replyRepository.deleteById(id);
        }
    }

    @PutMapping("/reply/{id}") // reply 수정
    public ResponseEntity<Reply> updateReply(@PathVariable int id, @RequestBody Reply reply) {
        Optional<Reply> isReply = replyRepository.findById(id);
        if (!isReply.isPresent()) {
            throw new NotFoundException(String.format("ID[%s} not found", id));
        }
        Reply replyData = reply;
        replyData.setSeq(id);
        replyData.setGroupNum(isReply.get().getGroupNum());
        replyData.setGroupOrder(isReply.get().getGroupOrder());
        replyData.setListNum(isReply.get().getListNum());
        replyData.setDate(new Date());
        Reply updatedReply = replyRepository.save(replyData);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedReply.getSeq())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    // 댓글 수 증가
    public void replyCtnUp(int id){
        Optional<Post> isPost = postRepository.findById(id);
        if (!isPost.isPresent()) {
            throw new NotFoundException(String.format("ID[%s} not found", id));
        }
        Post postData = isPost.get();
        postData.setReplyCtn(postData.getReplyCtn()+1);
        Post post = postRepository.save(postData);
    }

}
