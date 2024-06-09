package com.finalproject.document.management.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class DocumentRevisionDTO {
    private Long id;
    private String userId;
    private Long documentId;
    private String date;
    private Long revisionNumber;
    private String status;
    private String description;
    private String link;
    private String validatingUser;

}
