package com.finalproject.document.management.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="transaction")
public class TransactionDocument {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name="date")
    private String date;
    @Column(name="user")
    private String user;
    @Column(name="document_id")
    private int documentId;
    @Column(name="transaction_type")
    private String transactionType;

    public TransactionDocument(String date, String user, int toolId, String transactionType) {
        this.date = date;
        this.user = user;
        this.documentId = toolId;
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
