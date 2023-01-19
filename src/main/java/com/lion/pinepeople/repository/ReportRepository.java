package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Report;
import com.lion.pinepeople.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByFromUserIdAndUser(Long loginUserId, User targetUser);

    List<Report> findAllByUser(User targetUser);
}
