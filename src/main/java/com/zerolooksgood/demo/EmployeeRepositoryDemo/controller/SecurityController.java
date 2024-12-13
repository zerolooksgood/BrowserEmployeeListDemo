package com.zerolooksgood.demo.EmployeeRepositoryDemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SecurityController {

    @GetMapping("/")
    public String index() {
        return "redirect:/employees/list"; //Redirects from root to the "home" page
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model theModel) {
        //This method sends custom title and body to the html file because the different error pages use the same form
        theModel.addAttribute("header", "Access Denied");
        theModel.addAttribute("body", "You lack the sufficient permission to access this service, if you believe this to be a mistake then please contact the service administrator.");

        return "error-pages/error-page";
    }

    @GetMapping("/employee-does-not-exist")
    public String employeeDoesNotExist(Model theModel) {

        theModel.addAttribute("header", "Couldn't Find Employee");
        theModel.addAttribute("body", "The service couldn't retrieve any information about the employee you're looking for, please try again or contact the service administrator.");

        return "error-pages/error-page";
    }

    @GetMapping("/showLoginForm") //Spring security will automatically send the player to this method if their session isn't authorised
    public String showLoginForm() {
        return "security/login";
    }
}
