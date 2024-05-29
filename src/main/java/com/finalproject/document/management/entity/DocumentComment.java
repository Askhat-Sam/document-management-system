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
@Table(name="document_comment")
public class DocumentComment {
    @Id
    @Column(name="id")
    private int id;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="join_id")
    private int document;
    @Column(name="user_id")
    private String userId;
    @Column(name="comment")
    private String comment;

    public DocumentComment(String userId, String comment) {
        this.userId = userId;
        this.comment = comment;
    }
}
