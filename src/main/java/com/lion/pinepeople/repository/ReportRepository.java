package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
