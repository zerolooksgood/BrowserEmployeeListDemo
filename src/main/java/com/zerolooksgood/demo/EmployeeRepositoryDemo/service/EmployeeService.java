package com.zerolooksgood.demo.EmployeeRepositoryDemo.service;

import com.zerolooksgood.demo.EmployeeRepositoryDemo.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();
    Employee findById(int id);
    Employee save(Employee employee);
    void deleteById(int id);
}
