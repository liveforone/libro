package libro.libro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LibroApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibroApplication.class, args);
	}

}
