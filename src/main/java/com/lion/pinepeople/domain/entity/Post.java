package com.lion.pinepeople.domain.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Transactional // @Transactional LAZY 값 조회
@Builder
@Getter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE post SET deleted_at = CURRENT_TIMESTAMP where post_id = ?")
public class Post extends BaseEntity {


    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @Column(length = 50)
    private String title;

    private String body;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;


    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostRecommend> recommends;


    @Column(columnDefinition = "int default 0")	// 조회수의 기본 값을 0으로 지정, null 불가 처리
    private int hits; // 조회수

    @Column(columnDefinition = "int default 0")
    private int commentsCount;

    @Column(columnDefinition = "int default 0")
    private int recommendsCount;



    public void updatePost(String title, String body) {

        this.title = title;
        this.body = body;

    }


    public void updateHits(int hits) {
        this.hits = hits + 1;
     }

    public void addCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount + 1;
    }
    public void deleteCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount - 1;
    }

    public void addRecommendsCount() {
        this.recommendsCount = recommendsCount + 1;
    }

    public void deleteRecommendsCount() {
        this.recommendsCount = recommendsCount - 1;
    }


}