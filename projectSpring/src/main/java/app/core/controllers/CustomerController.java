package app.core.controllers;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exceptions.ControllerException;
import app.core.exceptions.CouponSystemException;
import app.core.services.CustomerService;

import java.util.List;

import javax.annotation.security.PermitAll;

import app.core.utils.JwtGenerate;
import app.core.utils.JwtGenerate.CredntialsDetails;
import app.core.utils.JwtGenerate.UserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	private CustomerService service;
	@Autowired
	private JwtGenerate jwtUtil;

	@PostMapping("/login")
	public UserDetails login(@RequestBody CredntialsDetails credntialsDetails) {
		if (service.login(credntialsDetails.email, credntialsDetails.password)) {
			String id = String
					.valueOf(service.getCustomerIdFromDB(credntialsDetails.email, credntialsDetails.password));
			UserDetails user = new UserDetails(id, credntialsDetails.email, credntialsDetails.password,
					UserDetails.UserType.CUSTOMER);
			user.token = jwtUtil.generateToken(user);
			return user;
		}
		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email or password are incorrect. please try again.");
	}

	@PutMapping("/purchase-coupon/{couponId}")
	public void purchaseCoupon(@RequestHeader String jwt, @PathVariable int couponId) {

		try {
			int id = getIdFromJwt(jwt);
//			int idCoupon = Integer.parseInt(couponId);
			service.purchaseCoupon(id, couponId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/all-coupons")
	public List<Coupon> getCoupons(@RequestHeader String jwt) {
		try {
			int id = getIdFromJwt(jwt);
			return service.getCoupons(id);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/coupons-by-category/{category}")
	public List<Coupon> getCouponsByCategory(@RequestHeader String jwt, @PathVariable Category category) {
		try {
			int id = getIdFromJwt(jwt);
			return service.getCouponsByCategory(id, category);
		} catch (ControllerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/coupons-by-price/{maxPrice}")
	public List<Coupon> getCouponsByMaxPrice(@RequestHeader String jwt, @PathVariable String maxPrice) {
		try {
			int id = getIdFromJwt(jwt);
			double price = Double.parseDouble(maxPrice);
			return service.getCouponsByMaxPrice(id, price);
			
		} catch (ControllerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/customer")
	public Customer getCustomer(@RequestHeader String jwt) {
		try {
			int id = getIdFromJwt(jwt);
			return service.getCustomerDetails(id);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, e.getMessage());
		}
	}

	/**
	 * @param jwt - validate the Jwt and
	 * @return the id as int
	 * @throws ResponseStatusException
	 */
	public int getIdFromJwt(String jwt) throws ControllerException {
		try {
			return Integer.parseInt(jwtUtil.extractID(jwt));
		} catch (ExpiredJwtException e) {
			throw new ControllerException("Something went terribly wrong :( , try logging in again. ");
		}
	}

}
