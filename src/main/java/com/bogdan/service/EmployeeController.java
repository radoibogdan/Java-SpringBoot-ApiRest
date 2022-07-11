package com.bogdan.service;

import com.bogdan.exception_handler.EmployeeNotFoundException;
import com.bogdan.model.Employee;
import com.bogdan.model.EmployeeDao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class EmployeeController {

    @Autowired
    EmployeeDao service;

    // ---------------- GET ALL ----------------
    @GetMapping("/employees")
    public List<Employee> getAll() {
        return service.getAllEmployees();
    }

    // ---------------- GET BY ID ----------------
    @Operation(summary = "Retourne un employée en utilisant son identifiant ")
    @ApiResponses(value = {
            @ApiResponse( responseCode = "200", description = "Employé trouvé",
                    content = {
                    @Content( mediaType = "application/json", schema = @Schema(implementation = Employee.class))
            }),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employé non trouvé", content = @Content) })
    @GetMapping(path = "/employees/{employeeId}")
    public Employee getEmployeeById(@PathVariable int employeeId) {
        Employee employee = service.getEmployeeById(employeeId);
        if (null == employee) {
             throw new EmployeeNotFoundException("Employee Not Found.");
        }
        return  employee;
    }

    // ---------------- GET BY ID - HATEOAS ----------------
    @GetMapping(path = "hateoas/employees/{employeeId}")
    public EntityModel<Employee> getEmployeeByIdHateoas(@PathVariable int employeeId) {
        Employee employee = service.getEmployeeById(employeeId);
        if (null == employee) {
            throw new EmployeeNotFoundException("Employee Not Found.");
        }
        EntityModel<Employee> model = EntityModel.of(employee);
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAll())
                .withRel("all-employees");
        model.add(link);
        return model;
    }

    // ---------------- POST Create Employee ----------------
    // Enable Validation with @Valid
    @PostMapping("employees/user")
    public ResponseEntity<Object> saveEmployee(@Valid @RequestBody Employee emp) {
        Employee newEmployee = service.saveEmployee(emp);
        // modify Header Localion URL to employees/user/4
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{employeeId}") // variable name for newEmployee.getEmployeeId
                .buildAndExpand(newEmployee.getId())
                .toUri();
        return ResponseEntity.created(uri).build(); // created = attach code 202 to response
    }

    // ---------------- DELETE Employee ----------------
    @DeleteMapping("employees/delete/{employeeId}")
    public void deleteEmployee(@PathVariable int employeeId) {
        Employee employee = service.deleteEmployee(employeeId);
        if (null == employee) {
            throw new EmployeeNotFoundException("Employee Not Found.");
        }
    }
}
