package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name="document_comment")
public class DocumentComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="document_id", insertable=false, updatable=false)
    private Long documentId;
    private String userId;
    @Column(name="date")
    private String date;
    @Column(name="comment")
    private String comment;
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name="document_id")
    private Document document;


    public DocumentComment(Long documentId, String userId, String date, String comment) {
        this.documentId = documentId;
        this.userId = userId;
        this.date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
        this.comment = comment;
    }
}
