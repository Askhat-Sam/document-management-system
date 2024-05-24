package com.finalproject.document.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="document")
public class Document {
    @Id
    private int id;
    private String documentReference;
    private String type;
    private String name;
    private int revision;
    private String status;
    private String issueDate;
    private String revisionDate;
    private int revisionInterval;
    private String processOwner;
}
