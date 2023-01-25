package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Alarm;
import com.lion.pinepeople.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Alarm findByUserAndTargetId(User user, Long targetId);

    Page<Alarm> findAllByUser(User user, Pageable pageable);
}
