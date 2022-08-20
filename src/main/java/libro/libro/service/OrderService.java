package libro.libro.service;

import libro.libro.domain.Orders;
import libro.libro.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    //== 주문 리스트 ==//
    @Transactional(readOnly = true)
    public List<Orders> getOrderList(String member) {
        return orderRepository.findByMember(member);
    }

    //== 주문 날짜 - 주문 취소를 위한 ==//
    @Transactional(readOnly = true)
    public String getOrderDay(String title) {
        Orders orders = orderRepository.findByItemTitle(title);
        int localDate = orders.getCreatedDate().getDayOfYear() + 7;
        int nowDate = LocalDate.now().getDayOfYear();
        if (nowDate <= localDate) {
            return "can";
        }
        return "cant";
    }
}
