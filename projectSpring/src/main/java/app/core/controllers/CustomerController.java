package app.core.controllers;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exceptions.ControllerException;
import app.core.exceptions.CouponSystemException;
import app.core.services.CustomerService;

import java.util.List;

import app.core.utils.JwtGenerate;
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

    @GetMapping("/login")
    public String login(@RequestBody String email, @RequestBody String password) {
        if (service.login(email, password)) {
            String id = String.valueOf(service.getCustomerIdFromDB(email, password));
            UserDetails userDetails = new UserDetails(id, email, password, UserDetails.UserType.CUSTOMER);
            return jwtUtil.generateToken(userDetails);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email or password are incorrect, please try again. ");
    }

    @PutMapping("/purchase-coupon")
    public void purchaseCoupon(@RequestParam String jwt, @RequestBody int couponId) {

        try {
            int id = getIdFromJwt(jwt);
            service.purchaseCoupon(id, couponId);
        } catch (CouponSystemException e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, e.getMessage());
        }
    }

    @GetMapping("/all-coupons")
    public List<Coupon> getCoupons(@RequestParam String jwt) {
        try {
            int id = getIdFromJwt(jwt);
            return service.getCoupons(id);
        } catch (CouponSystemException e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, e.getMessage());
        }
    }

    @GetMapping("/coupons-by-category")
    public List<Coupon> getCouponsByCategory(@RequestParam String jwt, @RequestBody Category category) {
        try {
            int id = getIdFromJwt(jwt);
            return service.getCouponsByCategory(id, category);
        } catch (ControllerException e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, e.getMessage());
        }
    }

    @GetMapping("/coupons-by-price")
    public List<Coupon> getCouponsByMaxPrice(@RequestParam String jwt, @RequestBody double maxPrice) {
        try {
            int id = getIdFromJwt(jwt);
            return service.getCouponsByMaxPrice(id, maxPrice);
        } catch (ControllerException e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, e.getMessage());
        }
    }

    @GetMapping("/customer")
    public Customer getCustomer(@RequestParam String jwt) {
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
