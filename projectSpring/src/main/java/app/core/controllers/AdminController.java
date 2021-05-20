package app.core.controllers;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.ControllerException;
import app.core.exceptions.CouponSystemException;
import app.core.services.AdminService;
import app.core.utils.JwtGenerate;
import app.core.utils.JwtGenerate.CredntialsDetails;
import app.core.utils.JwtGenerate.UserDetails;
import app.core.utils.JwtGenerate.UserDetails.UserType;
import io.jsonwebtoken.ExpiredJwtException;
import springfox.documentation.spring.web.json.Json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;

import javax.websocket.server.PathParam;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService service;
	@Autowired
	private JwtGenerate jwtUtil;

	@PostMapping("/login")
	public UserDetails login(@RequestBody CredntialsDetails credntialsDetails) {
		if (service.login(credntialsDetails.email, credntialsDetails.password)) {
			UserDetails user = new UserDetails("0", credntialsDetails.email, credntialsDetails.password,
					UserType.ADMIN);
			String token = jwtUtil.generateToken(user);
			user.token = token;
			return user;
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "login details incorrect");

	}

	@PostMapping("/add-company")
	public void addCompany(@RequestHeader String jwt, @RequestBody Company company) throws CouponSystemException {
		try {
			jwtValidation(jwt);
			service.addNewCompany(company);
		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/update-company")
	public void updateCompany(@RequestHeader String jwt, @RequestBody Company company) {
		try {
			jwtValidation(jwt);
			service.updateCompany(company.getId(), company);

		} catch (CouponSystemException e) {
			System.out.println("got here" + e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete-company/{id}")
	public void deleteCompany(@RequestHeader String jwt,@PathVariable int id) {
		try {
			jwtValidation(jwt);
			service.deleteCompany(id);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-all-companies")
	public List<Company> getAllCompanies(@RequestHeader String jwt) {
		try {
			jwtValidation(jwt);
			return service.getAllCompanies();
		} catch (ControllerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-one-company")
	public Company getOneCompany(@RequestHeader String jwt, @RequestBody int companyId) {
		try {
			jwtValidation(jwt);
			return service.getOneCompany(companyId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/add-customer")
	public void addCustomer(@RequestHeader String jwt, @RequestBody Customer customer) {
		try {
			jwtValidation(jwt);
			service.addNewCustomer(customer);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/update-customer")
	public void updateCustomer(@RequestHeader String jwt, @RequestBody Customer customer) {
		try {
			jwtValidation(jwt);
			service.updateCustomer(customer.getId(), customer);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete-customer")
	public void deleteCustomer(@RequestHeader String jwt, @RequestBody int customerId) {
		try {
			jwtValidation(jwt);
			service.deleteCustomer(customerId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-all-customers")
	public List<Customer> getAllCustomers(@RequestHeader String jwt) {
		try {
			jwtValidation(jwt);
			return service.getAllCustomers();
		} catch (ControllerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-one-customer")
	public Customer getOneCustomer(@RequestHeader String jwt, @RequestBody int customerId) {
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
