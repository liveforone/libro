package libro.libro.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Role auth;

    /*
    balance는 환불하는 과정에서 음수가 될 수도 있다.
    int형의 양/음을 구별은 unsigned/signed 인데 mysql의 default는 signed이다.
    따라서 걱정하지 말자. unsigned형은 아예 음수로 갈 수 없다. 조심하도록!!
     */
    @Column  
    private int balance;  //자산

    @Column(columnDefinition = "integer default 0")
    private int count;  //주문 건수

    @Column
    private String address;

    @Builder
    public Users(
            Long id,
            String email,
            String password,
            Role auth,
            int balance,
            int count,
            String address
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.auth = auth;
        this.balance = balance;
        this.count = count;
        this.address = address;
    }
}
