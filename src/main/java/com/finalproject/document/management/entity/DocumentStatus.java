package com.finalproject.document.management.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="document_status")
public class DocumentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="status_name")
    private String statusName;

    public DocumentStatus(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return "DocumentStatus{" +
                "id=" + id +
                ", statusName='" + statusName + '\'' +
                '}';
    }
}
