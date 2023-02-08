package com.lion.pinepeople.domain.entity;

import com.lion.pinepeople.enums.PartyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE party SET deleted_at = current_timestamp WHERE party_id = ? ")
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

    @Enumerated(STRING)
    private PartyStatus partyStatus;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name ="user_id")
    private User user;
    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cagegory_id")
    private Category category;

    public void updateStatus(PartyStatus partyStatus){
        this.partyStatus=partyStatus;
    }

}
