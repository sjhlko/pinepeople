package com.lion.pinepeople.domain.dto.order;

import com.lion.pinepeople.domain.entity.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderRequest {

    private PaymentType paymentType;
    private Integer discountPoint;

}
