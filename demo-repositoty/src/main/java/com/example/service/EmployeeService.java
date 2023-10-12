package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Employee;
import com.example.repository.EmployeeRepository;

import java.util.List;
//cho các phương thức khác
import java.time.ZonedDateTime;
import java.util.Collection;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id);
    }

    public void newEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public void update(Employee employee) {
        employeeRepository.update(employee);
    }

    public void delete(Long id) {
        employeeRepository.delete(id);
    }
    
    public List<Employee> searchEmployees(String searchTerm){
    	return employeeRepository.searchEmployees(searchTerm);
    }
    /*================================================= START :Truy vấn Khác()====================================================== */
    
    /*================================================= END :Truy vấn Khác()====================================================== */
}
