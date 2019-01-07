package me.yuankui.graphspr.demo;

import me.yuankui.graphspr.core.Model;

import java.util.Arrays;
import java.util.List;

@Model("Department")
public class Department {
    public String getName() {
        return "BD";
    }
    
    public int getIncome() {
        return 100;
    }
    public List<Employee> getEmployees() {
        return Arrays.asList(new Employee(), new Employee());
    }
}
