package libro.libro.controller;

import libro.libro.domain.Orders;
import libro.libro.domain.Users;
import libro.libro.dto.OrdersDto;
import libro.libro.service.ItemService;
import libro.libro.service.OrderService;
import libro.libro.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {  //주문과 myPage 를 함께 다룸(둘이 연관관계가 있음)

    private final OrderService orderService;
    private final UserService userService;

    //== 마이페이지 ==//
    @GetMapping("/user/myPage/{email}")
    public String myPage(
            @PathVariable("email") String email,
            Model model
    ) {
        Users user = userService.getUser(email);
        model.addAttribute("user", user);
        return "/user/myPage";
    }

    //== 주소 등록 ==//
    @PostMapping("/user/myPage/address/{email}")
    public String regiAddress(
            @PathVariable("email") String email,
            @RequestParam("address") String userAddress
    ) {
        String url = "/user/myPage/" + email;
        userService.regiAddress(email, userAddress);
        log.info("Address Regi Success!!");
        return "redirect:" + url;
    }

    //== 잔액 충전 ==//
    @PostMapping("/user/myPage/deposit/{email}")
    public String deposit(
            @PathVariable("email") String email,
            @RequestParam("deposit") int deposit
    ) {
        String url = "/user/myPage/" + email;
        userService.updateBalance(deposit, email);
        log.info("Deposit Success!!");
        return "redirect:" + url;
    }

    //== 주문 리스트 ==//
    @GetMapping("/user/myPage/orderList/{email}")
    public String orderList(
            @PathVariable("email") String email,
            Model model
    ) {
        List<Orders> ordersList = orderService.getOrderList(email);
        model.addAttribute("ordersList", ordersList);
        model.addAttribute("email", email);
        return "/order/orderList";
    }

    //== 상품 주문 ==//
    @PostMapping("/user/item/order/{title}")
    public String orderItem(
            @PathVariable("title") String title,
            @RequestParam("price") int price,
            Principal principal,
            Model model
    ) {
        String member = principal.getName();  //구매자
        int info = orderService.getMemberInfo(member, price);  //잔액과 상품금액 비교, 1 or -1

        if (info == 1) {  //구매 가능
            orderService.saveOrder(title, member, price);
            log.info("Order Success!");
        } else {  //구매불가능
            model.addAttribute("member", member);
            model.addAttribute("title", title);
            return "/order/orderDeny";
        }
        model.addAttribute("member", member);
        model.addAttribute("title", title);
        return "/order/orderSuccess";
    }

    //== 주문취소 ==//
    @PostMapping("/user/item/cancel/{title}")
    public String cancel(
            @PathVariable("title") String title,
            @RequestParam("email") String email
    ) {
        String url = "/user/myPage/orderList/" + email;
        int orderPossible = orderService.getOrderDay(title);

        if (orderPossible == 1) {  //주문취소 가능
            orderService.cancelOrder(title, email);
            log.info("Order Cancel Success!!");
        } else {  //주문취소 불가능
            log.info("Order Cancel Impossible");
            return "/order/cant";
        }
        return "redirect:" + url;
    }
}
