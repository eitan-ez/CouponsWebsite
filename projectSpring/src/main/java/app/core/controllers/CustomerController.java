package app.core.controllers;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exceptions.ServiceException;
import app.core.services.CustomerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/customer")
public class CustomerController extends ClientController {

    @Autowired
    private CustomerService service;

    @PutMapping("/purchase-coupon")
    public void purchaseCoupon(@RequestHeader String token, int couponId) {
    	try {
			service.purchaseCoupon(couponId);
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
    }
    
    @GetMapping("all-coupons")
    public List<Coupon> getCoupons(@RequestHeader String token) {
    	return service.getCoupons();
    }
    
    @GetMapping("coupons-by-price")
    public List<Coupon> getCouponsByCategory (@RequestHeader String token, @RequestBody Category category){
    	return service.getCouponsByCategory(category);
    }
    
    @GetMapping("coupons-by-price")
    public List<Coupon> getCouponsByMaxPrice (@RequestHeader String token, @RequestBody double maxPrice){
    	return service.getCouponsByMaxPrice(maxPrice);
    }
    
    @GetMapping("customer")
    public Customer getCustomer(@RequestHeader String token) {
    	try {
			return service.getCustomerDetails();
		} catch (ServiceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
    }
    
}
