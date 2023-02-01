package com.lion.pinepeople.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE comment SET deleted_at = CURRENT_TIMESTAMP where id = ?")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;


    /***
     * convertToEntity
     * @param body
     * @param user
     * @param post
     * @return
     */
    public static Comment convertToEntity (String body, User user, Post post) {
        return Comment.builder()
                .body(body)
                .user(user)
                .post(post)
                .build();
    }


    public void update(String body) {
        this.body = body;
    }

}