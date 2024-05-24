package com.finalproject.document.management.entity;

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
@Table(name="document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="document_reference")
    private String documentReference;
    @Column(name="type")
    private String type;
    @Column(name="name")
    private String name;
    @Column(name="revision")
    private int revision;
    @Column(name="status")
    private String status;
    @Column(name="issue_date")
    private String issueDate;
    @Column(name="revision_date")
    private String revisionDate;
    @Column(name="revision_interval")
    private int revisionInterval;
    @Column(name="process_owner")
    private String processOwner;

    public Document(String documentReference, String type, String name, int revision, String status, String issueDate, String revisionDate, int revisionInterval, String processOwner) {
        this.documentReference = documentReference;
        this.type = type;
        this.name = name;
        this.revision = revision;
        this.status = status;
        this.issueDate = issueDate;
        this.revisionDate = revisionDate;
        this.revisionInterval = revisionInterval;
        this.processOwner = processOwner;
    }
}
