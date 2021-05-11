package app.core.controllers;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.ServiceException;
import app.core.services.AdminService;
import app.core.utils.JwtGenerate;
import app.core.utils.JwtGenerate.UserDetails;
import app.core.utils.JwtGenerate.UserDetails.UserType;
import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService service;
	@Autowired
	private JwtGenerate jwtUtil;

	@GetMapping("/login")
	public String login(String email, String password) {
		if (service.login(email, password)) {
			UserDetails userDetails = new UserDetails("0", email, password, UserType.ADMIN);
			return jwtUtil.generateToken(userDetails);
		}

//        	TODO
		return null;
	}

	@PostMapping("/add-company")
	public void addCompany(@RequestParam String jwt, @RequestBody Company company) throws Exception {
		try {
			if (jwtValidation(jwt)) {
				service.addNewCompany(company);
				return;
			}
			throw new Exception();
		} catch (ServiceException e) {
			e.printStackTrace();
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/update-company")
	public void updateCompany(@RequestParam String jwt, @RequestBody Company company) {
		try {
			service.updateCompany(company.getId(), company);
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete-company")
	public void deleteCompany(@RequestParam String jwt, @RequestBody int companyId) {
		try {
			service.deleteCompany(companyId);
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-all-companies")
	public List<Company> getAllCompanies(@RequestParam String jwt) {
		return service.getAllCompanies();
	}

	@GetMapping("/get-one-company")
	public Company getOneCompany(@RequestBody int companyId) {
		try {
			return service.getOneCompany(companyId);
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/add-customer")
	public void addCustomer(@RequestBody Customer customer) {
		try {
			service.addNewCustomer(customer);
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/update-customer")
	public void updateCustomer(@RequestBody Customer customer) {
		try {
			service.updateCustomer(customer.getId(), customer);
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete-customer")
	public void deleteCustomer(@RequestBody int customerId) {
		try {
			service.deleteCustomer(customerId);
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-all-customers")
	public List getAllCustomers() {
		return service.getAllCustomers();
	}

	@GetMapping("/get-one-customer")
	public Customer getOneCustomer(@RequestBody int customerId) {
		try {
			
			return service.getOneCustomer(customerId);
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public boolean jwtValidation(String jwt) {
		try {
			jwtUtil.extractAllClaims(jwt);
			return true;
		} catch (ExpiredJwtException e) {
			return false;
		}

	}
}
