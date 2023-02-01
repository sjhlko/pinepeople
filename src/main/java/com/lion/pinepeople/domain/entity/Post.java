package com.lion.pinepeople.domain.entity;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Transactional // @Transactional LAZY 값 조회
@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String body;

    @ManyToOne(fetch = LAZY)
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true) // orphanRemoval 관계가 끊어진 child를 자동 제거
    private List<Comment> commentPage;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Like> likes;


    /***
     * convertToEntity dto를 entity로 변환하여 DB 저장
     * 고정된 크기를 가지는 자료 구조를 생성하는 메소드로 불필요한 할당을 하지 않기 위해 사용
     * @param title
     * @param body
     * @param user
     * @return
     */
    public static Post convertToEntity(String title, String body, User user) {
        return Post.builder()
                .title(title)
                .body(body)
                .user(user)
                .build();
    }


    /****
     * update 게시물 수정
     * @param title
     * @param body
     */
    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }

}