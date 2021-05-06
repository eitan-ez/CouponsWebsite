package app.core.controllers;

import app.core.services.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public abstract class ClientController {

    protected ClientService service;

    @GetMapping("/login")
    public boolean login(String email, String password) {
        if (service.login(email, password))
            return true;
        return false;
    }

}
