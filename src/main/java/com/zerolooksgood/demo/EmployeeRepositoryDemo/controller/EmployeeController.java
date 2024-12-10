package com.zerolooksgood.demo.EmployeeRepositoryDemo.controller;

import com.zerolooksgood.demo.EmployeeRepositoryDemo.entity.Employee;
import com.zerolooksgood.demo.EmployeeRepositoryDemo.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller //Defines this class as a controller (1)
@RequestMapping("/employees") //This defines the first part of the url you'll be using to see the application in your browser (2)
public class EmployeeController {

    private EmployeeService employeeService; //Defines employeeServices so that the class can use it

    public EmployeeController(EmployeeService theEmployeeService) { //Constructs the class, spring automatically provides the class with EmployeeService
        employeeService = theEmployeeService;
    }

    @GetMapping("/list") //Defines the GetMapping for this method as /list (3)
    public String list(Model theModel) {
        //For more information on Model see (4)
        List<Employee> employees = employeeService.findAll(); //Uses the function from EmployeeService to retrieve all the information in the database

        theModel.addAttribute("employees", employees); //Adds the list of employees to the model. The first parameter is the attribute name, and the second parameter is the actual data store in the attribute

        return "employees/list-employees"; //Returns a HTML file. Found under src/main/resources/templates
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        Employee theEmployee = new Employee(); //Creates a new empty employee object

        theModel.addAttribute("employee", theEmployee);
        theModel.addAttribute("header", "Save New Employee"); //Sends the custom title to the form, this is because both the edit and save function use the same html file

        return "employees/employee-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int theId, Model theModel) { //Looks for a parameter and converts it to an integer (5)

        Employee theEmployee = employeeService.findById(theId);

        theModel.addAttribute("employee", theEmployee);
        theModel.addAttribute("header", "Edit Employee Information");

        return "employees/employee-form";
    }
    //Note that this page doesn't actually contain the code required to delete anyone from the database
    @GetMapping("/showFormForDelete")
    public String showFormForDelete(@RequestParam("employeeId") int theId, Model theModel) {

        Employee theEmployee = employeeService.findById(theId);

        theModel.addAttribute("employee", theEmployee);

        return "employees/delete-form";
    }

    @PostMapping("/delete") //(6)
    public String deleteEmployee(@ModelAttribute("employee") Employee theEmployee) { //This method actually takes the entire object instead of an id as the parameter

        employeeService.deleteById(theEmployee.getId());

        return "redirect:/employees/list"; //Redirects the user to the home page.
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {

        employeeService.save(theEmployee);

        return "redirect:/employees/list";
    }
}
