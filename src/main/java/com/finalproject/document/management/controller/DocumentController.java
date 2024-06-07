package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.entity.DocumentComment;
import com.finalproject.document.management.entity.DocumentRevision;
import com.finalproject.document.management.entity.Search;
import com.finalproject.document.management.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//@RestController
@Controller
@RequestMapping("/document-management/documents")
@AllArgsConstructor
public class DocumentController {
    private DocumentService documentService;
    private DocumentTypeService documentTypeService;
    private DocumentStatusService documentStatusService;
    private final DocumentRevisionService documentRevisionService;
    private UserService userService;
    @Value("${headersDocument}")
    private List<String> headers;

    @GetMapping("/getDocuments")
    public String getDocuments(@RequestParam(name = "page", required = false) Integer page,
                               @RequestParam(name = "size", required = false) Integer size,
                               @RequestParam(name = "sortBy", required = false) String sortBy,
                               @RequestParam(name = "sortDirection", required = false) String sortDirection,
                               @RequestParam(name = "keyword", required = false) String keyword,
                               @RequestParam(name = "column", required = false) String column,
                               Model model) {
        Search search = new Search();

        List<Document> documents = documentService.findAll(page, size, sortBy, sortDirection, keyword, column);
        model.addAttribute("documents", documents);

        model.addAttribute("search", search);
        model.addAttribute("headers", headers);
        return "documents/documents";
    }


    @GetMapping("/getDocument/{id}")
    public Document getDocument(@PathVariable("id") Long id) {
        return documentService.findById(id);
    }


    @GetMapping("/view/{id}")
    public String viewDocument(@PathVariable Long id, Model model) {
        Document document = documentService.findById(id);
        List<DocumentRevision> revisions = documentRevisionService.findAllByDocumentId(document.getId());
        model.addAttribute("document", document);
        model.addAttribute("revisions", revisions);
        return "documents/view-document";
    }

    @GetMapping("/viewFile")
    public ResponseEntity<byte[]> openPdf(@RequestParam("link") String link) {
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
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("link") String link) {
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
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
                             @RequestParam("documentId") Long documentId,
                             Model model) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/uploadStatus";
        }

        try {
            // Save the file to the specified directory
            byte[] bytes = file.getBytes();
            Path path = Paths.get("src/main/resources/documentsUploaded/" + file.getOriginalFilename());
            Files.write(path, bytes);

            // Send the file path to the previous page link field
            redirectAttributes.addFlashAttribute("message", "documentsUploaded/" + file.getOriginalFilename());

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "File upload failed");
        }
        System.out.println("You successfully uploaded '" + file.getOriginalFilename() + "'");

        return "redirect:/document-management/documents/addNewRevisionPage?documentId=" + documentId;
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "documents/uploadStatus";
    }

    @GetMapping("/downloadListAsExcel")
    public ResponseEntity<byte[]> downloadListAsExcel() {
        return documentService.downloadListAsExcel();
    }

    @GetMapping("/addNewDocumentPage")
    public String addNewDocumentPage(Model model) {
        Document document = new Document();
        model.addAttribute("document", document);
        return "documents/add-document";
    }

    @PostMapping("/addNewDocument")
    public String addDocument(
            @RequestParam("documentCode") String documentCode,
            @RequestParam("documentTypeId") Long documentTypeId,
            @RequestParam("name") String name,
            @RequestParam("revisionNumber") Long revisionNumber,
            @RequestParam("statusId") Long statusId,
            @RequestParam("creationDate") String creationDate,
            @RequestParam("modificationDate") String modificationDate,
            @RequestParam("authorId") Long authorId) {

        String link = "src/main/resources/documentSource/";

        // Move the file into "documentsUploaded" folder
        String newLink = documentService.uploadDocument(name + ".pdf", link, "upload");
        // Fetch related entities
//        DocumentType documentType = documentTypeService.findById(documentTypeId);
//        DocumentStatus documentStatus = documentStatusService.findByID(statusId);
//        User author = userService.findById(authorId);

        // Create a new document
//        Document document = new Document(documentCode, documentType, name, revisionNumber, documentStatus, creationDate, modificationDate, author, newLink);

        // Create a new document
        Document document = new Document(documentCode, documentTypeId, name, revisionNumber, statusId, creationDate, modificationDate, authorId, link);
//        Document document = new Document(documentCode, documentType, name, revisionNumber, documentStatus,
//                creationDate, modificationDate, user, newLink);
        System.out.println(document);

        // Add document into database
        documentService.update(document);
        return "redirect:/document-management/documents/getDocuments";
    }


    @GetMapping("/downloadDocument")
    public String downloadDocument(@RequestParam Long id) {

        Document document = documentService.findById(id);

        // Move the file into "documentsUploaded" folder
        documentService.uploadDocument(document.getName() + ".pdf", "src/main/resources/documentsUploaded/", "download");

        return "redirect:/document-management/documents/getDocuments";
    }

    @GetMapping("/deleteDocument")
    public String deleteDocument(@RequestParam Long id) {

        Document document = documentService.findById(id);


        // Delete the file from "documentsUploaded" folder
        documentService.uploadDocument(document.getName() + ".pdf", document.getLink(), "delete");

        documentService.deleteDocumentById(id);

        return "Document with id: " + id + " has been successfully deleted from database";
    }


    @PostMapping("/updateDocument")
    public String updateDocument(@ModelAttribute Document document) {
        // Retrieve the existing document from the database
        Document existingDocument = documentService.findById(document.getId());

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
        if (document.getStatusId() != null) {
            existingDocument.setStatusId(document.getStatusId());
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
        documentService.update(existingDocument);

        return "redirect:/document-management/documents/getDocuments";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam Long documentId,
                             @RequestParam Long userId,
                             @RequestParam String comment) {
        Document document = documentService.findById(documentId);

        // Add comment to the document
        document.addComment(new DocumentComment(userId, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()), comment));

        documentService.update(document);
        return "Comment has been added into document with id " + documentId;
    }

    //to be removed previous version
//    @GetMapping("/downloadList")
//    public String downloadList() throws IOException, IllegalAccessException {
//        List<Document> documents = documentService.findAll();
//        documentService.downloadList(documents);
//        return "redirect:/document-management/documents/getDocuments";
//    }


    @GetMapping("/addNewRevisionPage")
    public String addNewRevisionPage(@RequestParam Long documentId,
                                     Model model) {
        model.addAttribute("documentId", documentId);
        DocumentRevision documentRevision = new DocumentRevision();
        model.addAttribute("documentRevision", documentRevision);
        return "documents/add-revision";
    }

    @PostMapping("/addNewRevision")
    public String addNewRevision(@RequestParam Long userId,
                                 @RequestParam Long documentId,
                                 @RequestParam String date,
                                 @RequestParam Long revisionNumber,
                                 @RequestParam Long statusId,
                                 @RequestParam String description,
                                 @RequestParam String link,
                                 @RequestParam Long validatingUserId) {
        Document document = documentService.findById(documentId);

        // Add comment to the document
        document.addRevision(new DocumentRevision(userId, revisionNumber, statusId, date, description, link, validatingUserId));

        documentService.update(document);
        return "redirect:/document-management/documents/view/" + documentId;
    }


//RestController

    //    @PostMapping("/addNewDocument")
//    public String addDocument(
//            @RequestParam("documentCode") String documentCode,
//            @RequestParam("documentTypeId") Long documentTypeId,
//            @RequestParam("name") String name,
//            @RequestParam("revisionNumber") int revisionNumber,
//            @RequestParam("statusId") Long statusId,
//            @RequestParam("creationDate") String creationDate,
//            @RequestParam("modificationDate") String modificationDate,
//            @RequestParam("authorId") Long authorId,
//            @RequestParam("link") String link) {
//
//        // Move the file into "documentsUploaded" folder
//        String newLink = documentService.uploadDocument(link, "upload");
//
//        // Get document type
//        DocumentType documentType = documentTypeService.findById(documentTypeId);
//
//        // Get document status object
//        DocumentStatus documentStatus = documentStatusService.findByID(statusId);
//
//        // Get user by authorId
//        User user = userService.findById(authorId);
//
//        // Create a new document
//        Document document = new Document(documentCode, documentType, name, revisionNumber, documentStatus,
//                creationDate, modificationDate, user, newLink);
//
//        // Add document into database
//        documentService.update(document);
//        return "Document with reference: " + documentCode + " has been added into database";
//    }

    //    @GetMapping("/getDocuments")
//    public List<Document>  getDocuments(@RequestParam(name = "page", required = false) Integer page,
//                               @RequestParam(name = "size", required = false) Integer size,
//                               @RequestParam(name = "sortBy", required = false) String sortBy,
//                               @RequestParam(name = "sortDirection", required = false) String sortDirection,
//                               @RequestParam(name = "keyword", required = false) String keyword,
//                               @RequestParam(name = "column", required = false) String column) {
//        List<Document> documents = documentService.findAll(page, size, sortBy, sortDirection, keyword, column);
//        return documents;
//    }
//    @PostMapping("/addRevision")
//    public String addRevision(
//                             @RequestParam Long userId,
//                             @RequestParam Long documentId,
//                             @RequestParam String date,
//                              @RequestParam Long revisionNumber,
//                             @RequestParam Long statusId,
//                              @RequestParam String description,
//                              @RequestParam String link,
//                              @RequestParam Long validatingUserId) {
//        Document document = documentService.findById(documentId);
//
//        // Add comment to the document
//        document.addRevision(new DocumentRevision(userId, revisionNumber, statusId, date, description, link, validatingUserId));
//
//        documentService.update(document);
//        return "Revision has been added into document with id " + documentId;
//    }


//    @GetMapping("/newDocument")
//    public String newDocument(){
//        return "documents/add-document";
//    }

    //    @PostMapping("/updateDocument")
//    public String updateDocument(
//            @RequestParam(name="id", required = false) Long id,
//            @RequestParam(name="documentCode", required = false) String documentCode,
//            @RequestParam(name="documentTypeId", required = false) Long documentTypeId,
//            @RequestParam(name="name", required = false) String name,
//            @RequestParam(name="revisionNumber", required = false) Long revisionNumber,
//            @RequestParam(name="statusId", required = false) Long statusId,
//            @RequestParam(name="creationDate", required = false) String creationDate,
//            @RequestParam(name="modificationDate", required = false) String modificationDate,
//            @RequestParam(name="authorId", required = false) Long authorId,
//            @RequestParam(name="link", required = false) String link) {
//
//        // Move the file into "documentsUploaded" folder
////        String newLink = documentService.uploadDocument(link, "upload");
//
//        Document document = documentService.findById(id);
//
//        // Get document type
//        if (documentTypeId!=null) {
//            DocumentType documentType = documentTypeService.findById(documentTypeId);
//            document.setDocumentType(documentType);
//        }
//        // Get document status object
//        if (statusId!=null) {
//            DocumentStatus documentStatus = documentStatusService.findByID(statusId);
//            document.setDocumentStatus(documentStatus);
//        }
//        // Get user by authorId
//        if (authorId!=null) {
//            User user = userService.findById(authorId);
//        }
//
//        if (documentCode != null) {
//            document.setDocumentCode(documentCode);
//        } else if (name != null) {
//            document.setName(name);
//        } else if (String.valueOf(revisionNumber) != null) {
//            document.setRevisionNumber(revisionNumber);
//        } else if (creationDate != null) {
//            document.setCreationDate(creationDate);
//        } else if (modificationDate != null) {
//            document.setModificationDate(modificationDate);
//        } else if (link != null) {
//            document.setLink(link);
//        }
//        documentService.update(document);
//        return "Document with id: " + id + " has been successfully updated in database";
//    }

}
