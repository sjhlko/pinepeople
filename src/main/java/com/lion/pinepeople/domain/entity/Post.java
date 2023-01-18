package com.lion.pinepeople.domain.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.lion.pinepeople.domain.dto.PostCreateRequest;
import com.lion.pinepeople.domain.dto.PostCreateResponse;
import lombok.*;

import javax.persistence.*;
import java.time.DateTimeException;

import static javax.persistence.FetchType.LAZY;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;
    private String title;
    private String body;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "like_id")

    // @OneToMany(name = "like_id" , mappedBy = )
    //
    //@OneToMany(name = "comment_id")


//    public void update(String title, String body) {
//        this.title = title;
//        this.body = body;
//    }


}
