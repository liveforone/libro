package libro.libro.controller;

import libro.libro.dto.UserDto;
import libro.libro.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    //== 메인 페이지 ==//
    @GetMapping("/")
    public String home() {
        return "home";
    }

    //== 회원가입 페이지 ==//
    @GetMapping("/user/signup")
    public String signupPage() {
        return "/user/signup";
    }

    //== 회원가입 처리 ==//
    @PostMapping("/user/signup")
    public String signup(UserDto userDto) {
        userService.joinUser(userDto);
        log.info("Sign Up Success!!!");
        return "redirect:/user/login";
    }

    //== 로그인 페이지 - 스프링 시큐리티가 뷰만 띄어주면 알아서 해줌 ==//
    @GetMapping("/user/login")
    public String loginPage() {
        return "/user/login";
    }

    //== 로그인 결과 페이지 ==//
    @GetMapping("/user/login/result")
    public String LoginResult() {
        log.info("Login Success!!!");
        return "/user/loginSuccess";
    }

    //== 로그아웃 결과 페이지 ==//
    @GetMapping("/user/logout/result")
    public String Logout() {
        log.info("Logout Success!!!");
        return "/user/logout";
    }

    //== 판매자 등록 페이지 ==//
    @GetMapping("/user/seller")
    public String sellerPage() {
        return "/user/sellerRegi";
    }

    //== 판매자 등록 - 권한 업데이트 ==//
    @PostMapping("/user/seller")
    public String seller(Principal principal) {
        String email = principal.getName();
        userService.updateAuth(email);
        log.info("Seller Regi Success!!");
        return "redirect:/";
    }

    //== 접근 거부 페이지 ==//
    @GetMapping("/user/prohibition")
    public String prohibition() {
        return "/user/prohibition";
    }

    //== 어드민 페이지 ==//
    @GetMapping("/admin")
    public String admin() {
        return "/user/admin";
    }

    //== 주의사항 페이지 ==//
    @GetMapping("/user/warn")
    public String warn() {
        return "/user/warn";
    }
}
