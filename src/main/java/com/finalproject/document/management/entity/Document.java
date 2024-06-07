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
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_code")
    private String documentCode;

    @Column(name = "document_type_id")
    private Long documentTypeId;

    @Column(name = "name")
    private String name;

    @Column(name = "revision_number")
    private Long revisionNumber;

    @Column(name = "status_id")
    private Long statusId;

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
    @JoinColumn(name = "status_id", insertable = false, updatable = false)
    private DocumentStatus documentStatus;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_type_id", insertable = false, updatable = false)
    private DocumentType documentType;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private User user;

    public Document(String documentCode, Long documentTypeId, String name, Long revisionNumber, Long statusId, String creationDate, String modificationDate, Long authorId, String link) {
        this.documentCode = documentCode;
        this.documentTypeId = documentTypeId;
        this.name = name;
        this.revisionNumber = revisionNumber;
        this.statusId = statusId;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.authorId = authorId;
        this.link = link;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", documentCode='" + documentCode + '\'' +
                ", documentTypeId=" + documentTypeId +
                ", name='" + name + '\'' +
                ", revisionNumber=" + revisionNumber +
                ", statusId=" + statusId +
                ", creationDate='" + creationDate + '\'' +
                ", modificationDate='" + modificationDate + '\'' +
                ", authorId=" + authorId +
                ", link='" + link + '\'' +
                '}';
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
