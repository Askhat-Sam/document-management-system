package com.finalproject.document.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="document_type")
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="type_name")
    private int typeName;
    @Column(name="type_short_name")
    private int typeShortName;
    @Column(name="revision_interval")
    private int revisionInterval;

    public DocumentType(int typeName, int typeShortName, int revisionInterval) {
        this.typeName = typeName;
        this.typeShortName = typeShortName;
        this.revisionInterval = revisionInterval;
    }

    @Override
    public String toString() {
        return "DocumentType{" +
                "id=" + id +
                ", typeName=" + typeName +
                ", typeShortName=" + typeShortName +
                ", revisionInterval=" + revisionInterval +
                '}';
    }
}