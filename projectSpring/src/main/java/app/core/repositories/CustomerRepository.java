package app.core.repositories;




import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Customer;
import org.springframework.stereotype.Repository;

@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	boolean existsCustomerByEmailAndPassword(String email, String password);

	boolean existsCustomerByEmail(String email);

	Customer findCustomerByEmailAndPassword(String email, String password);
	}
