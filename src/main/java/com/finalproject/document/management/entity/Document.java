package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="document")
public class Document{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="document_code")
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
    @Column(name="link")
    private String link;
    @JsonManagedReference
    @OneToMany(mappedBy = "document", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<DocumentComment> comments;

    public Document(String documentReference, String type, String name, int revision, String status, String issueDate,
                    String revisionDate, int revisionInterval, String processOwner, String link) {
        this.documentReference = documentReference;
        this.type = type;
        this.name = name;
        this.revision = revision;
        this.status = status;
        this.issueDate = issueDate;
        this.revisionDate = revisionDate;
        this.revisionInterval = revisionInterval;
        this.processOwner = processOwner;
        this.link= link;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", documentReference='" + documentReference + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", revision=" + revision +
                ", status='" + status + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", revisionDate='" + revisionDate + '\'' +
                ", revisionInterval=" + revisionInterval +
                ", processOwner='" + processOwner + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    public void add(DocumentComment comment){
        if(comment==null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
        comment.setDocument(this);
    }
}
