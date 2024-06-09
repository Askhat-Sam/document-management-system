package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name="user")
public class User implements Comparable<User>{

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private String userId;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="department")
    private String department;

    @Column(name="role")
    private String role;
    @Column(name="status")
    private String status;

    @Column(name="password")
    private String password;

    @Column(name="active")
    private int active;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    private List<Document> documents;

    public User(String userId, String firstName, String lastName, String email, String department, String role, String status, String password, int active) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.role = role;
        this.status =status;
        this.password = password;
        this.active = active;
    }

    public User(String userId, String firstName, String lastName, String email, String department, String role, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.role = role;
        this.password = password;
    }

    @Override
    public int compareTo(User o) {
        return Comparator.comparing(User::getUserId)
                .thenComparing(User::getFirstName)
                .thenComparing(User::getLastName)
                .thenComparing(User::getEmail)
                .thenComparing(User::getDepartment)
                .thenComparing(User::getRole)
                .compare(this, o);
    }
}
