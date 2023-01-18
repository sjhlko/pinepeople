package com.lion.pinepeople.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE party SET deleted_at = current_timestamp WHERE id = ? ")
public class Party extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;
    private String partyTitle;
    private String partyContent;
    private Integer partySize;
    private Integer partyCost;
    private Date startDate;
    private Date endDate;
    private String address;
    private String announcement;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name ="user_id")
    private User user;
    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Participant> participants = new ArrayList<>();


}
