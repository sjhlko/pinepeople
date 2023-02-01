package com.lion.pinepeople.domain.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo {

    private String imp_uid;
    private Integer discountPoint;
    private Long partyId;
    private Integer cost;
    private String paymentType;
}
