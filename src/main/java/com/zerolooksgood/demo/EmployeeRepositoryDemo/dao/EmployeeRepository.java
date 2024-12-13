package com.zerolooksgood.demo.EmployeeRepositoryDemo.dao;

import com.zerolooksgood.demo.EmployeeRepositoryDemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> { //Utilizes JPA-Repository's built in functionality to communicate with the database
    public List<Employee> findAllByOrderByIdAsc(); //This code activates a built-in method that outputs all the employees sorted by id
}
