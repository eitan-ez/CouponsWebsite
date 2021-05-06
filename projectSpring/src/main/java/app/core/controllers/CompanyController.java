package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.exceptions.ServiceException;
import app.core.services.CompanyService;

@RestController
@RequestMapping("/api/company")
public class CompanyController extends ClientController {

	@Autowired
	private CompanyService service;

	@PostMapping("/add")
//	TODO return type and Exception type
	public void addCoupon(@RequestBody Coupon coupon) {
		try {
			service.addNewCoupon(coupon);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PutMapping("/update")
	public Coupon updateCoupon(@RequestBody Coupon coupon, @RequestBody int id) {
		try {
			return service.updateCoupon(coupon, id);
		} catch (ServiceException e) { // in case of exception from our method
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (Exception e) { //in case of any other exeption
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@DeleteMapping("/delete")
	public void deleteCoupon (@RequestParam int couponId) {
		try {
			service.deleteCoupon(couponId);
			
		} catch (ServiceException e) {// in case of exception from our method 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	
	

}
