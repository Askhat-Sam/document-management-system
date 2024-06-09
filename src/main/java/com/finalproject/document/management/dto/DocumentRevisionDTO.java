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

    public DocumentRevisionDTO(String userId, Long documentId, String date, Long revisionNumber, String status, String description, String link, String validatingUser) {
        this.userId = userId;
        this.documentId = documentId;
        this.date = date;
        this.revisionNumber = revisionNumber;
        this.status = status;
        this.description = description;
        this.link = link;
        this.validatingUser = validatingUser;
    }
}
