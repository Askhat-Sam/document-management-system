package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document-management/documents")
@AllArgsConstructor
public class DocumentController {
    private DocumentService documentService;


    @GetMapping("/getDocuments")
    public List<Document> getDocuments(@RequestParam(name = "page", required = false) Integer page,
                                       @RequestParam(name = "size", required = false) Integer size,
                                       @RequestParam(name = "sortBy", required = false) String sortBy,
                                       @RequestParam(name = "sortDirection", required = false) String sortDirection) {
        return documentService.findAll(page, size, sortBy, sortDirection);
    }

    @GetMapping("/getDocument/{id}")
    public Document getDocument(@PathVariable("id") int id) {
        return documentService.findById(id);
    }

    @PostMapping("/addNewDocument")
    public String addDocument(
            @RequestParam("documentReference") String documentReference,
            @RequestParam("type") String type,
            @RequestParam("name") String name,
            @RequestParam("revision") int revision,
            @RequestParam("status") String status,
            @RequestParam("issueDate") String issueDate,
            @RequestParam("revisionDate") String revisionDate,
            @RequestParam("revisionInterval") int revisionInterval,
            @RequestParam("processOwner") String processOwner,
            @RequestParam("link") String link) {

        // Create a new document
        Document document = new Document(documentReference, type, name, revision, status, issueDate, revisionDate, revisionInterval, processOwner, link);

        documentService.update(document);

        return "Document with reference: " + documentReference + " has been added into database";
    }

    @GetMapping("/deleteDocument")
    public String deleteDocument(@RequestParam int id) {
        documentService.deleteDocumentById(id);
        return "Document with id: " + id + " has been successfully deleted from database";
    }

    @PostMapping("/updateDocument")
    public String updateDocument(
            @RequestParam(name = "id", required = false) int id,
            @RequestParam(name = "documentReference", required = false) String documentReference,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "revision", required = false) Integer revision,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "issueDate", required = false) String issueDate,
            @RequestParam(name = "revisionDate", required = false) String revisionDate,
            @RequestParam(name = "revisionInterval", required = false) Integer revisionInterval,
            @RequestParam(name = "processOwner", required = false) String processOwner) {


        Document document = documentService.findById(id);
        if (documentReference != null) {
            document.setDocumentReference(documentReference);
        }
        if (type != null) {
            document.setType(type);
        }
        if (name != null) {
            document.setName(name);
        }
        if (revision != null) {
            document.setRevision(revision);
        }
        if (status != null) {
            document.setStatus(status);
        }
        if (issueDate != null) {
            document.setIssueDate(issueDate);
        }
        if (revisionInterval != null) {
            document.setRevisionInterval(revisionInterval);
        }
        if (processOwner != null) {
            document.setProcessOwner(processOwner);
        }

        documentService.update(document);

        return "Document with id: " + id + " has been successfully updated in database";
    }

}
