package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="department")
public class Department {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private int name;
    @JsonManagedReference
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<User> users;
    public Department(int name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
