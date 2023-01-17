package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName (String name);
    Optional<Category> findByBranchAndName (String branch, String name);
    Boolean existsByBranchAndName(String branch, String name);
}