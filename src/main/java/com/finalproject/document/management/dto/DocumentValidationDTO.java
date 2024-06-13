package com.finalproject.document.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DocumentValidationDTO {
    private Long id;
    private String documentCode;
    private Long documentId;
    private String name;
    private Long revisionNumber;
    private String userId;
    private String status;
}
