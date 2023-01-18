package com.lion.pinepeople.domain.dto;

import com.lion.pinepeople.domain.entity.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class OrderRequest {

    private Integer cost;
    private OrderType orderType;
    private Integer discountPoint;

}
