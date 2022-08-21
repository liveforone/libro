package libro.libro.repository;

import libro.libro.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, Long> {

    //== 이메일로 찾기 ==//
    Users findByEmail(String email);

    //== 주소 업데이트 ==//
    @Modifying
    @Query("update Users u set u.address = :address where u.email = :email")
    void updateAddress(@Param("address") String address ,@Param("email") String email);

    //== 계좌 업데이트 - 잔액 충전 ==//
    @Modifying
    @Query("update Users u set u.balance = u.balance + :balance where u.email = :email")
    void updateBalance(@Param("balance") int balance, @Param("email") String email);

    //== 계좌 업데이트 - 주문 완료 ==//
    @Modifying
    @Query("update Users u set u.balance = u.balance - :price where u.email = :email")
    void changeBalance(@Param("price") int price, @Param("email") String email);

    //== 주문횟수 업데이트 - 주문 완료 ==//
    @Modifying
    @Query("update Users u set u.count = u.count + 1 where u.email = :email")
    void plusCount(@Param("email") String email);

    //== 주문횟수 업데이트 - 주문 취소 ==//
    @Modifying
    @Query("update Users u set u.count = u.count - 1 where u.email = :email")
    void minusCount(@Param("email") String email);
}
