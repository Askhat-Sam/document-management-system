package com.finalproject.document.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="transaction_user")
public class TransactionUser implements TransactionEntity{
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name="date")
    private String date;
    @Column(name="user")
    private String user;
    @Column(name="user_id")
    private String userId;
    @Column(name="transaction_type")
    private String transactionType;

    public TransactionUser(String date, String user, String userId, String transactionType) {
        this.date = date;
        this.user = user;
        this.userId = userId;
        this.transactionType = transactionType;
    }

    public TransactionUser(Long id, String date, String user, String userId, String transactionType) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.userId = userId;
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "TransactionUser{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", user='" + user + '\'' +
                ", documentId=" + userId +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}
