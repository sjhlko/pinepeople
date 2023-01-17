package com.lion.pinepeople.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class Brix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brix_id")
    private Long id;

    @Column(name = "brix_figure")
    private Double brixFigure;

    @Column(name = "brix_name")
    private String brixName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void update(Double brixNum) {
        this.brixFigure += brixNum;
    }
}
