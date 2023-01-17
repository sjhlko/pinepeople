package com.lion.pinepeople.domain.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class PartyComment extends BaseEntity  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_comment_id")
    private Long id;




}
