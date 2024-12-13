package com.zerolooksgood.demo.EmployeeRepositoryDemo.service;

import com.zerolooksgood.demo.EmployeeRepositoryDemo.dao.EmployeeRepository;
import com.zerolooksgood.demo.EmployeeRepositoryDemo.entity.Employee;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service //Defines the class as part of the service
public class EmployeeServiceImpl implements EmployeeService{

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl (EmployeeRepository theEmployeeRepository) { //Allows us to use the EmployeeRepository to communicate with the database
        this.employeeRepository = theEmployeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByIdAsc(); //Uses JPA-Rep's built in communications functions to extract the data from the database.
    }

    @Override
    public Employee findById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.get();
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee); //Once again uses JPA-Rep's built in methods
    }

    @Override
    public void deleteById(int id) {
        employeeRepository.deleteById(id); //Same here as the method above
    }

    @Override
    public boolean exists(int id) {
        Optional<Employee> temp = employeeRepository.findById(id); //Searches for the employee in the database by id and saves it as an optional variable (1);

        if (temp.isPresent()) { //Checks if there was anything saved to the optional variable
            return true;
        } else {
            return false;
        }
    }
}
