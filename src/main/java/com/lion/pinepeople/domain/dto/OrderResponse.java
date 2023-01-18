package com.lion.pinepeople.domain.dto;

import com.lion.pinepeople.domain.entity.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderResponse {

    private Integer cost;
    private OrderType orderType;
    private Integer accumulateCost;
    private Integer discountPoint;
    private LocalDateTime orderDate;
    private String message;



}
