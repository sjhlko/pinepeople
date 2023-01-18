//package com.lion.pinepeople.domain.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@AllArgsConstructor
//@Getter
//@NoArgsConstructor
//public class BlackList {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "blackList_id")
//    private Long blackListId;
//
//    @Column(name = "start_date")
//    private LocalDateTime startDate;
//
//    @OneToMany
//    @JoinColumn(name = "user_id")
//    private List<User> fromReportUsers = new ArrayList<>();
//
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User blackListUser;
//
//
//}
