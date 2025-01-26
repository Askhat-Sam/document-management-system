package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.entity.DocumentCSVRecord;

import java.io.File;
import java.util.List;

public interface DocumentCSVService {
    List<DocumentCSVRecord> convertCSV(File csvFile);
}
