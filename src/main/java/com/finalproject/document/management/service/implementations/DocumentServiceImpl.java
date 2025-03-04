package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.dto.DocumentDTO;
import com.finalproject.document.management.entity.*;
import com.finalproject.document.management.repository.DocumentRepository;
import com.finalproject.document.management.repository.DocumentTransactionRepository;
import com.finalproject.document.management.service.interfaces.DocumentService;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFComments;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentTransactionRepository documentTransactionRepository;
    private final EntityManager entityManager;
    private static final Logger logger = Logger.getLogger(DocumentServiceImpl.class.getName());
//    @Value("${documentCheckPatterns.regexp}")
//    private List<String> patterns; TO be corrected issue with reading "," in properties fole


    @Override
    public List<DocumentDTO> findAll(Integer pageNo, Integer pageSize, String sortBy, String sortDirection, String keyword, String column) {
        Pageable pageable = doPagingAndSorting(pageNo, pageSize, sortBy, sortDirection);
        List<Document> documents;
        List<DocumentDTO> documentsDTO = new ArrayList<>();
        if (pageable != null) {
            documentsDTO = documentRepository.findAll(pageable).stream().map(this::fromEntityToDTO).collect(Collectors.toList());
        } else {
            documents = documentRepository.findAll();
            documentsDTO = documents.stream().map(this::fromEntityToDTO).collect(Collectors.toList());
        }

        List<DocumentDTO> documentsFiltered = new ArrayList<>();

        //if searching by keyword in certain column. Uses "contains" to search by the part of word.
        if (keyword != null && column != null) {
            switch (column) {
                case "Id":
                    documentsFiltered = documentsDTO.stream().filter(d -> Long.toString(d.getId()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Document code":
                    documentsFiltered = documentsDTO.stream().filter(d -> d.getDocumentCode().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return documentsFiltered;
                case "Document type":
                    documentsFiltered = documentsDTO.stream().filter(d -> d.getDocumentType().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return documentsFiltered;
                case "Title":
                    documentsFiltered = documentsDTO.stream().filter(d -> d.getName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return documentsFiltered;
                case "Status":
                    documentsFiltered = documentsDTO.stream().filter(d -> d.getStatus().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return documentsFiltered;
                case "Creation date":
                    documentsFiltered = documentsDTO.stream().filter(d -> d.getCreationDate().contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Modification date":
                    documentsFiltered = documentsDTO.stream().filter(d -> d.getModificationDate().contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Revision number":
                    documentsFiltered = documentsDTO.stream().filter(d -> Long.toString(d.getRevisionNumber()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "Author":
                    documentsFiltered = documentsDTO.stream().filter(d -> d.getAuthor().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return documentsFiltered;
            }
            return documentsFiltered;
        }
        return documentsDTO;
    }

    public DocumentDTO fromEntityToDTO(Document document) {
        return new DocumentDTO(document.getId(), document.getDocumentCode(), document.getDocumentType(), document.getName(),
                document.getRevisionNumber(), document.getStatus(), document.getCreationDate(), document.getModificationDate(),
                document.getAuthor());
    }

    @Override
    public List<DocumentDTO> findAll() {
        return documentRepository.findAll().stream().map(this::fromEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public List<Document> findAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public DocumentDTO findById(Long id) {
//        entityManager.clear();

        Optional<Document> result = documentRepository.findById(id);

        System.out.println("result: " + result);

        Document document = null;

        if (result.isPresent()) {
            document = result.get();
            System.out.println("result.get(): " + document);
        } else {
            throw new RuntimeException("Did not find document with id: " + id);
        }
        return new DocumentDTO(document.getId(), document.getDocumentCode(), document.getDocumentType(), document.getName(),
                document.getRevisionNumber(), document.getStatus(), document.getCreationDate(), document.getModificationDate(), document.getAuthor());
    }

    @Override
    public Document findByDocumentId(Long id) {
        return documentRepository.findById(id).get();
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
            row.createCell(2).setCellValue(d.getDocumentType());
            row.createCell(3).setCellValue(d.getName());
            row.createCell(4).setCellValue(d.getRevisionNumber());
            row.createCell(5).setCellValue(d.getStatus());
            row.createCell(6).setCellValue(d.getCreationDate());
            row.createCell(7).setCellValue(d.getModificationDate());
            row.createCell(8).setCellValue(d.getAuthor());
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
                if (cell.getRowIndex() == 0) {
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
    public List<DocumentDTO> findByUserId(String userId) {
        return documentRepository.findByUserId(userId).stream()
                .map(this::fromEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public void uploadFile(MultipartFile file, RedirectAttributes redirectAttributes, Long documentId, Model model) {
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
        DocumentDTO existingDocument = findById(document.getId());

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
        if (document.getAuthor() != null) {
            existingDocument.setAuthor(document.getAuthor());
        }

        // Convert DTO back to the enityty to update DB
        Document updatedDocument = convertToEntity(existingDocument);

        // Update the document in the database
        update(updatedDocument);
    }

    public Document convertToEntity(DocumentDTO documentDTO) {
        Document document = new Document();
        document.setId(documentDTO.getId());
        document.setDocumentCode(documentDTO.getDocumentCode());
        document.setDocumentType(documentDTO.getDocumentType());
        document.setName(documentDTO.getName());
        document.setRevisionNumber(documentDTO.getRevisionNumber());
        document.setStatus(documentDTO.getStatus());
        document.setCreationDate(documentDTO.getCreationDate());
        document.setModificationDate(documentDTO.getModificationDate());
        document.setAuthor(documentDTO.getAuthor());

        return document;
    }

    @Override
    public Document createDocument(String documentType, String name, long revisionNumber, String status, String creationDate,
                                   String modificationDate, String author, String link) {
        // Generate document code
        // Get the last index for the given documentType
        int lastIndex = 0;
        try {
            lastIndex = Integer.parseInt(documentRepository.findAll().stream()
                    .filter(x -> x.getDocumentCode().startsWith(documentType.toUpperCase().substring(0, 3)))
                    .reduce((first, second) -> second).get().getDocumentCode().substring(4));
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "NoSuchElementException was thrown when computing lastIndex");
        }

        // Create document
        Document document = new Document(documentType.toUpperCase().substring(0, 3) + "-" + ++lastIndex, documentType, name, revisionNumber,
                status, creationDate, modificationDate, author);

        return document;
    }

    @Override
    public ResponseEntity<byte[]> downloadDocumentTransactions() {
        List<TransactionDocument> transactions = documentTransactionRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Document transactions");

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
        rowHeader.createCell(1).setCellValue("Date");
        rowHeader.createCell(2).setCellValue("Modified by");
        rowHeader.createCell(3).setCellValue("Document code");
        rowHeader.createCell(4).setCellValue("Transaction type");

        // Add table body values
        for (TransactionDocument d : transactions) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(d.getId());
            row.createCell(1).setCellValue(d.getDate());
            row.createCell(2).setCellValue(d.getUser());
            row.createCell(3).setCellValue(d.getDocumentCode());
            row.createCell(4).setCellValue(d.getTransactionType());
        }

        // Apply style to cells
        for (Row row : sheet) {
            for (Cell cell : row) {
                // Apply border to all non empty cells
                if (cell.getCellType() != CellType.BLANK) {
                    cell.setCellStyle(styleBody);
                }
                // Apply style to the headers
                if (cell.getRowIndex() == 0) {
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
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Document transactions.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<byte[]> checkWord(MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException {
        // Read the input Word file
        InputStream inputStream = file.getInputStream();
        XWPFDocument document = new XWPFDocument(inputStream);

//        XWPFParagraph paragraph = document.createParagraph();
//        paragraph.createRun().setText("This is a new paragraph added to the document.");

        // Iterate through all paragraphs
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            // Update the text of the first run in the paragraph
            if (!paragraph.getRuns().isEmpty()) {
                List<MatchWord> listDate = checkDate(paragraph.getText());
                if (!listDate.isEmpty()) {
                    updateParagraph(paragraph, listDate, document);
                } else {
                    updateParagraph(paragraph, List.of(new MatchWord(paragraph.getText(),"999",
                            0,paragraph.getText().length(),paragraph.getText(),0)), document);
                }
            }

        }

        // Save the modified document to a new file
        File outputFile = new File("src/main/resources/donwloads/updated_document.docx");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            document.write(fos);
        }

        // Save the modified document to a ByteArrayOutputStream instead of a file
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);

        // Prepare the byte array to send as a response
        byte[] documentBytes = byteArrayOutputStream.toByteArray();

        // Set the response headers for file download
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=updated_document.docx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);
    }

    @Override
    public List<Document> importListAsCSV(MultipartFile file, RedirectAttributes redirectAttributes,
                                                  Model model) {


        try {
            // Create a temporary file
            File csvFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());

            System.out.println("#Working here 1");

            // Transfer the contents of the MultipartFile to the File
            file.transferTo(csvFile);
            System.out.println("#Working here 2");
            //Get the list of DocumentCSVRecord
            List<DocumentCSVRecord> documentCSVRecords = new CsvToBeanBuilder<DocumentCSVRecord>(new FileReader(csvFile))
                    .withType(DocumentCSVRecord.class)
                    .build().parse();

            List<Document> documents = documentCSVRecords.stream().map(this::convertDocumentCSVToEntity).toList();

            System.out.println(documents);


            return documents;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public Document findByDocumentCode(String documentCode) {
        return documentRepository.findByDocumentCode(documentCode);
    }

    public Document convertDocumentCSVToEntity(DocumentCSVRecord documentCSVRecord) {
        // Get current document id value from DB:
        long currentId = findAll().size();

        Document document = new Document();
//        document.setId(documentCSVRecord.getId());
        document.setDocumentCode(documentCSVRecord.getDocumentCode());
        document.setDocumentType(documentCSVRecord.getDocumentType());
        document.setName(documentCSVRecord.getName());
        document.setRevisionNumber(documentCSVRecord.getRevisionNumber());
        document.setStatus(documentCSVRecord.getStatus());
        document.setCreationDate(documentCSVRecord.getCreationDate());
        document.setModificationDate(documentCSVRecord.getModificationDate());
        document.setAuthor(documentCSVRecord.getAuthor());

        return document;
    }

    private void countWords(XWPFParagraph paragraph, List<MatchWord> listDate, XWPFDocument document) {
    }

    private void updateParagraph(XWPFParagraph paragraph, List<MatchWord> list, XWPFDocument document) {
        int runNumber = -1;

        XWPFRun r = null;
        String text = paragraph.getText();
        RevisedWord revisedWord = null;
        int startPoint = 0;

//         Clear existing runs in the paragraph
        while (paragraph.getRuns().size() > 0) {
            paragraph.removeRun(0);
        }

        // Process matches
        for (MatchWord m : list) {
            // GEt revised word
            revisedWord = m.getNewData();
            // Add text before the match
            if (startPoint < m.getStartIndex()) {
                r = paragraph.insertNewRun(++runNumber);
                r.setText(text.substring(startPoint, m.getStartIndex()));
                r.setFontFamily("Tahoma");
                r.setFontSize(12);
            }
            if (!revisedWord.getComment().equals("Not changed")) {
                // Add the new matched data with formatting
                r = paragraph.insertNewRun(++runNumber);
                r.setText(text.substring(m.getStartIndex(), m.getEndIndex()));
                r.setFontFamily("Tahoma");
                r.setColor("DC143C");
                r.setFontSize(12);

                // Update the starting point
                // Consider the length of abbreviation definition.
                startPoint = m.getEndIndex();

                // Add a comment to the document
                XWPFComments commentsPart = document.createComments();
                CTComment ctComment = commentsPart.getCtComments().addNewComment();

                // Set comment properties

                BigInteger commentId = BigInteger.valueOf(1); // Unique comment ID
                ctComment.setId(commentId);
                ctComment.setAuthor("AutoCheck");
                ctComment.addNewP().addNewR().addNewT().setStringValue(revisedWord.getComment());
                ctComment.setDate(Calendar.getInstance()); // Current date and time

                // Map the comment to a text range in the paragraph
                CTP ctp = paragraph.getCTP();

                // Start comment range
                CTMarkupRange commentStart = ctp.addNewCommentRangeStart();
                commentStart.setId(commentId);

                // End comment range
                CTMarkupRange commentEnd = ctp.addNewCommentRangeEnd();
                commentEnd.setId(commentId);

                // Add comment reference (visible indicator)
                CTR commentRef = ctp.addNewR();
                CTMarkup commentReference = commentRef.addNewCommentReference();
                commentReference.setId(commentId);
            } else {
                // Add the new matched data with formatting
                r = paragraph.insertNewRun(++runNumber);
                r.setText(text.substring(m.getStartIndex(), m.getEndIndex()));
                r.setFontFamily("Tahoma");
                r.setColor("259A0E");
                r.setFontSize(12);

                // Update the starting point
                // Consider the length of abbreviation definition.
                startPoint = m.getEndIndex();
            }


        }

        // Add remaining text after the last match
        if (startPoint < text.length()) {
            r = paragraph.insertNewRun(++runNumber);
            r.setText(text.substring(startPoint));
            r.setFontFamily("Tahoma");
//            r.setColor("259A0E");
            r.setFontSize(12);
//            System.out.println("COLOR CHANGED@@@@@@@@@@@@@@@@@");

        }
    }

    private List<MatchWord> checkDate(String runText) throws IOException {

        String regex = "\\b([A-Za-z]+) (\\d{1,2}), (\\d{4})\\b" +  // Groups 1, 2, 3
                "|\\b(\\d{1,2})-(\\d{1,2})-(\\d{4})\\b" +  // Groups 4, 5, 6
                "|\\b(January|February|March|April|May|June|July|August|September|October|November|December) ([1-9]|[12][0-9]|3[01])\\b" +
                // Groups 7, 8
                "|(\\([A-Z]+\\))" +  // Group 9
                "|(\\b[A-Z]{2,}\\b)";  // Group 10


        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(runText);
        List<MatchWord> listDate = new ArrayList<>();
        String matcherPattern = null;

        // Create a Matcher object
        while (matcher.find()) {
            // Determine which part of the regex matched
            if (matcher.group(1) != null) {
                matcherPattern = "1";
                System.out.println("Matched format: 'Month Day, Year'");
                listDate.add(new MatchWord(matcher.group(), matcherPattern, matcher.start(), matcher.end(), runText, matcher.group().length()));
            } else if (matcher.group(4) != null) {
                System.out.println("Matched format: 'Day-Month-Year'");
                matcherPattern = "4";
                listDate.add(new MatchWord(matcher.group(), matcherPattern, matcher.start(), matcher.end(), runText, matcher.group().length()));
            } else if (matcher.group(7) != null) {
                System.out.println("Matched format: 'Month Day'");
                matcherPattern = "7";
                listDate.add(new MatchWord(matcher.group(), matcherPattern, matcher.start(), matcher.end(), runText, matcher.group().length()));
            } else if (matcher.group(9) != null) {
                System.out.println("Matched format: '(XXX)'");
                matcherPattern = "9";
                listDate.add(new MatchWord(matcher.group(), matcherPattern, matcher.start(), matcher.end(), runText, matcher.group().length()));
            } else if (matcher.group(10) != null) {
                System.out.println("Matched format: 'XXX'");
                matcherPattern = "10";
                listDate.add(new MatchWord(matcher.group(), matcherPattern, matcher.start(), matcher.end(), runText, matcher.group().length()));
            }
        }


        return listDate;
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
                String oldPath = filePath + fileName;
                Path oldFilePath = Paths.get(oldPath);
                String newPath = actionType.equals("upload") ? "src/main/resources/documentsUploaded/" + fileName :
                        "src/main/resources/donwloads/" + fileName;

                Path newFilePath = Paths.get(newPath);

                File file = new File(oldPath);

                if (!file.exists()) {
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
