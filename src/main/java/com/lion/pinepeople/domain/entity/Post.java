package com.lion.pinepeople.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true) // orphanRemoval 관계가 끊어진 child를 자동 제거
    private List<Comment> comments;

    private Long commentsCount;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PostBookmark> bookmarks;

    private Long boomarksCount;

    //@ColumnDefault = 0, nullable = false)	// 조회수의 기본 값을 0으로 지정, null 불가 처리
    private Long hits;

    private String keyword;


    public void updatePost(String title, String body) {
        this.title = title;
        this.body = body;
    }

}