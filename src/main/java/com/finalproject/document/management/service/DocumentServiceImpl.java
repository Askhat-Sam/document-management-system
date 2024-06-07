package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.repository.DocumentRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
    @Autowired
    EntityManager entityManager;

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
                case "Id": documentsFiltered = documents.stream().filter(d -> Long.toString(d.getId()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Document Code": documentsFiltered = documents.stream().filter(d -> d.getDocumentCode().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return documentsFiltered;
                case "Document Type Id": documentsFiltered = documents.stream().filter(d ->  Long.toString(d.getDocumentTypeId()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Title": documentsFiltered = documents.stream().filter(d -> d.getName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return documentsFiltered;
                case "Status Id": documentsFiltered = documents.stream().filter(d -> Long.toString(d.getStatusId()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Creation Date": documentsFiltered = documents.stream().filter(d -> d.getCreationDate().contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Modification Date": documentsFiltered = documents.stream().filter(d -> d.getModificationDate().contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Revision Number": documentsFiltered = documents.stream().filter(d -> Long.toString(d.getRevisionNumber()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Author Id": documentsFiltered = documents.stream().filter(d -> Long.toString(d.getAuthorId()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "All": return documents;
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
        entityManager.clear();

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
    public String uploadDocument(String fileName, String path, String actionType) {
        return moveFile(fileName, path, actionType);
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

    private static String moveFile(String fileName, String filePath, String actionType) {

        try {
            Path filePathTemp = findFile(Paths.get(filePath), fileName);
            if (filePathTemp != null) {
                String oldPath = filePath  + fileName;
                Path oldFilePath = Paths.get(oldPath);
                String newPath = actionType.equals("upload") ? "src/main/resources/documentsUploaded/" + fileName:
                        "src/main/resources/donwloads/" + fileName;

                Path newFilePath = Paths.get(newPath);

                File file = new File(oldPath);

                if (!file.exists()){
                    logger.log(Level.SEVERE, "File cannot be found");
                } else {
                    if (actionType.equals("upload") || actionType.equals("download")) {
                        try {
                            Files.copy(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
                            return "src/main/resources/donwloads/" + fileName;
                        } catch (IOException e) {
                            logger.log(Level.SEVERE, "File cannot be uploaded");
                        }
                    } else if (actionType.equals("delete")) {
                        try {
                            Files.delete(oldFilePath);
                        } catch (IOException e) {
                            logger.log(Level.SEVERE, "File cannot be deleted");
                        }
                    }
                }
            } else {
                System.out.println("File not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Path findFile(Path directory, String fileName) throws IOException {
        final Path[] foundFile = {null};

        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().equals(fileName)) {
                    foundFile[0] = file;
                    return FileVisitResult.TERMINATE; // Stop the file tree walk
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                // Handle file visit failure
                return FileVisitResult.CONTINUE;
            }
        });

        return foundFile[0];
    }
}
