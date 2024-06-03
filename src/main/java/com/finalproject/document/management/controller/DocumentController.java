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

    @GetMapping("/getDocument/{id}")
    public Document getDocument(@PathVariable("id") int id) {
        return documentService.findById(id);
    }

    @PostMapping("/addNewDocument")
    public String addDocument(
            @RequestParam("documentCode") String documentCode,
            @RequestParam("documentTypeId") int documentTypeId,
            @RequestParam("name") String name,
            @RequestParam("revisionNumber") int revisionNumber,
            @RequestParam("statusId") int statusId,
            @RequestParam("creationDate") String creationDate,
            @RequestParam("modificationDate") String modificationDate,
            @RequestParam("authorId") int authorId,
            @RequestParam("link") String link) {

        // Move the file into "documentsUploaded" folder
        String newLink = documentService.uploadDocument(link, "upload");

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

    @GetMapping("/deleteDocument")
    public String deleteDocument(@RequestParam int id) {

        Document document = documentService.findById(id);


        // Delete the file from "documentsUploaded" folder
        documentService.uploadDocument(document.getLink(), "delete");

        documentService.deleteDocumentById(id);

        return "Document with id: " + id + " has been successfully deleted from database";
    }

    @PostMapping("/updateDocument")
    public String updateDocument(
            @RequestParam("id") int id,
            @RequestParam("documentCode") String documentCode,
            @RequestParam("documentTypeId") int documentTypeId,
            @RequestParam("name") String name,
            @RequestParam("revisionNumber") int revisionNumber,
            @RequestParam("statusId") int statusId,
            @RequestParam("creationDate") String creationDate,
            @RequestParam("modificationDate") String modificationDate,
            @RequestParam("authorId") int authorId,
            @RequestParam("link") String link) {

        // Move the file into "documentsUploaded" folder
        String newLink = documentService.uploadDocument(link, "upload");

        // Get document type
        DocumentType documentType = documentTypeService.findById(documentTypeId);

        // Get document status object
        DocumentStatus documentStatus = documentStatusService.findByID(statusId);

        // Get user by authorId
        User user = userService.findById(authorId);

        Document document = documentService.findById(id);
        if (documentCode != null) {
            document.setDocumentCode(documentCode);
        } else if (documentType != null) {
            document.setDocumentType(documentType);
        } else if (name != null) {
            document.setName(name);
        } else if (String.valueOf(revisionNumber) != null) {
            document.setRevisionNumber(revisionNumber);
        } else if (documentStatus!= null) {
            document.setDocumentStatus(documentStatus);
        } else if (creationDate != null) {
            document.setCreationDate(creationDate);
        } else if (modificationDate != null) {
            document.setModificationDate(modificationDate);
        } else if (user != null) {
            document.setAuthorId(user.getUserId());
        } else if (link != null) {
            document.setLink(link);
        }
        documentService.update(document);
        return "Document with id: " + id + " has been successfully updated in database";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam int documentId,
                             @RequestParam String userId,
                             @RequestParam String comment) {
        Document document = documentService.findById(documentId);

        // Add comment to the document
        document.add(new DocumentComment(userId, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()), comment));

        documentService.update(document);
        return "Comment has been added into document with id " + documentId;
    }
    @GetMapping("/downloadList")
    public String downloadList() throws IOException, IllegalAccessException {
        List<Document> documents = documentService.findAll();
        return documentService.downloadList(documents);
    }
}
