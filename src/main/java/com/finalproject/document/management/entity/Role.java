package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="user_id")
    private String userId;
    @Column(name="user_role")
    private String userRole;
    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="join_id")
    private User user;

    @Column(insertable=false, updatable=false, name="join_id")
    private long joinId;

    public Role(String userRole) {
        this.userRole = userRole;
    }

    public Role(String userId,  String userRole) {
        this.userId = userId;
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return this.getUserRole();
    }
}
