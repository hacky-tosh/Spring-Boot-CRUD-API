package com.example.controller;


import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.repository.EmployeeRepository;
import com.example.model.Employee;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class EmployeeController {


    @Autowired
    private EmployeeRepository employeeRepository;

//    post emp data
    @PostMapping("/employees")
    public String createEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
        return "Employee created successfully";

    }

    // get all emp data
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try{
            List<Employee> employees = new ArrayList<Employee>();
            employeeRepository.findAll().forEach(employees::add);
            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    // get emp by id
    @GetMapping("/employees/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }

    // update emp by id put methode

    @PutMapping("/employees/{id}")
    public ResponseEntity<String> updateEmployeeByIdPut(@PathVariable long id, @RequestBody Employee employee){

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()){
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setName(employee.getName());
            existingEmployee.setSalary(employee.getSalary());
            existingEmployee.setAge(employee.getAge());
            existingEmployee.setCity(employee.getCity());
            employeeRepository.save(existingEmployee);
            return ResponseEntity.ok("Employee updated successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

    }


    // update emp by id patch methode
    @PatchMapping("/employees/{id}")
    public ResponseEntity<String> updateEmployeeByIdPatch(@PathVariable long id, @RequestBody Employee employee){

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()){
            Employee existingEmployee = optionalEmployee.get();
            if (employee.getName() != null) {
                existingEmployee.setName(employee.getName());
            }
            if (employee.getSalary() != 0) {
                existingEmployee.setSalary(employee.getSalary());
            }
            if (employee.getAge() != 0) {
                existingEmployee.setAge(employee.getAge());
            }
            if (employee.getCity() != null) {
                existingEmployee.setCity(employee.getCity());
            }
            employeeRepository.save(existingEmployee);
            return ResponseEntity.ok("Employee updated successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

    }

    // delete emp by id
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable long id) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            if (optionalEmployee.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
            }
            employeeRepository.deleteById(id);

            return ResponseEntity.ok("Employee deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }

    // delete all emp
    @DeleteMapping("/employees")
    public ResponseEntity<String> deleteAllEmployees() {
        try {
            employeeRepository.deleteAll();
            return ResponseEntity.ok("All Employees deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting employees");
        }
    }

    // search by keywords
    @GetMapping("/employees/empcity")
    public ResponseEntity<List<Employee>> getEmployeeByCity(@RequestParam("city") String city) {
        try {
            System.out.println("city: " + city);
            List<Employee> employees = employeeRepository.findByCity(city);
            if (employees.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




}
