package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.entity.DocumentCSVRecord;
import com.finalproject.document.management.service.interfaces.DocumentCSVService;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class DocumentCSVServiceImplTest {
    DocumentCSVService documentCSVService = new DocumentCSVServiceImpl();


    @Test
    public void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/documents.csv");
        List<DocumentCSVRecord> docs = documentCSVService.convertCSV(file);
        System.out.println(docs.size());
        System.out.println(docs);
    }
}
