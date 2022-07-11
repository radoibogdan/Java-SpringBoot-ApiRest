package com.bogdan.service;

import com.bogdan.exception_handler.EmployeeNotFoundException;
import com.bogdan.model.Department;
import com.bogdan.model.DepartmentRepository;
import com.bogdan.model.Employee;
import com.bogdan.model.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class EmployeeRepoController {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    // ---------------- GET ALL ----------------
    @GetMapping("/jpa/employees")
    public List<Employee> getAllJpa() {
        return employeeRepository.findAll();
    }

    // ---------------- GET BY ID - HATEOAS ----------------
    @GetMapping("/jpa/employees/{employeeId}/hateoas")
    public EntityModel<Employee> getEmployeeByIdJpaHateoas(@PathVariable Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        if (null == employee)
            throw new EmployeeNotFoundException("Employee Not Found.");
        EntityModel<Employee> model = EntityModel.of(employee);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllJpa()).withRel("all-employees-jpa");
        model.add(link);
        return model;
    }

    // ---------------- GET BY ID - NO HATEOAS ----------------
    @GetMapping("jpa/employees/{employeeId}")
    public Employee getEmployeeByIdJpa(@PathVariable Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        if (null == employee) {
            throw new EmployeeNotFoundException("Employee Not Found.");
        }
        return employee;
    }

    // ---------------- POST - Create Employee ----------------
    // Enable Validation with @Valid
    @PostMapping("/jpa/employees")
    public ResponseEntity<Object> saveEmployeeJpa(@Valid @RequestBody Employee emp) {
        Employee newEmployee = employeeRepository.save(emp);
        // modify Header Localion URL to employees/user/4
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{employeeId}") // variable name for newEmployee.getEmployeeId
                .buildAndExpand(newEmployee.getId())
                .toUri();
        return ResponseEntity.created(uri).build(); // created = attach code 202 to response
    }

    // ---------------- POST - Create Department for Employee----------------
    // Enable Validation with @Valid
    @PostMapping("/jpa/add-departement/employees/{empId}")
    public ResponseEntity<Object> saveDepartementForEmployeeJpa(@PathVariable Long empId, @RequestBody Department department) {
        Employee employee = employeeRepository.findById(empId).get();
        if (null == employee) {
            throw new EmployeeNotFoundException("Employee Not Found.");
        }
        department.setEmployee(employee);
        departmentRepository.save(department);
        // modify Header Localion URL to employees/user/4
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{employeeId}") // variable name for employee.getEmployeeId
                .buildAndExpand(employee.getId())
                .toUri();
        return ResponseEntity.created(uri).build(); // created = attach code 202 to response
    }

    // ---------------- DELETE Employee ----------------
    @DeleteMapping("/jpa/employees/{employeeId}")
    public void deleteEmployeeJpa(@PathVariable Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        if (null == employee) {
            throw new EmployeeNotFoundException("Employee Not Found.");
        }
        employeeRepository.delete(employee);
    }
}
