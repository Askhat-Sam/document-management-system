package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    @Column(name="date")
    private String date;
    @Column(name="comment")
    private String comment;
    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="document_id")
    private Document document;

    public DocumentComment(String userId, String comment) {
        this.userId = userId;
        this.comment = comment;
    }

    public DocumentComment(String userId, String date, String comment) {
        this.userId = userId;
        this.date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
        this.comment = comment;
    }

    public DocumentComment(String userId, String comment, int documentId) {
        this.userId = userId;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "DocumentComment{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
