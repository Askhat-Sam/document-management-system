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
@ToString
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_code")
    private String documentCode;

    @Column(name = "document_type")
    private Long documentType;

    @Column(name = "name")
    private String name;

    @Column(name = "revision_number")
    private Long revisionNumber;

    @Column(name = "status")
    private Long status;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "modification_date")
    private String modificationDate;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "link")
    private String link;

    @JsonManagedReference
    @OneToMany(mappedBy = "document", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<DocumentComment> comments;

    @JsonManagedReference
    @OneToMany(mappedBy = "document", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<DocumentRevision> revisions;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private User user;

    public Document(String documentCode, Long documentType, String name, Long revisionNumber, Long status, String creationDate, String modificationDate, Long authorId, String link) {
        this.documentCode = documentCode;
        this.documentType = documentType;
        this.name = name;
        this.revisionNumber = revisionNumber;
        this.status = status;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.authorId = authorId;
        this.link = link;
    }

    public void addComment(DocumentComment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
        comment.setDocument(this);
    }

    public void addRevision(DocumentRevision revision) {
        if (revisions == null) {
            revisions = new ArrayList<>();
        }
        revisions.add(revision);
        revision.setDocument(this);
    }
}
