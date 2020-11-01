package org.mma.training.java.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootHibernateUserAddressMappingApplication implements CommandLineRunner{

//	@Autowired
//	UserRepository UserRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootHibernateUserAddressMappingApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
//		Address address = new Address("7185 Shady Palm Dr.", "", "Springfield", "VA", "22153", "");
//		User user = new User("Tausif", "R", "Ahsan", "(571)999-9999", "tausif_ahsan@yahoo.com", address);
		
		//UserRepository.save(user);
		 
		
	}

	
}
