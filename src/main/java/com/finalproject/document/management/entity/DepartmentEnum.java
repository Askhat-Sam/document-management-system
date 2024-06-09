package com.finalproject.document.management.entity;

public class DepartmentEnum {
    private static DepartmentName departmentName;
    public enum DepartmentName{
        Finance,
        Procurement,
        Human_resources,
        Administrative,
        Sales,
        Marketing,
        Legal,
        Security
    }

    public static DepartmentName getDepartmentName() {
        return departmentName;
    }
}
