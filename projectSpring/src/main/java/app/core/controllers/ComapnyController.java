package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.exceptions.ServiceException;
import app.core.services.CompanyService;

@RestController
@RequestMapping("/api/company")
public class ComapnyController {

	@Autowired
	private CompanyService comService;

//	TODO
	public boolean login(String email, String password) {
		return false;
	}

	@PostMapping("/add")
//	TODO return type and Exception type
	public void addCoupon(@RequestBody Coupon coupon) {
		try {
			comService.addNewCoupon(coupon);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PutMapping("/update")
	public Coupon updateCoupon(@RequestBody Coupon coupon, @RequestBody int id) {
		try {
			return comService.updateCoupon(coupon, id);
		} catch (ServiceException e) { // in case of exception from our method
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (Exception e) { //in case of any other exeption
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@DeleteMapping("delete")
	public void deleteCoupon (@RequestParam int couponId) {
		try {
			comService.deleteCoupon(couponId);
			
		} catch (ServiceException e) {// in case of exception from our method 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	
	

}
