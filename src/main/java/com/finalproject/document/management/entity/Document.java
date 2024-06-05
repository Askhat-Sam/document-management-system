package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_code")
    private String documentCode;

    @Column(insertable = false, updatable = false, name = "document_type_id")
    private int documentTypeId;

    @Column(name = "name")
    private String name;

    @Column(name = "revision_number")
    private Long revisionNumber;

    @Column(insertable = false, updatable = false, name = "status_id")
    private Long statusId;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "modification_date")
    private String modificationDate;

    @Column(name = "author_id", insertable = false, updatable = false)
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
    @JoinColumn(name = "status_id")
    private DocumentStatus documentStatus;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "author_id")
    private User user;

    public Document(String documentCode, DocumentType documentType, String name, Long revisionNumber,
                    DocumentStatus documentStatus, String creationDate, String modificationDate,
                    User user, String link) {
        this.documentCode = documentCode;
        this.documentType = documentType;
        this.name = name;
        this.revisionNumber = revisionNumber;
        this.documentStatus = documentStatus;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.user =user;
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
                ", comments=" + comments +
                ", revisions=" + revisions +
                ", documentStatus=" + documentStatus +
                ", documentType=" + documentType +
                ", user=" + user +
                '}';
    }

    public void addComment(DocumentComment comment){
        if(comment==null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
        comment.setDocument(this);
    }

    public void addRevision(DocumentRevision revision){
        if(revision==null){
            revisions = new ArrayList<>();
        }
        revisions.add(revision);
        revision.setDocument(this);
    }
}
