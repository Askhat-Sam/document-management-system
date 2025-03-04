package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "document")
@ToString
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_code")
    private String documentCode;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "name")
    private String name;

    @Column(name = "revision_number")
    private Long revisionNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "modification_date")
    private String modificationDate;

    @Column(name = "author")
    private String author;

    @Column(name = "link")
    private String link;


    public Document(String documentCode, String documentType, String name, Long revisionNumber, String status, String creationDate, String modificationDate, String author, String link) {
        this.documentCode = documentCode;
        this.documentType = documentType;
        this.name = name;
        this.revisionNumber = revisionNumber;
        this.status = status;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.author = author;
        this.link = link;
    }

    public Document(String documentCode, String documentType, String name, Long revisionNumber, String status, String creationDate, String modificationDate, String author) {
        this.documentCode = documentCode;
        this.documentType = documentType;
        this.name = name;
        this.revisionNumber = revisionNumber;
        this.status = status;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.author = author;
    }


    public Document(String documentType, String name, Long revisionNumber, String status, String creationDate, String modificationDate, String author, String link) {
        this.documentType = documentType;
        this.name = name;
        this.revisionNumber = revisionNumber;
        this.status = status;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.author = author;
    }

    public Document(Long id, String documentCode, String documentType, String name, Long revisionNumber, String status, String creationDate, String modificationDate, String author) {
        this.id = id;
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
