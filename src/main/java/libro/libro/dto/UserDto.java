package libro.libro.dto;

import libro.libro.domain.Role;
import libro.libro.domain.Users;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private Role auth;
    private int balance;
    private int count;
    private String address;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    //==dto -> entity==//
    public Users toEntity() {
        return Users.builder()
                .id(id)
                .email(email)
                .password(password)
                .auth(auth)
                .balance(balance)
                .count(count)
                .address(address)
                .build();
    }

    @Builder
    public UserDto(
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
