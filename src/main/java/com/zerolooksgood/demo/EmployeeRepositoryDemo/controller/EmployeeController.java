package com.zerolooksgood.demo.EmployeeRepositoryDemo.controller;

import com.zerolooksgood.demo.EmployeeRepositoryDemo.entity.Employee;
import com.zerolooksgood.demo.EmployeeRepositoryDemo.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller //Defines this class as a controller
@RequestMapping("/employees") //This defines the request mapping for this controller, this is not required, but is recommended to differentiate the different controllers
public class EmployeeController {

    private EmployeeService employeeService; //Defines employeeServices so that the class can use it

    public EmployeeController(EmployeeService theEmployeeService) { //Constructs the class, spring automatically provides the class with EmployeeService
        employeeService = theEmployeeService;
    }

    @GetMapping("/list") //Defines the GetMapping for this method as /list, this mapping also only works with GET requests
    public String list(Model theModel) { //Adds the model as a parameter to the method, this allows for transfer of data between the method and the html file

        List<Employee> employees = employeeService.findAll(); //Uses the function from EmployeeService to retrieve all the information in the database
        employees.remove(0); //Hides the first user because this user is only used for testing purposes

        theModel.addAttribute("employees", employees); //Adds the list of employees to the model. The first parameter is the attribute name, and the second parameter is the actual data stored in the attribute

        return "employees/list-employees"; //Returns an HTML file. Found under src/main/resources/templates
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        Employee theEmployee = new Employee(); //Creates a new empty employee object which the user will fill in

        theModel.addAttribute("employee", theEmployee); //Adds the empty object to the model
        theModel.addAttribute("header", "Save New Employee"); //Sends the custom title to the form, this is because both the edit and save function use the same html file

        return "employees/employee-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int theId, Model theModel) { //Looks for a parameter appended to the url and converts it to an integer

        if (!employeeService.exists(theId)) { //Checks if the employee exists
            return "redirect:/employee-does-not-exist"; //Returns custom error page if the employee doesn't exist
        }

        Employee theEmployee = employeeService.findById(theId); //Extracts the employee from the database

        theModel.addAttribute("employee", theEmployee);
        theModel.addAttribute("header", "Edit Employee Information");

        return "employees/employee-form";
    }
    //Note that this page doesn't actually contain the code required to delete anyone from the database
    @GetMapping("/showFormForDelete")
    public String showFormForDelete(@RequestParam("employeeId") int theId, Model theModel) {

        if (!employeeService.exists(theId)) {
            return "redirect:/employee-does-not-exist";
        }

        Employee theEmployee = employeeService.findById(theId);

        theModel.addAttribute("employee", theEmployee);

        return "employees/delete-form";
    }

    @PostMapping("/delete") //This method actually deletes the employee, it's also a post mapping, meaning that it can only be accessed by POST requests
    public String deleteEmployee(@ModelAttribute("employee") Employee theEmployee) { //This method actually takes the entire object instead of an id as the parameter

        employeeService.deleteById(theEmployee.getId()); //Deletes the employee from the database

        return "redirect:/employees/list"; //Redirects the user to the home page.
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {

        employeeService.save(theEmployee); //Saves the employee to the database

        return "redirect:/employees/list";
    }
}
