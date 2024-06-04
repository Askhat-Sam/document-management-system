package com.finalproject.document.management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "document_revision")
public class DocumentRevision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "document_id", insertable=false, updatable=false)
    private Long documentId;
    @Column(name = "date")
    private String date;
    @Column(name = "revision_number")
    private Long revisionNumber;
    @Column(name = "statusId")
    private Long statusId;
    @Column(name = "link")
    private String link;
    @Column(name = "validated_user_id")
    private Long validatedUserId;

    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="document_id")
    private Document documentRevision;

    public DocumentRevision(Long documentId, String date, Long revisionNumber, Long statusId, String link, Long validatedUserId) {
        this.documentId = documentId;
        this.date = date;
        this.revisionNumber = revisionNumber;
        this.statusId = statusId;
        this.link = link;
        this.validatedUserId = validatedUserId;
    }

    @Override
    public String toString() {
        return "DocumentRevision{" +
                "id=" + id +
                ", documentId=" + documentId +
                ", date='" + date + '\'' +
                ", revisionNumber=" + revisionNumber +
                ", statusId=" + statusId +
                ", link='" + link + '\'' +
                ", validatedUserId=" + validatedUserId +
                '}';
    }
}
