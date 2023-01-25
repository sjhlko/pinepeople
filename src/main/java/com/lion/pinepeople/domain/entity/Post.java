package com.lion.pinepeople.domain.entity;


import lombok.*;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    private Long userId;



    public static Post convertToEntity(String title, String body, User user) {
        return Post.builder()
                .title(title)
                .body(body)
                .user(user)
                .build();

    }

    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }


}

