package in.gov.telangana.agri.farmeridentityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FarmerIdentityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmerIdentityServiceApplication.class, args);
	}

}
