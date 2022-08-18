package libro.libro.service;

import libro.libro.domain.Role;
import libro.libro.domain.Users;
import libro.libro.dto.UserDto;
import libro.libro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    //== 회원 가입 로직 ==//
    @Transactional
    public Long joinUser(UserDto userDto) {
        //비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setAuth(Role.MEMBER);  //기본 권한 매핑

        return userRepository.save(userDto.toEntity()).getId();
    }

    //== 로그인 로직 ==//
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (("admin@eduText.com").equals(email)) {  //어드민 아이디 지정됨, 비밀번호는 회원가입해야함
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else if (users.getAuth() == Role.MEMBER) {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        } else if (users.getAuth() == Role.SELLER) {
            authorities.add(new SimpleGrantedAuthority(Role.SELLER.getValue()));
        }

        return new User(users.getEmail(), users.getPassword(), authorities);
    }

    //== 권한 업데이트 ==//
    @Transactional
    public void updateAuth (String email) {
        //권한을 업데이트(컨텍스트홀더 + db객체)
        //업데이트 한 권한 현재 객체에 저장, 로그아웃 하지 않아도 됨!
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority(Role.SELLER.getValue()));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        //컨텍스트홀더 업데이트 끝.

        //db업데이트
        Users user = userRepository.findByEmail(email);
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .auth(Role.SELLER)
                .balance(user.getBalance())
                .count(user.getCount())
                .build();
        userRepository.save(userDto.toEntity());
    }
}
