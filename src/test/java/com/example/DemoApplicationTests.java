package com.example;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.controller.EmployeeController;
import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DemoApplicationTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void testCreateEmployee() {
        // Create a sample employee
        Employee employee = new Employee(1, "John Doe", 50000, 30, "New York");

        // Mocking the save method of employeeRepository
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Call the method to be tested
        String result = employeeController.createEmployee(employee);

        // Verify that the save method was called with the correct argument
        verify(employeeRepository, times(1)).save(employee);

        // Check the returned message
        assertEquals("Employee created successfully", result);
    }

    @Test
    void testGetAllEmployees() {
        // Create sample employees
        Employee employee1 = new Employee(1, "John Doe", 50000, 30, "New York");
        Employee employee2 = new Employee(2, "Jane Smith", 60000, 35, "Los Angeles");
        List<Employee> employeeList = Arrays.asList(employee1, employee2);

        // Mocking the findAll method of employeeRepository
        when(employeeRepository.findAll()).thenReturn(employeeList);

        // Call the method to be tested
        ResponseEntity<List<Employee>> responseEntity = employeeController.getAllEmployees();

        // Verify that the findAll method was called
        verify(employeeRepository, times(1)).findAll();

        // Check the HTTP status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Check the returned employee list
        List<Employee> returnedEmployees = responseEntity.getBody();
        assertNotNull(returnedEmployees);
        assertEquals(2, returnedEmployees.size());
        assertEquals(employee1, returnedEmployees.get(0));
        assertEquals(employee2, returnedEmployees.get(1));
    }



    @Test
    void testGetEmployeeById() {
        // Create a sample employee
        Employee employee = new Employee(1, "John Doe", 50000, 30, "New York");

        // Mocking the findById method of employeeRepository
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Call the method to be tested
        ResponseEntity<?> responseEntity = employeeController.getEmployeeById(1);

        // Verify that the findById method was called with the correct argument
        verify(employeeRepository, times(1)).findById(1L);

        // Check the HTTP status code and returned employee
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(employee, responseEntity.getBody());
    }
}
