package libro.libro.repository;

import libro.libro.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByMember(String member);

    Orders findByItemTitle(String itemTitle);
}
