package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.post.PostReadResponse;
import com.lion.pinepeople.domain.dto.post.PostResponse;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {



    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public PostResponse create(String userId, String title, String body) {

        // 회원 이름 확인
        User findUser = AppUtil.findUser(userRepository, Long.parseLong(userId)); // String -> Long 변환

        // 게시물 저장
        Post savePost = Post.convertToEntity(title, body, findUser);
        savePost = postRepository.save(savePost);

        // Entity -> DTO 변환 후 반환
        return PostResponse.convertToDto("포스트 등록 성공", savePost.getPostId());

    }


    public PostReadResponse readPost(Long postId) {

        // 회원 id로 게시물 검색
        Post findPost = AppUtil.findPost(postRepository, postId);

        // Post entity -> DTO로 변환 후 반환
        return PostReadResponse.convertToDto(findPost);

    }


    public Page<PostReadResponse> readPostList(Pageable pageable) {

        Page<Post> posts = postRepository.findAll(pageable);
        return PostReadResponse.ConvertListToDto(posts);

    }


    public Page<PostReadResponse> readMyPosts(Pageable pageable, String userId) {

        // 회원 확인
        User findUser = AppUtil.findUser(userRepository, Long.parseLong(userId));

        // 내가 쓴 게시물 불러오기
        Page<Post> myPosts = postRepository.findByUser(pageable, findUser);

        return PostReadResponse.ConvertListToDto(myPosts);
    }


    public PostResponse update(String userId, Long postId, String title, String body) {

        Post updatePost = AppUtil.findPost(postRepository, postId);

        User findUser = AppUtil.findUser(userRepository, Long.parseLong(userId));


        AppUtil.checkUser(updatePost.getUser().getId(), findUser.getId());


        updatePost.update(title, body);
        return PostResponse.convertToDto("게시물 수정 완료", updatePost.getPostId());

    }


    public PostResponse delete(String userId, Long postId) {

        Post deletePost = AppUtil.findPost(postRepository, postId);
        User findUser = AppUtil.findUser(userRepository, Long.parseLong(userId)); // long으로 변환
        AppUtil.checkUser(deletePost.getUser().getId(), findUser.getId());

        postRepository.delete(deletePost);
        return PostResponse.convertToDto("게시물 삭제 완료", deletePost.getPostId());

    }





}
