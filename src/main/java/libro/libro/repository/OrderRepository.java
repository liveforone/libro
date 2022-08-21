package libro.libro.repository;

import libro.libro.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    //== 구매자 이름으로 찾기 - orderList ==//
    List<Orders> findByMember(String member);

    //== 상품 이름으로 찾기 - orderCancel ==//
    Orders findByItemTitle(String itemTitle);
}
