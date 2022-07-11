package com.bogdan.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// DAO - Data Access Object
@Component
public class EmployeeDao {
    static List<Employee> list = new ArrayList<>();

//    static {
//        list.add(new Employee(1234, "Jean","jean@gmail.com"));
//        list.add(new Employee(1235, "Marie","marie@gmail.com"));
//        list.add(new Employee(1236, "Pierre","pierre@gmail.com"));
//    }

    public List<Employee> getAllEmployees() {
        return list;
    }

    public Employee getEmployeeById(int employeeId) {
        return list.stream()
                .filter( emp -> emp.getId() == employeeId)
                .findAny()
                .orElse(null); // return null if no employee is found

    }

    public Employee saveEmployee(Employee emp) {
        emp.setId((long) (list.size()+ 1)); // bidouille (long)
        list.add(emp);
        return emp;
    }

    public Employee deleteEmployee(int employeeId) {
        Iterator<Employee> iterator = list.iterator();
        while (iterator.hasNext()) {
            Employee emp = iterator.next();
            if(employeeId == emp.getId()) {
                iterator.remove();
                return emp;
            }
        }
        return null;
    }
}

