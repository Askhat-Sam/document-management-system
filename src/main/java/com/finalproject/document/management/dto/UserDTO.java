package com.finalproject.document.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String role;
}
