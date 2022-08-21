package libro.libro.sevice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class OrderServiceTest {

    @Test
    @Rollback(value = false)
    @DisplayName("주문취소 시간 테스트")
    void dateTest() {
        LocalDate localDate = LocalDate.of(2022, 8, 21);
        int dbDate = localDate.getDayOfYear() + 7;
        int nowDate = LocalDate.now().getDayOfYear();
        System.out.println("dbDate : " + dbDate);
        System.out.println("nowDate : " + nowDate);
        assertThat(dbDate - 7).isEqualTo(nowDate);
    }
}
