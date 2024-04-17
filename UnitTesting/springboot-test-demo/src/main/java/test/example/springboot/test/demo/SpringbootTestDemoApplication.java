package test.example.springboot.test.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootTestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTestDemoApplication.class, args);
		System.out.println("Server is Running");
	}
}
