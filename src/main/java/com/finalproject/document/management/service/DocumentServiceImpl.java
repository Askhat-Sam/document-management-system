package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private DocumentRepository documentRepository;

    private static final Logger logger = Logger.getLogger(DocumentServiceImpl.class.getName());

    @Override
    public List<Document> findAll(Integer pageNo, Integer pageSize, String sortBy, String sortDirection,  String  keyword, String column) {
        Pageable pageable = doPagingAndSorting(pageNo, pageSize, sortBy, sortDirection);
        List<Document> documents;
        if (pageable != null) {
            documents = documentRepository.findAll(pageable).toList();
        } else {
            documents = documentRepository.findAll();
        }

        List<Document> documentsFiltered = new ArrayList<>();

        //if searching by keyword in certain column. Uses "contains" to search by the part of word.
        if (keyword!=null&&column!=null){
            switch(column) {
                case "id": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getId()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "documentCode": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getDocumentCode()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "documentTypeId": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getDocumentTypeId()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "name": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getName()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "statusId": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getStatusId()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "creationDate": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getCreationDate()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "modificationDate": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getModificationDate()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
//                case "revisionInterval": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.get()).equals(keyword)).collect(Collectors.toList());
//                    return documentsFiltered;
                case "authorId": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getAuthorId()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "link": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getLink()).contains(keyword)).collect(Collectors.toList());
            }
            return documentsFiltered;
        }
        return documents;
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    //Issue with using OPTIONAL ????
    @Override
    public Document findById(Long id) {

        Optional<Document> result =documentRepository.findById(id);

        System.out.println("result: " + result);

        Document document = null;

        if (result.isPresent()) {
            document = result.get();
            System.out.println("result.get(): " + document);
        } else {
            throw new RuntimeException("Did not find document with id: " + id);
        }
        return document;
    }

    @Override
    public void update(Document document) {
        documentRepository.save(document);
    }

    @Override
    public void deleteDocumentById(Long id) {
        documentRepository.deleteById(id);
    }

    @Override
    public String uploadDocument(String path, String actionType) {
        return moveFile(path, actionType);
    }

    public <T> String downloadList(List<T> documentsList) {
        List<Document> documents =documentRepository.findAll();
        String excelFilePath = "src/main/resources/donwloads/list.xlsx";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("List");

        // Set style for table body
        CellStyle styleBody = workbook.createCellStyle();
        styleBody.setBorderTop(BorderStyle.THIN);
        styleBody.setBorderBottom(BorderStyle.THIN);
        styleBody.setBorderLeft(BorderStyle.THIN);
        styleBody.setBorderRight(BorderStyle.THIN);
        styleBody.setAlignment(HorizontalAlignment.LEFT);

        int rowIndex = 1;
        // Add header values
        Row rowHeader = sheet.createRow(rowIndex-1);
        Cell cellHeader = rowHeader.createCell(0);
        cellHeader.setCellValue("Id");
        Cell cellHeader1 = rowHeader.createCell(1);
        cellHeader1.setCellValue("Document code");
        Cell cellHeader2 = rowHeader.createCell(2);
        cellHeader2.setCellValue("Type");
        Cell cellHeader3 = rowHeader.createCell(3);
        cellHeader3.setCellValue("Name");
        Cell cellHeader4 = rowHeader.createCell(4);
        cellHeader4.setCellValue("Revision number");
        Cell cellHeader5 = rowHeader.createCell(5);
        cellHeader5.setCellValue("Status");
        Cell cellHeader6 = rowHeader.createCell(6);
        cellHeader6.setCellValue("Creation date");
        Cell cellHeader7 = rowHeader.createCell(7);
        cellHeader7.setCellValue("Modification date");
        Cell cellHeader8 = rowHeader.createCell(8);
        cellHeader8.setCellValue("Process owner");
        Cell cellHeader9 = rowHeader.createCell(9);
        cellHeader9.setCellValue("Link");

        // Add table body values
        for (Document d:documents) {
            Row row = sheet.createRow(rowIndex++);
            Cell cell = row.createCell(0);
            cell.setCellValue(d.getId());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(d.getDocumentCode());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(d.getDocumentTypeId());
            Cell cell3= row.createCell(3);
            cell3.setCellValue(d.getName());
            Cell cell4= row.createCell(4);
            cell4.setCellValue(d.getRevisionNumber());
            Cell cell5= row.createCell(5);
            cell5.setCellValue(d.getStatusId());
            Cell cell6= row.createCell(6);
            cell6.setCellValue(d.getCreationDate());
            Cell cell7= row.createCell(7);
            cell7.setCellValue(d.getModificationDate());
            Cell cell8= row.createCell(8);
            cell8.setCellValue(d.getAuthorId());
            Cell cell9= row.createCell(9);
            cell9.setCellValue(d.getLink());
        }

        // Apply borders to all non-empty cells
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    cell.setCellStyle(styleBody);
                }
            }
        }

        for (int i = 0; i <= 11; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            workbook.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Workbook cannot be closed");
        }
        return "List has been downloaded";
    }

    @Override
    public List<Document> findByUserId(Long id) {
        return documentRepository.findByUserId(id);
    }

    private static Pageable doPagingAndSorting(Integer page, Integer size, String sortBy, String sortDirection) {
        if (sortBy != null) {
            Sort sort = Sort.by(Sort.DEFAULT_DIRECTION, sortBy);
            // Defining the sort direction: A - ascending and D - descending. If direction not provided, default direction to be applied
            if (sortDirection != null) {
                if (sortDirection.equals("A")) {
                    sort = Sort.by(ASC, sortBy);
                } else if (sortDirection.equals("D")) {
                    sort = Sort.by(DESC, sortBy);
                }
            }

            Pageable pageable;
            if (page != null) {
                pageable = PageRequest.of(page, size, sort);
            } else {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
            }
            return pageable;
        } else {
            Pageable pageable;
            if (page != null) {
                pageable = PageRequest.of(page, size);
            } else {
                pageable = PageRequest.of(0, Integer.MAX_VALUE);
            }
            return pageable;
        }
    }

    private static String moveFile(String filePath, String actionType) {

        Path oldFilePath = Paths.get(filePath);

        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

        String newPath = "src/main/resources/documentsUploaded/" + fileName;

        Path newFilePath = Paths.get(newPath);

        File file = actionType.equals("upload") ? new File(filePath): new File(newPath);

        if (!file.exists()){
            logger.log(Level.SEVERE, "File cannot be found");
        } else {
            if (actionType.equals("upload")) {
                try {
                    Files.copy(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(oldFilePath);
                    return "src/main/resources/documentsUploaded/" + fileName;
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "File cannot be uploaded");
                }
            } else if (actionType.equals("delete")) {
                try {
                    Files.delete(newFilePath);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "File cannot be deleted");
                }
            }
        }
        return null;
    }
}
