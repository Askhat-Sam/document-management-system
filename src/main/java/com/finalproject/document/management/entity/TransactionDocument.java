package com.finalproject.document.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "transaction_document")
public class TransactionDocument implements TransactionEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private String date;
    @Column(name = "user")
    private String user;
    @Column(name = "document_id")
    private Long documentId;
    @Column(name = "transaction_type")
    private String transactionType;

    public TransactionDocument(String date, String user, Long documentId, String transactionType) {
        this.date = date;
        this.user = user;
        this.documentId = documentId;
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", user='" + user + '\'' +
                ", toolId='" + documentId + '\'' +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}
