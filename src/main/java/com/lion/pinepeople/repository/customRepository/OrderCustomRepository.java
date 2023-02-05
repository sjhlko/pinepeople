package com.lion.pinepeople.repository.customRepository;

import com.lion.pinepeople.domain.dto.order.OrderSearch;
import com.lion.pinepeople.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCustomRepository {

    Page<Order> findAllByOrderStatus(OrderSearch orderSearch, String userName, Pageable pageable);
}
