package com.lion.pinepeople.mvc;

import com.lion.pinepeople.domain.dto.order.OrderVo;
import com.lion.pinepeople.domain.dto.party.PartyInfoResponse;
import com.lion.pinepeople.domain.entity.User;
import com.lion.pinepeople.exception.ErrorCode;
import com.lion.pinepeople.exception.customException.AppException;
import com.lion.pinepeople.repository.UserRepository;
import com.lion.pinepeople.service.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserRepository userRepository;

    /**
     * 주문 하기 form
     */
    @GetMapping("/users/party/{partyId}/order")
    public String orderForm(@PathVariable Long partyId, @ModelAttribute("discountPoint") OrderVo orderVo, Model model, Authentication authentication) {
        PartyInfoResponse party = partyService.getParty(partyId);
        User user = getUser(authentication);
        Integer discountPoint = orderVo.getDiscountPoint();
        int cost = (int) party.getPartyCost() / party.getPartySize();
        int commission = (int) (cost * 0.1); // 수수료

        model.addAttribute("discountPoint", discountPoint);
        model.addAttribute("party", party);
        model.addAttribute("cost", cost);
        model.addAttribute("commission", commission);
        model.addAttribute("user", user);
        return "pay/oder";
    }


    private User getUser(Authentication authentication) {
        long userId = Long.parseLong(authentication.getName());
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
