package com.lion.pinepeople.repository;

import com.lion.pinepeople.domain.entity.Order;
<<<<<<< HEAD
import com.lion.pinepeople.domain.entity.User;
=======
>>>>>>> fd5ca66bb71328ea1b95556c58ccee548c89b8e1
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAll(Pageable pageable);
}
