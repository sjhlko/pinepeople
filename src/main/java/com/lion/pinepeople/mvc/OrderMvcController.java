package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.order.OrderInfoResponse;
import com.lion.pinepeople.domain.dto.order.OrderSearch;
import com.lion.pinepeople.domain.dto.order.OrderVo;
import com.lion.pinepeople.domain.dto.participant.ParticipantInfoResponse;
import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
import com.lion.pinepeople.domain.dto.user.myInfo.MyInfoResponse;
import com.lion.pinepeople.enums.ApprovalStatus;
import com.lion.pinepeople.enums.OrderStatus;
import com.lion.pinepeople.service.OrderService;
import com.lion.pinepeople.service.ParticipantService;
import com.lion.pinepeople.service.PartyService;
import com.lion.pinepeople.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("pinepeople")
@Slf4j
public class OrderMvcController {

    private final PartyService partyService;
    private final UserService userService;
    private final OrderService orderService;
    private final ParticipantService participantService;

    /**
     * 주문하기 페이지
     *
     * @param partyId        주문할 파티
     * @param orderVo        주문할 때 할인 정보를 담는 Vo
     * @param model          주문 할 때 필요한 입력 정보(할인금액) 넘겨주기
     * @param authentication 인증된 사용자만 주문할 수 있음
     * @return 주문 이후 주문 내역 페이지로 이동
     */
    @GetMapping("/party/{partyId}/order")
    public String orderForm(@PathVariable Long partyId, @ModelAttribute("discountPoint") OrderVo orderVo, Model model, Authentication authentication) {
        MyInfoResponse user = userService.getMyInfo(authentication.getName());
        PartyInfoResponse party = partyService.getParty(partyId);
        ParticipantInfoResponse participant = participantService.getParticipant(user.getUserId(), party.getPartyId());
        if (participant.getApprovalStatus().name() != ApprovalStatus.APPROVED.name()) {
            return "redirect:/pinepeople";
        }

        Integer discountPoint = orderVo.getDiscountPoint();
        int cost = (int) party.getPartyCost() / party.getPartySize();
        Integer commission = (int) (cost * 0.1); // 수수료

        model.addAttribute("discountPoint", discountPoint);
        model.addAttribute("party", party);
        model.addAttribute("cost", cost);
        model.addAttribute("commission", commission);
        model.addAttribute("user", user);
        return "pay/order";
    }

    /**
     * 주문 내역 상세 보기 페이지
     *
     * @param orderId        주문한 아이디
     * @param model          주문 정보에 필요한 정보 넘겨주기
     * @param authentication 인증된 본인의 주문만 볼 수 있음
     * @return 주문 상태에 따라 상세 페이지, 취소 페이지로 이동
     */
    @GetMapping("/party/order-detail/{orderId}")
    public String orderDetail(@PathVariable Long orderId, Model model, Authentication authentication) {
        MyInfoResponse user = userService.getMyInfo(authentication.getName());
        OrderInfoResponse order = orderService.getOrderDetail(String.valueOf(user.getUserId()), orderId);
        Integer commission = (int) (order.getCost() * 0.1);
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("commission", commission);
        if (order.getOrderStatus().equals(OrderStatus.ORDER_COMPLETE)) {
            return "pay/orderDetail";
        } else {
            return "pay/orderCancel";
        }
    }

    /**
     * 주문 내역 보기 페이지
     *
     * @param model          주문 내역에 필요한 내용 넘겨주기
     * @param authentication 인증된 본인의 주문내역 볼 수 있음
     * @param pageable       주문 내역 페이징 처리
     * @return 주문 내역 페이지로 이동
     */
    @GetMapping("/party/order-list")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model, Authentication authentication,
                            @PageableDefault(size = 5) Pageable pageable) {

        Page<OrderInfoResponse> orderList = orderService.findMyOrder(authentication.getName(), orderSearch, pageable);

        /**페이징 처리**/
        int nowPage = orderList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, orderList.getTotalPages());

        model.addAttribute("orderSearch", orderSearch);
        model.addAttribute("orderList", orderList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "pay/orderList";
    }
}
