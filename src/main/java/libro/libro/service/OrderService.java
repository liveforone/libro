package libro.libro.service;

import libro.libro.domain.Item;
import libro.libro.domain.OrderStatus;
import libro.libro.domain.Orders;
import libro.libro.domain.Users;
import libro.libro.dto.OrdersDto;
import libro.libro.repository.ItemRepository;
import libro.libro.repository.OrderRepository;
import libro.libro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    //== 주문 리스트 ==//
    @Transactional(readOnly = true)
    public List<Orders> getOrderList(String member) {
        return orderRepository.findByMember(member);
    }

    //== 주문 - 고객 자산 검증 ==//
    @Transactional(readOnly = true)
    public int getMemberInfo(String member, int price) {
        Users users = userRepository.findByEmail(member);
        int balance = users.getBalance();
        //자산검증
        if (balance < price) {  //구매불가
            return -1;
        }
        return 1;  //구매 가능
    }

    //== 주문 - order ==//
    @Transactional
    public void saveOrder(String itemTitle, String member, int price) {
        userRepository.changeBalance(price, member);  //상품 가격만큼 자산에서 빼야함.
        OrdersDto ordersDto = OrdersDto.builder()
                .itemTitle(itemTitle)
                .member(member)
                .status(OrderStatus.ORDER)
                .build();
        itemRepository.minusRemaining(itemTitle);  //상품 재고 -1
        orderRepository.save(ordersDto.toEntity());
    }

    //== 주문 날짜 - 주문 취소를 위한 ==//
    @Transactional(readOnly = true)
    public int getOrderDay(String title) {
        Orders orders = orderRepository.findByItemTitle(title);
        int localDate = orders.getCreatedDate().getDayOfYear() + 7;  //생성날짜 + 7일
        int nowDate = LocalDate.now().getDayOfYear();
        if (nowDate <= localDate) {
            return 1;  //주문취소 가능, 1 == true라는 뜻
        }
        return -1;  //주문취소 불가능, -1 == False라는 뜻
    }

    //== 주문취소 ==//
    @Transactional
    public void cancelOrder(String title, String email) {
        Item item = itemRepository.findByTitle(title);
        int price = item.getPrice();
        userRepository.updateBalance(price - 6000, email);  //6천원(배송비)를 뺸 상품가격을 자산에 복구한다.
        Orders order = orderRepository.findByItemTitle(title);
        OrdersDto ordersDto = OrdersDto.builder()
                .id(order.getId())
                .itemTitle(order.getItemTitle())
                .member(order.getMember())
                .status(OrderStatus.CANCEL)
                .build();
        itemRepository.plusRemaining(title);  //재고수량 복구(+1)
        orderRepository.save(ordersDto.toEntity());
    }
}
