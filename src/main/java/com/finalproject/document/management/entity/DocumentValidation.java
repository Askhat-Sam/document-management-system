package com.finalproject.document.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "document_validation")
public class DocumentValidation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_code")
    private String documentCode;
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "name")
    private String name;

    @Column(name = "revision_number")
    private Long revisionNumber;

    @Column(name = "author")
    private String userId;

    @Column(name = "status")
    private String status;
    @Column(name = "link")
    private String link;



    public DocumentValidation(Long id, String documentCode, String name, Long revisionNumber, String userId, String status) {
        this.id = id;
        this.documentCode = documentCode;
        this.name = name;
        this.revisionNumber = revisionNumber;
        this.userId = userId;
        this.status = status;
    }

    public DocumentValidation(String documentCode, Long documentId, String name, Long revisionNumber, String userId, String status, String link) {
        this.documentCode = documentCode;
        this.documentId = documentId;
        this.name = name;
        this.revisionNumber = revisionNumber;
        this.userId = userId;
        this.status = status;
        this.link = link;
    }
}
