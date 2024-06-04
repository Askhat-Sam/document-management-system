package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Long id;
    @Column(name="document_id", insertable=false, updatable=false)
    private Long documentId;
    @Column(name="user_id",  insertable=false, updatable=false)
    private Long userId;
    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;
    @Column(name="date")
    private String date;
    @Column(name="comment")
    private String comment;
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name="document_id")
    private Document document;

    public DocumentComment(Long userId, String comment) {
        this.userId = userId;
        this.comment = comment;
    }

    public DocumentComment(Long userId, String date, String comment) {
        this.userId = userId;
        this.date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
        this.comment = comment;
    }

    public DocumentComment(Long userId, String comment, int documentId) {
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
