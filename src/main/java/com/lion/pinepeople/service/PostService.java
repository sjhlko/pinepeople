package com.lion.pinepeople.service;


import com.lion.pinepeople.domain.dto.post.*;
import com.lion.pinepeople.domain.entity.Post;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.PostRepository;
import com.lion.pinepeople.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //private final static String VIEWCOOKIENAME = "alreadyViewCookie";

    @Transactional
    public PostCreateResponse create(PostCreateRequest postCreateRequest, String userId) {

        User findUser = validateUser(userId);

        Post savedPost = postRepository.save(postCreateRequest.of(findUser));

        return PostCreateResponse.of(savedPost);

    }


    public PostReadResponse getPost(Long postId) {

        validatePost(postId);
        //countUpHits(postId, request, response);
        //updateHits(postId, request, response);
        //validateHits(postId, request, response);

        postRepository.findById(postId);


        return PostReadResponse.of(postRepository.findById(postId));

    }


    public Page<PostReadResponse> getPostList(Pageable pageable) {

        return PostReadResponse.of(postRepository.findAll(pageable));

    }


    public Page<PostReadResponse> getMyPosts(Pageable pageable, String userId) {

        return PostReadResponse.of(postRepository.findByUser(pageable, validateUser(userId)));

    }



    public PostUpdateResponse update(Long postId, String userId, PostUpdateRequest postUpdateRequest) {

        validatePost(postId);
        validateUser(userId);
        verifyPostAuthor(userId, postId);

        Post updatedPost = validatePost(postId);

        updatedPost.updatePost(postUpdateRequest.getTitle(), postUpdateRequest.getBody());


        return PostUpdateResponse.of(updatedPost);

    }


    public PostDeleteResponse delete(Long postId, String userId) {


        validatePost(postId);
        log.info("postId: {}", postId);
        validateUser(userId);
        log.info("userId: {}", userId);
        verifyPostAuthor(userId, postId);

        postRepository.deleteById(postId);

        return PostDeleteResponse.of(postId);

    }





    // 키워드로 게시물 검색
    public Page<PostReadResponse> searchByKeyword(Pageable pageable, String keyword) {

        if (keyword == null) {
            searchByKeyword(pageable, null);
        } else {
            searchByKeyword(pageable, keyword);
        }


        return PostReadResponse.of(postRepository.findByKeywordContaining(keyword, pageable));

    }


    // 제목으로 게시물 검색
    public Page<PostReadResponse> searchByTitle(Pageable pageable, String keyword) {

        if (keyword == null) {
            searchByTitle(pageable, null);
        } else {
            searchByTitle(pageable, keyword);
        }


        return PostReadResponse.of(postRepository.findByTitle(keyword, pageable));

    }


    public User validateUser(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
    }

    public Post validatePost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> {
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        });
    }


    public void verifyPostAuthor(String userId, Long postId) {
        if (userRepository.findById(Long.parseLong(userId)).get().getId() != postRepository.findById(postId).get().getUser().getId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        };
    }


//
//    public void updateHits(Long postId, HttpServletRequest request, HttpServletResponse response) {
////        postRepository.countUpHits(postId);
//        Post findPost = postRepository.findById(postId).orElseThrow(() -> {
//            throw new AppException(ErrorCode.POST_NOT_FOUND,ErrorCode.POST_NOT_FOUND.getMessage());
//        });
//        findPost.updateHits();
//        postRepository.saveAndFlush(findPost);
//    }
//
//    @Transactional
//    public void validateHits(Post post, HttpServletRequest request, HttpServletResponse response) {
//        Cookie[] cookies = request.getCookies();
//        Cookie cookie = null;
//        boolean isCookie = false;
//        // request에 쿠키들이 있을 때
//        for (int i = 0; cookies != null && i < cookies.length; i++) {
//            // postView 쿠키가 있을 때
//            if (cookies[i].getName().equals("postView")) {
//                // cookie 변수에 저장
//                cookie = cookies[i];
//                // 만약 cookie 값에 현재 게시글 번호가 없을 때
//                if (!cookie.getValue().contains("[" + post.getId() + "]")) {
//                    // 해당 게시글 조회수를 증가시키고, 쿠키 값에 해당 게시글 번호를 추가
//                    post.updateHits();
//                    cookie.setValue(cookie.getValue() + "[" + post.getId() + "]");
//                }
//                isCookie = true;
//                break;
//            }
//        }
//        // 만약 postView라는 쿠키가 없으면 처음 접속한 것이므로 새로 생성
//        if (!isCookie) {
//            post.updateHits();
//            cookie = new Cookie("postView", "[" + post.getId() + "]"); // oldCookie에 새 쿠키 생성
//        }
//
//        // 쿠키 유지시간을 오늘 하루 자정까지로 설정
//        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
//        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//        cookie.setPath("/"); // 모든 경로에서 접근 가능
//        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
//        response.addCookie(cookie);
//    }

//
//    /*
//     * 조회수 중복 방지를 위한 쿠키 생성 메소드
//     * @param cookie
//     * @return
//     * */
//    private Cookie createCookieForForNotOverlap(Long postId) {
//        Cookie cookie = new Cookie(VIEWCOOKIENAME+postId, String.valueOf(postId));
//        cookie.setComment("조회수 중복 증가 방지 쿠키");
//        cookie.setMaxAge(getRemainSecondForTommorow());
//        cookie.setHttpOnly(true);
//        return cookie;
//    }
//
//    // 다음 날 정각까지 남은 시간(초)
//    private int getRemainSecondForTommorow() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime tommorow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
//        return (int) now.until(tommorow, ChronoUnit.SECONDS);
//    }
//
//

}