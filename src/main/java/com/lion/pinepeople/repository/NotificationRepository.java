package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.receiver.id = :userId")
    Page<Notification> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

//    @Query("select count(n) from Notification n where n.receiver.id = :userId and n.isRead is false ")
//    Integer countUnReadNotifications(@Param("userId") Long userId);

}
