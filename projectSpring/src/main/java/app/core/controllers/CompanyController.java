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

@RestController
@RequestMapping("/api/company")
public class CompanyController extends ClientController {

	@Autowired
	private CompanyService service;

	
	public CompanyController() {
	}

	
	
	@PostMapping("/add")
//	TODO return type and Exception type
	public void addCoupon(@RequestBody Coupon coupon) {
			try {
				service.addNewCoupon(coupon);
			} catch (ServiceException e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
	}

	@PutMapping("/update")
	public Coupon updateCoupon(@RequestBody Coupon coupon, @RequestBody int id) {
		try {
			return service.updateCoupon(coupon, id);
		} catch (ServiceException e) { // in case of exception from our method
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@DeleteMapping("/delete")
	public void deleteCoupon (@RequestBody int couponId) {
		try {
			service.deleteCoupon(couponId);
		} catch (ServiceException e) {// in case of exception from our method 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/get-coupons")
	public List<Coupon> getCompanyCoupons () {
		return service.getCoupons();
	}

	@GetMapping("/get-coupons-by-category")
	public List<Coupon> getCouponsByCategory(@RequestBody Category category){
		return service.getCouponsByCategory(category);
	}
	
	@GetMapping("/get-coupons-by-price")
	public List<Coupon> getCouponsByCategory(@RequestBody double maxPrice){
		return service.getCouponsByMaxPrice(maxPrice);
	}
	
	@GetMapping("/company")
	public Company getCompanyDetails () {
		try {
			return this.service.getCompanyDetails();
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	
}
