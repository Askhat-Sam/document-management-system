package com.finalproject.document.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class DocumentDTO {
    private Long id;
    private String documentCode;
    private String documentType;
    private String name;
    private Long revisionNumber;
    private String status;
    private String creationDate;
    private String modificationDate;
    private String author;
    private String link;

    public DocumentDTO(String documentCode, String documentType, String name, Long revisionNumber, String status, String creationDate, String modificationDate, String author) {
        this.documentCode = documentCode;
        this.documentType = documentType;
        this.name = name;
        this.revisionNumber = revisionNumber;
        this.status = status;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.author = author;
    }
}
