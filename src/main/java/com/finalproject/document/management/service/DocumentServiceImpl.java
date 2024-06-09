package com.finalproject.document.management.service;

import com.finalproject.document.management.dto.DocumentDTO;
import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.repository.DocumentRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
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
    private final DocumentRepository documentRepository;
    private final EntityManager entityManager;
    private static final Logger logger = Logger.getLogger(DocumentServiceImpl.class.getName());

    @Override
    public List<DocumentDTO> findAll(Integer pageNo, Integer pageSize, String sortBy, String sortDirection, String  keyword, String column) {
        Pageable pageable = doPagingAndSorting(pageNo, pageSize, sortBy, sortDirection);
        List<Document> documents;
        List<DocumentDTO> documentsDTO = new ArrayList<>();
        if (pageable != null) {
            documents = documentRepository.findAll(pageable).toList();
            documentsDTO = documents.stream().map(this::fromEntityToDTO).collect(Collectors.toList());
        } else {
            documents = documentRepository.findAll();
            documentsDTO = documents.stream().map(this::fromEntityToDTO).collect(Collectors.toList());
        }

        List<DocumentDTO> documentsFiltered = new ArrayList<>();

        //if searching by keyword in certain column. Uses "contains" to search by the part of word.
        if (keyword!=null&&column!=null){
            switch(column) {
                case "Id": documentsFiltered = documentsDTO.stream().filter(d -> Long.toString(d.getId()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Document Code": documentsFiltered = documentsDTO.stream().filter(d -> d.getDocumentCode().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return documentsFiltered;
                case "Document Type Id": documentsFiltered = documentsDTO.stream().filter(d ->  d.getDocumentType().toLowerCase().contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Title": documentsFiltered = documentsDTO.stream().filter(d -> d.getName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return documentsFiltered;
                case "Status Id": documentsFiltered = documentsDTO.stream().filter(d -> d.getStatus().toLowerCase().contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Creation Date": documentsFiltered = documentsDTO.stream().filter(d -> d.getCreationDate().contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Modification Date": documentsFiltered = documentsDTO.stream().filter(d -> d.getModificationDate().contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Revision Number": documentsFiltered = documentsDTO.stream().filter(d -> Long.toString(d.getRevisionNumber()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Author Id": documentsFiltered = documentsDTO.stream().filter(d -> d.getAuthor().toLowerCase().contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "All": return documentsDTO;
            }
            return documentsFiltered;
        }
        return documentsDTO;
    }

    public DocumentDTO fromEntityToDTO(Document document) {
        return new DocumentDTO();
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

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
    public void save(Document document) {
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

    public ResponseEntity<byte[]> downloadListAsExcel() {
        List<Document> documents = documentRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("List");

        // Set style for table body
        CellStyle styleBody = workbook.createCellStyle();
        styleBody.setBorderTop(BorderStyle.THIN);
        styleBody.setBorderBottom(BorderStyle.THIN);
        styleBody.setBorderLeft(BorderStyle.THIN);
        styleBody.setBorderRight(BorderStyle.THIN);
        styleBody.setAlignment(HorizontalAlignment.LEFT);

        // Create header CellStyle
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.index);
        CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
        // fill foreground color ...
        headerCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 32, 91), null));
        // and solid fill pattern produces solid grey cell fill
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFont(headerFont);


        int rowIndex = 1;
        // Add header values
        Row rowHeader = sheet.createRow(rowIndex - 1);
        rowHeader.createCell(0).setCellValue("Id");
        rowHeader.createCell(1).setCellValue("Document code");
        rowHeader.createCell(2).setCellValue("Type");
        rowHeader.createCell(3).setCellValue("Name");
        rowHeader.createCell(4).setCellValue("Revision number");
        rowHeader.createCell(5).setCellValue("Status");
        rowHeader.createCell(6).setCellValue("Creation date");
        rowHeader.createCell(7).setCellValue("Modification date");
        rowHeader.createCell(8).setCellValue("Process owner");
        rowHeader.createCell(9).setCellValue("Link");

        // Add table body values
        for (Document d : documents) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(d.getId());
            row.createCell(1).setCellValue(d.getDocumentCode());
            row.createCell(2).setCellValue(d.getDocumentTypeId());
            row.createCell(3).setCellValue(d.getName());
            row.createCell(4).setCellValue(d.getRevisionNumber());
            row.createCell(5).setCellValue(d.getStatusId());
            row.createCell(6).setCellValue(d.getCreationDate());
            row.createCell(7).setCellValue(d.getModificationDate());
            row.createCell(8).setCellValue(d.getAuthorId());
            row.createCell(9).setCellValue(d.getLink());
        }

        // Apply style to cells
        for (Row row : sheet) {
            for (Cell cell : row) {
                // Apply border to all non empty cells
                if (cell.getCellType() != CellType.BLANK) {
                    cell.setCellStyle(styleBody);
                }
                // Apply style to the headers
                if (cell.getRowIndex()==0) {
                    cell.setCellStyle(headerCellStyle);
                }
            }
        }

        for (int i = 0; i <= 9; i++) {
            sheet.autoSizeColumn(i);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=list.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Document> findByUserId(Long id) {
        return documentRepository.findByUserId(id);
    }

    @Override
    public void uploadfile(MultipartFile file, RedirectAttributes redirectAttributes, Long documentId, Model model) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
//            return "redirect:/uploadStatus";
        }

        try {
            // Save the file to the specified directory
            byte[] bytes = file.getBytes();
            Path path = Paths.get("src/main/resources/documentsUploaded/" + file.getOriginalFilename());
            Files.write(path, bytes);

            // Send the file path to the previous page link field
            redirectAttributes.addFlashAttribute("message", "documentsUploaded/" + file.getOriginalFilename());

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Document cannot be uploaded", e);
            redirectAttributes.addFlashAttribute("message", "File upload failed");
        }
    }

    @Override
    public ResponseEntity<byte[]> viewFile(String link) {
        try {
            // Load the PDF file as a resource from the classpath
            Resource resource = new ClassPathResource(link);

            // Read the file's content into a byte array
            byte[] content = Files.readAllBytes(resource.getFile().toPath());

            // Set headers to inform the browser to display the PDF
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + link);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Document cannot be displayed", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String link) {
        try {
            // Load the PDF file as a resource from the classpath
            Resource resource = new ClassPathResource(link);

            // Read the file's content into a byte array
            byte[] content = Files.readAllBytes(resource.getFile().toPath());

            // Set headers to inform the browser to display the PDF
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + link);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Document cannot be downloaded", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateDocument(Document document) {
        Document existingDocument = findById(document.getId());

        // Update the fields that are not null in the submitted form
        if (document.getDocumentCode() != null) {
            existingDocument.setDocumentCode(document.getDocumentCode());
        }
        if (document.getDocumentType() != null) {
            existingDocument.setDocumentType(document.getDocumentType());
        }
        if (document.getName() != null) {
            existingDocument.setName(document.getName());
        }
        if (document.getRevisionNumber() != null) {
            existingDocument.setRevisionNumber(document.getRevisionNumber());
        }
        if (document.getStatus() != null) {
            existingDocument.setStatus(document.getStatus());
        }
        if (document.getCreationDate() != null) {
            existingDocument.setCreationDate(document.getCreationDate());
        }
        if (document.getModificationDate() != null) {
            existingDocument.setModificationDate(document.getModificationDate());
        }
        if (document.getAuthorId() != null) {
            existingDocument.setAuthorId(document.getAuthorId());
        }
        if (document.getLink() != null) {
            existingDocument.setLink(document.getLink());
        }

        // Update the document in the database
        update(existingDocument);
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
