package customers.customerlist;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import customers.customerlist.domain.Customer;
import customers.customerlist.domain.CustomerRepository;

@SpringBootApplication
public class CustomerlistApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerlistApplication.class, args);
	}

	@Bean
	public CommandLineRunner alustaTietokanta(CustomerRepository customerRepository) {
		return (parametrit) -> {

			Customer customer1 = new Customer();
			customer1.setName("Matti Meikäläinen");
			customer1.setEmail("matti.meikalainen@esimerkki.com");
			
			customerRepository.save(customer1);


			Customer customer2 = new Customer();
			customer2.setName("Liisa Laine");
			customer2.setEmail("liisa.laine@esimerkki.com");
			
			customerRepository.save(customer2);

			Customer customer3 = new Customer();
			customer3.setName("Tarja Halonen");
			customer3.setEmail("tarja.halonen@esimerkki.com");
			
			customerRepository.save(customer3);

			Customer customer4 = new Customer();
			customer4.setName("Sauli Niinisto");
			customer4.setEmail("sauli.niinisto@esimerkki.com");
			
			customerRepository.save(customer4);

			Customer customer5 = new Customer();
			customer5.setName("Martti Ahtisaari");
			customer5.setEmail("martti.ahtisaarin@esimerkki.com");
			
			customerRepository.save(customer5);

			customerRepository.findAll().toString();
		
		};
	}
}
