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
public class AdminController extends ClientController {

    @Autowired
    private AdminService service;

    @PostMapping("/add-company")
    public void addCompany(@RequestHeader String token, @RequestBody Company company) {
        try {
            service.addNewCompany(company);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update-company")
    public void updateCompany(@RequestHeader String token, @RequestBody Company company) {
        try {
            service.updateCompany(company.getId(), company);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete-company")
    public void deleteCompany(@RequestHeader String token, @RequestBody int companyId) {
        try {
            service.deleteCompany(companyId);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/get-all-companies")
    public List<Company> getAllCompanies(@RequestHeader String token) {
        return service.getAllCompanies();
    }

    @GetMapping("/get-one-company")
    public Company getOneCompany(@RequestHeader String token, @RequestBody int companyId) {
        try {
            return service.getOneCompany(companyId);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/add-customer")
    public void addCustomer(@RequestHeader String token, @RequestBody Customer customer) {
        try {
            service.addNewCustomer(customer);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update-customer")
    public void updateCustomer(@RequestHeader String token, @RequestBody Customer customer) {
        try {
            service.updateCustomer(customer.getId(), customer);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete-customer")
    public void deleteCustomer(@RequestHeader String token, @RequestBody int customerId) {
        try {
            service.deleteCustomer(customerId);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/get-all-customers")
    public List getAllCustomers(@RequestHeader String token) {
        return service.getAllCustomers();
    }

    @GetMapping("/get-one-customer")
    public Customer getOneCustomer(@RequestHeader String token, @RequestBody int customerId) {
        try {
            return service.getOneCustomer(customerId);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
