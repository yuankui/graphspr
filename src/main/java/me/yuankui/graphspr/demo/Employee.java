package me.yuankui.graphspr.demo;

import me.yuankui.graphspr.core.Model;

@Model("Employee")
public class Employee {
    public String getName() {
        return "yuankui";
    }
    
    public int getAge() {
        return 22;
    }
    
    public Department getDepartment() {
        return new Department();
    }
}
