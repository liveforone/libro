package libro.libro.controller;

import libro.libro.domain.Orders;
import libro.libro.domain.Users;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {  //주문과 myPage를 함께 다룸(둘이 연관관계가 있음)

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

    //== 주문취소 ==//
    @PostMapping("/user/item/cancel/{title}")
    public String cancel(
            @PathVariable("title") String title
    ) {
        String orderDay = orderService.getOrderDay(title);
        if (orderDay.equals("can")) {
            //취소로직
        } else {
            return "/order/cant";
        }
        return "redirect:/user/itemHome";
    }
}
