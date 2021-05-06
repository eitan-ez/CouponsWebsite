package app.core.controllers;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.ServiceException;
import app.core.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController  {

    @Autowired
    private AdminService service;

    @GetMapping("/login")
    public boolean login(String email, String password) {
        if (service.login(email, password))
            return true;
        return false;
    }
    
    @PostMapping("/add-company")
    public void addCompany(@RequestBody Company company) {
        try {
            service.addNewCompany(company);
        } catch (ServiceException e) {
        	e.printStackTrace();
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update-company")
    public void updateCompany(@RequestBody Company company) {
        try {
            service.updateCompany(company.getId(), company);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete-company")
    public void deleteCompany(@RequestBody int companyId) {
        try {
            service.deleteCompany(companyId);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/get-all-companies")
    public List<Company> getAllCompanies() {
        return service.getAllCompanies();
    }

    @GetMapping("/get-one-company")
    public Company getOneCompany(@RequestBody int companyId) {
        try {
            return service.getOneCompany(companyId);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/add-customer")
    public void addCustomer(@RequestBody Customer customer) {
        try {
            service.addNewCustomer(customer);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update-customer")
    public void updateCustomer(@RequestBody Customer customer) {
        try {
            service.updateCustomer(customer.getId(), customer);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete-customer")
    public void deleteCustomer(@RequestBody int customerId) {
        try {
            service.deleteCustomer(customerId);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/get-all-customers")
    public List getAllCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("/get-one-customer")
    public Customer getOneCustomer(@RequestBody int customerId) {
        try {
            return service.getOneCustomer(customerId);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
