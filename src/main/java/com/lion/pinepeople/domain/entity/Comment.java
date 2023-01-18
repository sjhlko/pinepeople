package com.lion.pinepeople.domain.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postid")
    private Long comment_id;

    @OneToMany(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Long post_id;

    @OneToMany(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Long user_id;



}
