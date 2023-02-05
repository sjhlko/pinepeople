package com.lion.pinepeople.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Brix extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brix_id")
    private Long id;

    @Column(name = "brix_figure")
    private Double brixFigure;

    @Column(name = "brix_name")
    private String brixName;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    public static Brix toEntity(double brixdefault, String brixName, User user) {
        return Brix.builder()
                .brixFigure(brixdefault)
                .brixName(brixName)
                .user(user)
                .build();
    }

    public void update(Double brixNum) {
        this.brixFigure += brixNum;
    }
}
