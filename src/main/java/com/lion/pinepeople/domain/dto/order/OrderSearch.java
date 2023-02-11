package com.lion.pinepeople.domain.dto.order;

import com.lion.pinepeople.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderSearch {

    private String userName;
    private OrderStatus orderStatus;
}
