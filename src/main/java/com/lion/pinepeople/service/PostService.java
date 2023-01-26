package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.post.PostReadResponse;
import com.lion.pinepeople.domain.dto.post.PostResponse;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional // 트랜젝션
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    /****
     * create 게시물 작성
     * @param id
     * @param title
     * @param body
     * @return PostResponse(DTO)
     */
    public PostResponse create(String id, String title, String body) {

        // 회원 아이디(인증) 확인
        log.info("id:{}", id);
        User findUser = AppUtil.findUser(userRepository, Long.parseLong(id)); // Long 형변환

        // 게시물 저장
        Post savePost = Post.convertToEntity(title, body, findUser);
        savePost = postRepository.save(savePost);

        // Entity -> DTO 변환하여 반환
        return PostResponse.convertToDto("포스트 등록 성공", savePost.getId());
    }


    /****
     * getPost 게시물 단건 조회
     * @param id
     * @return
     */
    public PostReadResponse getPost(Long id) {

        // 게시물 id로 게시물 검색
        Post findPost = AppUtil.findPost(postRepository, id);

        // Post entity -> DTO로 변환 후 반환
        return PostReadResponse.convertToDto(findPost);

    }


    /***
     * getPostList 게시물 목록 조회
     * @param pageable
     * @return posts(Dto)
     */
    public Page<PostReadResponse> getPostList(Pageable pageable) {

        Page<Post> posts = postRepository.findAll(pageable);
        return PostReadResponse.convertListToDto(posts);

    }


    /***
     * getMyPosts 내 게시물 조회
     * @param pageable
     * @param id
     * @return
     */
    public Page<PostReadResponse> getMyPosts(Pageable pageable, Long id) {

        // 회원 확인
        User findUser = AppUtil.findUser(userRepository, id);

        // 내가 쓴 게시물 불러오기
        Page<Post> myPosts = postRepository.findByUser(pageable, findUser);

        return PostReadResponse.convertListToDto(myPosts);
    }


    /***
     * update 게시물 수정
     * @param postId
     * @param userId
     * @param title
     * @param body
     * @return
     */
    public PostResponse update(Long postId, String userId, String title, String body) {

        // 게시물 아이디 확인
        log.info("postId:{}", postId);
        Post updatePost = AppUtil.findPost(postRepository, postId);

        //회원 아이디(인증) 확인
        log.info("id:{}", userId);
        User findByUser = AppUtil.findUser(userRepository, Long.parseLong(userId));

        User findUser = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "해당 회원이 없습니다.");
                });


        // 게시물 작성자 비교
        AppUtil.checkUser(updatePost.getUser().getId(), findUser.getId());


        updatePost.update(title, body);
        return PostResponse.convertToDto("게시물 수정 완료", updatePost.getId());

    }



    /***
     * 게시물 삭제
     * @param postId
     * @param userId
     * @return
     */
    public PostResponse delete(Long postId, String userId) {

        Post deletePost = AppUtil.findPost(postRepository, postId);
        User findUser = AppUtil.findUser(userRepository, Long.parseLong(userId));
        AppUtil.checkUser(deletePost.getUser().getId(), findUser.getId()); // 메소드 이름 수정

        postRepository.delete(deletePost);
        return PostResponse.convertToDto("게시물 삭제 완료", deletePost.getId());

    }


}