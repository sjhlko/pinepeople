package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.dto.order.OrderCancelResponse;
import com.lion.pinepeople.domain.dto.order.OrderCancelVo;
import com.lion.pinepeople.domain.dto.order.OrderResponse;
import com.lion.pinepeople.domain.dto.order.OrderVo;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.ErrorResponse;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.service.OrderService;
import com.lion.pinepeople.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("pinepeople")
@Api(tags = "IamPort API")
@Slf4j
public class PaymentController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserRepository userRepository;

    @ApiOperation(value = "만나서 결제")
    @PostMapping(value = "/order/contact-payment")
    public ResponseEntity<Response<?>> paymentContact(@RequestBody OrderVo orderVo, Authentication authentication) throws IOException {
        String name = authentication.getName();

        if (orderVo.getDiscountPoint() > 0) {
            return ResponseEntity.badRequest().body(Response.error(new ErrorResponse(ErrorCode.INVALID_ORDER_POINT, "만나서 결제는 포인트를 사용할 수 없습니다.")));
        }

        OrderResponse orderResponse = orderService.orderMvc(name, orderVo.getPartyId(), orderVo);
        return ResponseEntity.ok().body(Response.success(orderResponse));
    }

    @ApiOperation(value = "카드 결제 -> 완료 -> 검증")
    @PostMapping(value = "/order/payment/complete")
    public ResponseEntity<Response<?>> paymentComplete(@RequestBody OrderVo orderVo, Authentication authentication) throws IOException {
        long userId = Long.parseLong(authentication.getName());
        User user = userRepository.findById(userId).get();

        // 1. 아임포트 API 키와 SECRET키로 토큰을 생성
        String token = paymentService.getToken();
        log.info("token = {}", token);

        // 2. 토큰으로 결제 완료된 결제정보(결제 완료된 금액) 가져옴
        int amount = paymentService.paymentInfo(orderVo.getImp_uid(), token);
        System.out.println("결제 완료된 금액 = " + amount);

        try {
            // 현재 회원 포인트
            int point = user.getPoint();
            log.info("현재 회원 포인트 = " + point);

            // 주문 시 사용한 포인트
            int usePoint = orderVo.getDiscountPoint();
            log.info("주문 시 사용 포인트 = " + usePoint);

            // 4. 현재 포인트보다 사용포인트가 많을 경우 결제 취소
            if (point < usePoint) {
                paymentService.paymentCancel(token, orderVo.getImp_uid(), amount, "유저 포인트 오류");
                return ResponseEntity.badRequest().body(Response.error(new ErrorResponse(ErrorCode.INVALID_ORDER_POINT, "결제 취소. 현재 사용할 수 있는 최대 포인트는  " + point + "포인트 입니다")));
            }

            // 5. DB에서 실제 계산되어야 할 가격 가져오기(실제 계산 금액 가져오기)
            long orderPriceCheck = orderService.totalCost(user, orderVo.getDiscountPoint(), orderVo.getCost());
            log.info("DB상 실제 계산 금액 = " + orderPriceCheck);

            // 6. 결제 완료된 금액과 DB상 계산되어야 할 금액이 다를경우 결제 취소
            if (orderPriceCheck != amount) {
                paymentService.paymentCancel(token, orderVo.getImp_uid(), amount, "결제 금액 오류");
                return ResponseEntity.badRequest().body(Response.error(new ErrorResponse(ErrorCode.INVALID_ORDER_TOTAL_COST, "결제 취소. 결제 금액에 오류가 있습니다. 다시 시도해주세요.")));
            }

            // ** 주문하기 **
            OrderResponse orderResponse = orderService.orderMvc(authentication.getName(), orderVo.getPartyId(), orderVo);

            return ResponseEntity.ok().body(Response.success(orderResponse));
        } catch (Exception e) {
            paymentService.paymentCancel(token, orderVo.getImp_uid(), amount, "결제 에러");
            return ResponseEntity.badRequest().body(Response.error(new ErrorResponse(ErrorCode.INVALID_ORDER, "사용자가 결제를 취소하셨습니다.")));
        }
    }


    @ApiOperation(value = "결제 취소")
    @PostMapping(value = "/order/payment/cancel")
    public ResponseEntity<Response<?>> paymentCancel(@RequestBody OrderCancelVo orderCancelVo, Authentication authentication) throws IOException {
        log.info("authentication.getName()={}", authentication.getName());

        // 1. 아임포트 API 키와 SECRET키로 토큰을 생성
        String token = paymentService.getToken();
        log.info("token = {}", token);

        Long partyId = orderCancelVo.getPartyId();
        log.info("partyId={}", partyId);
        String imp_uid = orderCancelVo.getImp_uid();
        System.out.println("imp_uid = " + imp_uid);

        // ** 주문 취소 **
        // Imp_uid가 null이 아니면 아임포트 결제임 -> 아임포트 결제취소 api 호출해야 함
        if (orderCancelVo.getImp_uid() != null) {
            // 2. 토큰으로 결제 완료된 결제정보(결제 완료된 금액) 가져옴
            int amount = paymentService.paymentInfo(orderCancelVo.getImp_uid(), token);
            log.info("amount={}", amount);
            paymentService.paymentCancel(token, orderCancelVo.getImp_uid(), amount, "결제 에러");
        }
        OrderCancelResponse orderCancelResponse = orderService.cancelOrder(authentication.getName(), orderCancelVo.getOrderId(), orderCancelVo.getPartyId());
        return ResponseEntity.ok().body(Response.success(orderCancelResponse));
    }
}



