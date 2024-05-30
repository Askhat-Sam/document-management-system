package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="document_comment")
public class DocumentComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="user_id")
    private String userId;
    @Column(name="comment")
    private String comment;
    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="document_id")
    private Document document;

    @Column(insertable=false, updatable=false, name="document_id")
    private int documentId;

    public DocumentComment(String userId, String comment) {
        this.userId = userId;
        this.comment = comment;
    }

    public DocumentComment(String userId, String comment, int documentId) {
        this.userId = userId;
        this.comment = comment;
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "DocumentComment{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", comment='" + comment + '\'' +
                ", documentId=" + documentId +
                '}';
    }
}
