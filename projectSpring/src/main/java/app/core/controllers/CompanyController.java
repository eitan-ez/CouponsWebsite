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
		if (service.login(credntialsDetails.email, credntialsDetails.password)) {
			String id = String.valueOf(service.getCompanyIdFromDB(credntialsDetails.email, credntialsDetails.password));
			UserDetails user = new UserDetails(id, credntialsDetails.email, credntialsDetails.password,
					UserType.COMPANY);
			user.token = jwtUtil.generateToken(user);
			return user;
		}

		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "login detailes are wrong. please try again");

	}

	@PostMapping("/add")
	public void addCoupon(@RequestHeader String jwt, @RequestBody Coupon coupon) {
		try {
			int id = getIdFromJwt(jwt);
			service.addNewCoupon(coupon, id);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/update")
	public Coupon updateCoupon(@RequestHeader String jwt, @RequestBody Coupon coupon) {
		try {
			int companyId = getIdFromJwt(jwt);
			return service.updateCoupon(coupon, coupon.getId(), companyId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete/{couponId}")
	public void deleteCoupon(@RequestHeader String jwt, @PathVariable int couponId) {
		try {
			int id = getIdFromJwt(jwt);
			service.deleteCoupon(id, couponId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-coupons")
	public List<Coupon> getCompanyCoupons(@RequestHeader String jwt) {
		int id;
		try {
			id = getIdFromJwt(jwt);
			return service.getCoupons(id);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-coupons-by-category/{category}")
	public List<Coupon> getCouponsByCategory(@RequestHeader String jwt, @PathVariable Category category) {
		try {
			int id = getIdFromJwt(jwt);
			return service.getCouponsByCategory(id, category);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get-coupons-by-price/{maxPrice}")
	public List<Coupon> getCouponsByMaxPrice(@RequestHeader String jwt, @PathVariable String maxPrice) {
		try {
			int id = getIdFromJwt(jwt);
			double price = Double.parseDouble(maxPrice);
			return service.getCouponsByMaxPrice(id, price);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/company")
	public Company getCompanyDetails(@RequestHeader String jwt) {
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
