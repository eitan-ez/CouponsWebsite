package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.exceptions.ControllerException;
import app.core.exceptions.CouponSystemException;
import app.core.services.CompanyService;
import app.core.utils.JwtGenerate;
import app.core.utils.JwtGenerate.CredntialsDetails;
import app.core.utils.JwtGenerate.UserDetails;
import app.core.utils.JwtGenerate.UserDetails.UserType;
import io.jsonwebtoken.ExpiredJwtException;

@CrossOrigin
@RestController
@RequestMapping("/api/company")
public class CompanyController {

	@Autowired
	private CompanyService service;
	@Autowired
	private JwtGenerate jwtUtil;

	@PostMapping("/login")
	public UserDetails login(@RequestBody CredntialsDetails credntialsDetails) {
		System.out.println(credntialsDetails.email + "," + credntialsDetails.password);
		if (service.login(credntialsDetails.email, credntialsDetails.password)) {
			String id = String.valueOf(service.getCompanyIdFromDB(credntialsDetails.email, credntialsDetails.password));
			UserDetails user = new UserDetails(id, credntialsDetails.email, credntialsDetails.password, UserType.COMPANY);
			user.token = jwtUtil.generateToken(user);
			return user;
		}

		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "login detailes are wrong. please try again");

	}

	@PostMapping("/add")
	public void addCoupon(@RequestParam String jwt, @RequestBody Coupon coupon) {
		try {
			int id = getIdFromJwt(jwt);
			service.addNewCoupon(coupon, id);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User aren't logged in. Please log in");
		}
	}

	@PutMapping("/update")
	public Coupon updateCoupon(@RequestParam String jwt, @RequestBody Coupon coupon, @RequestBody int couponId) {
		try {
			int id = getIdFromJwt(jwt);
			return service.updateCoupon(coupon, couponId, id);
		} catch (CouponSystemException e) { // in case of exception from our method
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete")
	public void deleteCoupon(@RequestParam String jwt, @RequestBody int couponId) {
		try {
			int id = getIdFromJwt(jwt);
			service.deleteCoupon(id, couponId);
		} catch (CouponSystemException e) {// in case of exception from our method
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-coupons")
	public List<Coupon> getCompanyCoupons(@RequestParam String jwt) {
		int id;
		try {
			id = getIdFromJwt(jwt);
			return service.getCoupons(id);
		} catch (ControllerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-coupons-by-category")
	public List<Coupon> getCouponsByCategory(@RequestParam String jwt, @RequestBody Category category) {
		int id;
		try {
			id = getIdFromJwt(jwt);
		} catch (ControllerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return service.getCouponsByCategory(id, category);
	}

	@GetMapping("/get-coupons-by-price")
	public List<Coupon> getCouponsByMaxPrice(@RequestParam String jwt, @RequestBody double maxPrice) {
		int id;
		try {
			id = getIdFromJwt(jwt);
		} catch (ControllerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return service.getCouponsByMaxPrice(id, maxPrice);
	}

	@GetMapping("/company")
	public Company getCompanyDetails(@RequestParam String jwt) {
		try {
			int id = getIdFromJwt(jwt);
			return this.service.getCompanyDetails(id);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
			throw new ControllerException("User aren't logged in. Please log in");
		}
	}

}
