package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.*;
import com.finalproject.document.management.service.DocumentService;
import com.finalproject.document.management.service.DocumentStatusService;
import com.finalproject.document.management.service.DocumentTypeService;
import com.finalproject.document.management.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    private UserService userService;

    @GetMapping("/getDocuments")
    public String getDocuments(@RequestParam(name = "page", required = false) Integer page,
                                       @RequestParam(name = "size", required = false) Integer size,
                                       @RequestParam(name = "sortBy", required = false) String sortBy,
                                       @RequestParam(name = "sortDirection", required = false) String sortDirection,
                                       @RequestParam(name = "keyword", required = false) String keyword,
                                       @RequestParam(name = "column", required = false) String column,
                                        Model model) {
        List<Document> documents = documentService.findAll(page, size, sortBy, sortDirection, keyword, column);
        model.addAttribute("documents", documents);
        return "documents/documents";
    }
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

    @GetMapping("/getDocument/{id}")
    public Document getDocument(@PathVariable("id") Long id) {
        return documentService.findById(id);
    }

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

    @PostMapping("/addNewDocument")
    public String addDocument(
            @RequestParam("documentCode") String documentCode,
            @RequestParam("documentTypeId") Long documentTypeId,
            @RequestParam("name") String name,
            @RequestParam("revisionNumber") Long revisionNumber,
            @RequestParam("statusId") Long statusId,
            @RequestParam("creationDate") String creationDate,
            @RequestParam("modificationDate") String modificationDate,
            @RequestParam("authorId") Long authorId,
            @RequestParam("link") String link) {

        // Move the file into "documentsUploaded" folder
        String newLink = documentService.uploadDocument(name, link, "upload");

        // Get document type
        DocumentType documentType = documentTypeService.findById(documentTypeId);

        // Get document status object
        DocumentStatus documentStatus = documentStatusService.findByID(statusId);

        // Get user by authorId
        User user = userService.findById(authorId);

        // Create a new document
        Document document = new Document(documentCode, documentType, name, revisionNumber, documentStatus,
                creationDate, modificationDate, user, newLink);

        // Add document into database
        documentService.update(document);
        return "Document with reference: " + documentCode + " has been added into database";
    }

    @GetMapping("/downloadDocument")
    public String downloadDocument(@RequestParam Long id) {

        Document document = documentService.findById(id);

        // Move the file into "documentsUploaded" folder
        documentService.uploadDocument(document.getName() + ".pdf","src/main/resources/documentSource", "upload");

        return "redirect:/document-management/users/getUsers";
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
    public String updateDocument(
            @RequestParam(name="id", required = false) Long id,
            @RequestParam(name="documentCode", required = false) String documentCode,
            @RequestParam(name="documentTypeId", required = false) Long documentTypeId,
            @RequestParam(name="name", required = false) String name,
            @RequestParam(name="revisionNumber", required = false) Long revisionNumber,
            @RequestParam(name="statusId", required = false) Long statusId,
            @RequestParam(name="creationDate", required = false) String creationDate,
            @RequestParam(name="modificationDate", required = false) String modificationDate,
            @RequestParam(name="authorId", required = false) Long authorId,
            @RequestParam(name="link", required = false) String link) {

        // Move the file into "documentsUploaded" folder
//        String newLink = documentService.uploadDocument(link, "upload");

        Document document = documentService.findById(id);

        // Get document type
        if (documentTypeId!=null) {
            DocumentType documentType = documentTypeService.findById(documentTypeId);
            document.setDocumentType(documentType);
        }
        // Get document status object
        if (statusId!=null) {
            DocumentStatus documentStatus = documentStatusService.findByID(statusId);
            document.setDocumentStatus(documentStatus);
        }
        // Get user by authorId
        if (authorId!=null) {
            User user = userService.findById(authorId);
        }

        if (documentCode != null) {
            document.setDocumentCode(documentCode);
        } else if (name != null) {
            document.setName(name);
        } else if (String.valueOf(revisionNumber) != null) {
            document.setRevisionNumber(revisionNumber);
        } else if (creationDate != null) {
            document.setCreationDate(creationDate);
        } else if (modificationDate != null) {
            document.setModificationDate(modificationDate);
        } else if (link != null) {
            document.setLink(link);
        }
        documentService.update(document);
        return "Document with id: " + id + " has been successfully updated in database";
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
    @GetMapping("/downloadList")
    public String downloadList() throws IOException, IllegalAccessException {
        List<Document> documents = documentService.findAll();
        documentService.downloadList(documents);
        return "redirect:/document-management/users/getUsers";
    }

    @PostMapping("/addRevision")
    public String addRevision(
                             @RequestParam Long userId,
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
        return "Revision has been added into document with id " + documentId;
    }


//    @GetMapping("/newDocument")
//    public String newDocument(){
//        return "documents/add-document";
//    }

}
