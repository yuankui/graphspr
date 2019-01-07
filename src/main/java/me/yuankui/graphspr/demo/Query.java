package me.yuankui.graphspr.demo;

import me.yuankui.graphspr.core.Root;

import java.util.Arrays;
import java.util.List;

@Root
public class Query {
    public Employee getEmployee() {
        return new Employee();
    }
    public List<Employee> getEmployees() {
        return Arrays.asList(new Employee(), new Employee(), new Employee());
    }
    public Department getDepartment() {
        return new Department();
    } 
    
    public List<Department> getDepartments() {
        return Arrays.asList(new Department(), new Department());
    }
}
