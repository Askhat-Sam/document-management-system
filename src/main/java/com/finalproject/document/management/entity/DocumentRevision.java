package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "document_revision")
public class DocumentRevision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "document_id")
    private Long documentId;
    @Column(name = "date")
    private String date;
    @Column(name = "revision_number")
    private Long revisionNumber;
    @Column(name = "status")
    private String status;
    @Column(name = "description")
    private String description;
    @Column(name = "link")
    private String link;
    @Column(name = "validated_user")
    private String validatingUser;

    public DocumentRevision(Long documentId, String date, Long revisionNumber, String status, String link, String validatingUserId) {
        this.documentId = documentId;
        this.date = date;
        this.revisionNumber = revisionNumber;
        this.status = status;
        this.link = link;
        this.validatingUser = validatingUser;
    }

    public DocumentRevision(String userId, Long documentId, String date, Long revisionNumber, String status, String description, String validatingUser) {
        this.userId = userId;
        this.documentId = documentId;
        this.date = date;
        this.revisionNumber = revisionNumber;
        this.status = status;
        this.description = description;
        this.validatingUser = validatingUser;
    }

    public DocumentRevision(Long id, String userId, Long documentId, String date, Long revisionNumber, String status, String description, String validatingUser) {
        this.id = id;
        this.userId = userId;
        this.documentId = documentId;
        this.date = date;
        this.revisionNumber = revisionNumber;
        this.status = status;
        this.description = description;
        this.validatingUser = validatingUser;
    }

    public DocumentRevision(String userId, Long documentId, String date, Long revisionNumber, String status, String description, String link, String validatingUser) {
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
