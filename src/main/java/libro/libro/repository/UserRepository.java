package libro.libro.repository;

import libro.libro.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);

    //== 주소 업데이트 ==//
    @Modifying
    @Query("update Users u set u.address = :address where u.email = :email")
    void updateAddress(@Param("address") String address ,@Param("email") String email);

    //== 계좌 업데이트 ==//
    @Modifying
    @Query("update Users u set u.balance = :balance where u.email = :email")
    void updateBalance(@Param("balance") int balance, @Param("email") String email);
}
