package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.exceptions.ServiceException;
import app.core.services.CompanyService;
import app.core.utils.JwtGenerate;
import app.core.utils.JwtGenerate.UserDetails;
import app.core.utils.JwtGenerate.UserDetails.UserType;
import io.jsonwebtoken.ExpiredJwtException;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

	@Autowired
	private CompanyService service;
	@Autowired
	private JwtGenerate jwtUtil;

	public CompanyController() {
	}

	@GetMapping("/login")
	public String login(String email, String password) throws ResponseStatusException {
		if (service.login(email, password)) {
			String id = String.valueOf(service.getCompanyIdFromDB(email, password));
			UserDetails userDetails = new UserDetails(id, email, password, UserType.COMPANY);
			return jwtUtil.generateToken(userDetails);
		}

		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "login detailes are wrong. please try again");
		
	}

	@PostMapping("/add")
	public void addCoupon(@RequestParam String jwt, @RequestBody Coupon coupon) {
		try {
			int id = getIdFromJwt(jwt);
			service.addNewCoupon(coupon, id);
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (ExpiredJwtException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User aren't logged in. Please log in");
		}
	}

	@PutMapping("/update")
	public Coupon updateCoupon(@RequestParam String jwt, @RequestBody Coupon coupon, @RequestBody int couponId) {
		try {
			int id = getIdFromJwt(jwt);
			return service.updateCoupon(coupon, couponId, id);
		} catch (ServiceException e) { // in case of exception from our method
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete")
	public void deleteCoupon(@RequestParam String jwt, @RequestBody int couponId) {
		try {
			int id = getIdFromJwt(jwt);
			service.deleteCoupon(id, couponId);
		} catch (ServiceException e) {// in case of exception from our method
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-coupons")
	public List<Coupon> getCompanyCoupons(@RequestParam String jwt) {
		int id = getIdFromJwt(jwt);
		return service.getCoupons(id);
	}

	@GetMapping("/get-coupons-by-category")
	public List<Coupon> getCouponsByCategory(@RequestParam String jwt, @RequestBody Category category) {
		int id = getIdFromJwt(jwt);
		return service.getCouponsByCategory(id, category);
	}

	@GetMapping("/get-coupons-by-price")
	public List<Coupon> getCouponsByCategory(@RequestParam String jwt, @RequestBody double maxPrice) {
		int id = getIdFromJwt(jwt);
		return service.getCouponsByMaxPrice(id, maxPrice);
	}

	@GetMapping("/company")
	public Company getCompanyDetails(@RequestParam String jwt) {
		try {
			int id = getIdFromJwt(jwt);

			return this.service.getCompanyDetails(id);
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	public int getIdFromJwt(String jwt) {
		try {
			
			return Integer.parseInt(jwtUtil.extractID(jwt));
		} catch (ExpiredJwtException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User aren't logged in. Please log in");
		}
	}

}
