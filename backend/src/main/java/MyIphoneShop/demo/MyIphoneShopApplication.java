package MyIphoneShop.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyIphoneShopApplication {
	// lsof -i :8080
	// kill -9 1234 nếu cổng 8080 bị chiếm
	public static void main(String[] args) {
		SpringApplication.run(MyIphoneShopApplication.class, args);
	}

}
