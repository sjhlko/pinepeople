package com.lion.pinepeople.service;

import com.lion.pinepeople.domain.dto.comment.CommentDeleteResponse;
import com.lion.pinepeople.domain.dto.comment.CommentResponse;
import com.lion.pinepeople.domain.dto.comment.CommentUpdateResponse;
import com.lion.pinepeople.domain.dto.post.PostReadResponse;
import com.lion.pinepeople.domain.entity.Comment;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.repository.CommentRepository;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentService {


    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    // response 들어온 걸 파라미터로 받으니까 entity -> dto로 바꾸고 로직 처리?
    public CommentResponse createComment(String userId, Long id, String comment) {

        User findUser = AppUtil.findUser(userRepository, Long.parseLong(userId)); // 회원 확인
        Post findPost = AppUtil.findPost(postRepository, id); // 게시물 확인

        Comment saveComment = Comment.convertToEntity(comment, findUser, findPost); // 댓글 Dto -> Entity 변환
        saveComment = commentRepository.save(saveComment); // 댓글 Entity 저장

        return CommentResponse.convertToDto(saveComment); // 저장한 entity -> Dto 리턴 => Copntroller

    }


    public Page<CommentResponse> readCommentPage(Pageable pageable, Long postId) {
        Post findPost = AppUtil.findPost(postRepository, postId);
        Page<Comment> commentPage = commentRepository.findByPost(pageable, findPost);
        return CommentResponse.convertListToDto(commentPage);
    }



    public Page<CommentResponse> readMyComments(Pageable pageable, String userId) {

        // 회원 확인
        User findUser = AppUtil.findUser(userRepository, Long.parseLong(userId));

        // 내가 쓴 댓글 불러오기
        Page<Comment> myComments = commentRepository.findByUser(pageable, findUser);

        return CommentResponse.convertListToDto(myComments);
    }




    public CommentUpdateResponse update(String userId, Long postId, Long id, String comment) {
        //유저체크
        User findUser = AppUtil.findUser(userRepository, Long.parseLong(userId));
        //포스트 체크
        Post findPost = AppUtil.findPost(postRepository, postId);
        //댓글 체크
        Comment findBody = AppUtil.findBody(commentRepository, postId, id);
        //작성자 체크
        AppUtil.checkUser(findBody.getUser().getId(), findUser.getId());
        //댓글 수정
        findBody.update(comment);
        commentRepository.saveAndFlush(findBody);
        return CommentUpdateResponse.convertToDto(findBody);
    }


    public CommentDeleteResponse delete(String userId, Long postId, Long id) {
        //유저 체크
        User findUser = AppUtil.findUser(userRepository, Long.parseLong(userId));
        //포스트 체크
        Post findPost = AppUtil.findPost(postRepository, postId);
        //댓글 체크
        Comment findBody = AppUtil.findBody(commentRepository, postId, id);
        //작성자 비교
        AppUtil.checkUser(findBody.getUser().getId(), findUser.getId());
        //삭제
        commentRepository.delete(findBody);
        //DTO 리턴
        return CommentDeleteResponse.ConvertToDto("댓글 삭제 완료", findBody.getId());
    }



}
