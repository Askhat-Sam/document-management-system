package com.finalproject.document.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DocumentDTO {
    private Long id;
    private String documentCode;
    private String documentType;
    private String name;
    private Long revisionNumber;
    private String status;
    private String creationDate;
    private String modificationDate;
    private String author;

    @Override
    public String toString() {
        return "DocumentDTO{" +
                "id=" + id +
                ", documentCode='" + documentCode + '\'' +
                ", documentType='" + documentType + '\'' +
                ", name='" + name + '\'' +
                ", revisionNumber=" + revisionNumber +
                ", status='" + status + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", modificationDate='" + modificationDate + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
