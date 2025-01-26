package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.entity.DocumentCSVRecord;
import com.finalproject.document.management.service.interfaces.DocumentCSVService;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class DocumentCSVServiceImpl implements DocumentCSVService {
    @Override
    public List<DocumentCSVRecord> convertCSV(File csvFile) {

        try {
            List<DocumentCSVRecord> documentCSVRecords = new CsvToBeanBuilder<DocumentCSVRecord>(new FileReader(csvFile))
                    .withType(DocumentCSVRecord.class)
                    .build().parse();
            return documentCSVRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
