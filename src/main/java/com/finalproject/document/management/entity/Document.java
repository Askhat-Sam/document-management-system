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
    private String documentCode;
    @Column(insertable=false, updatable=false, name="document_type_id")
    private int documentTypeId;
    @Column(name="name")
    private String name;
    @Column(name="revision_number")
    private int revisionNumber;
    @Column(name="status_id")
    private int statusId;
    @Column(name="creation_date")
    private String creationDate;
    @Column(name="modification_date")
    private String modificationDate;
    @Column(name="author_id")
    private String authorId;
    @Column(name="link")
    private String link;
    @JsonManagedReference
    @OneToMany(mappedBy = "document", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<DocumentComment> comments;

    public Document(String documentCode, int documentTypeId, String name, int revisionNumber, int statusId, String creationDate, String modificationDate, String authorId, String link, List<DocumentComment> comments) {
        this.documentCode = documentCode;
        this.documentTypeId = documentTypeId;
        this.name = name;
        this.revisionNumber = revisionNumber;
        this.statusId = statusId;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.authorId = authorId;
        this.link = link;
        this.comments = comments;
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
                ", authorId='" + authorId + '\'' +
                ", link='" + link + '\'' +
                ", comments=" + comments +
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
