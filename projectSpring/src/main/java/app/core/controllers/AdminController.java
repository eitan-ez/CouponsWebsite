package app.core.controllers;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.ControllerException;
import app.core.exceptions.CouponSystemException;
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
			String token =  jwtUtil.generateToken(userDetails);
		}

//        	TODO
		return null;
	}

	@PostMapping("/add-company")
	public void addCompany(@RequestParam String jwt, @RequestBody Company company) throws Exception {
		try {
			jwtValidation(jwt);
			service.addNewCompany(company);

		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/update-company")
	public void updateCompany(@RequestParam String jwt, @RequestBody Company company) {
		try {
			jwtValidation(jwt);
			service.updateCompany(company.getId(), company);

		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete-company")
	public void deleteCompany(@RequestParam String jwt, @RequestBody int companyId) {
		try {
			jwtValidation(jwt);
			service.deleteCompany(companyId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-all-companies")
	public List<Company> getAllCompanies(@RequestParam String jwt) {
		try {
			jwtValidation(jwt);
			return service.getAllCompanies();
		} catch (ControllerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-one-company")
	public Company getOneCompany(@RequestParam String jwt, @RequestBody int companyId) {
		try {
			jwtValidation(jwt);
			return service.getOneCompany(companyId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/add-customer")
	public void addCustomer(@RequestParam String jwt, @RequestBody Customer customer) {
		try {
			jwtValidation(jwt);
			service.addNewCustomer(customer);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/update-customer")
	public void updateCustomer(@RequestParam String jwt, @RequestBody Customer customer) {
		try {
			jwtValidation(jwt);
			service.updateCustomer(customer.getId(), customer);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete-customer")
	public void deleteCustomer(@RequestParam String jwt, @RequestBody int customerId) {
		try {
			jwtValidation(jwt);
			service.deleteCustomer(customerId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-all-customers")
	public List<Customer> getAllCustomers(@RequestParam String jwt) {
		try {
			jwtValidation(jwt);
			return service.getAllCustomers();
		} catch (ControllerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-one-customer")
	public Customer getOneCustomer(@RequestParam String jwt, @RequestBody int customerId) {
		try {
			jwtValidation(jwt);
			return service.getOneCustomer(customerId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public void jwtValidation(String jwt) throws ControllerException {
		try {
			jwtUtil.extractAllClaims(jwt);
		} catch (ExpiredJwtException e) {
			throw new ControllerException("You are not logged in. Please log in");
		}

	}
}
