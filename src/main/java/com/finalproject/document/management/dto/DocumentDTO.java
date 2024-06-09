package com.finalproject.document.management.dto;

import jakarta.persistence.Column;
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
}
