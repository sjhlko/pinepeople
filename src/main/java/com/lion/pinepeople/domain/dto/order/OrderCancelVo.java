package com.lion.pinepeople.domain.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelVo {

    private String imp_uid;
    private Long partyId;
    private Long orderId;
}
