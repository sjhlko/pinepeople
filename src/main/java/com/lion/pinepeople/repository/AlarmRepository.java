package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Long, Alarm> {
}
