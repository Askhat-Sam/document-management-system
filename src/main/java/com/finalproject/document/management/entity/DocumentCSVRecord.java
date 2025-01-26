package com.finalproject.document.management.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCSVRecord {
    @CsvBindByName(column = "Id")
    private Long id;

    @CsvBindByName(column = "Document Code")
    private String documentCode;

    @CsvBindByName(column = "Document Code")
    private String documentType;

    @CsvBindByName(column = "Name")
    private String name;

    @CsvBindByName(column = "Revision number")
    private Long revisionNumber;

    @CsvBindByName(column = "Status")
    private String status;

    @CsvBindByName(column = "Creation date")
    private String creationDate;

    @CsvBindByName(column = "Modification date")
    private String modificationDate;

    @CsvBindByName(column = "Process owner")
    private String author;

//    @CsvBindByName(column = "Document Code")
//    private String link;
}
