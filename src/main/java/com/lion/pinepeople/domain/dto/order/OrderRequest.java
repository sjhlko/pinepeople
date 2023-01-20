package com.lion.pinepeople.domain.dto.order;

import com.lion.pinepeople.domain.entity.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderRequest {

    private OrderType orderType;
    private Integer discountPoint;

}
