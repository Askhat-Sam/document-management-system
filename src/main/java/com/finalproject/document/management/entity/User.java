package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="user")
public class User{

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name="user_id")
    private String userId;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="email")
    private String email;
    @Column(insertable=false, updatable=false, name="department_id")
    private String departmentId;
    @JsonManagedReference
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name="department_id")
    private Department department;
    @Column(name="role")
    private String role;
    @Column(name="password")
    private String password;
    @Column(name="active")
    private int active;

    public User(String userId, String firstName, String lastName, String email, Department department, String role, String password, int active) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.role = role;
        this.password = password;
        this.active = active;
    }

    public User(String userId, String firstName, String lastName, String email, String departmentId, String role, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.departmentId = departmentId;
        this.role = role;
        this.password = password;
    }

    public Department getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", department=" + department +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                '}';
    }
}
