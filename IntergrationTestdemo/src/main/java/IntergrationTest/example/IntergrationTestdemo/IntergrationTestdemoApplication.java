package IntergrationTest.example.IntergrationTestdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IntergrationTestdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntergrationTestdemoApplication.class, args);
		System.out.println("Server is running");
	}

}
