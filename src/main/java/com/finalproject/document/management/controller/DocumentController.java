package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.entity.Role;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/document-management/documents")
@AllArgsConstructor
public class DocumentController {
    private DocumentService documentService;


    @GetMapping("/getDocuments")
    public List<Document> getDocuments(){
        return documentService.findAll();
    }

    @GetMapping("/getDocument/{id}")
    public Optional<Document> getDocument(@PathVariable("id") int id){
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
            @RequestParam("processOwner") String processOwner){

        // Create a new document
        Document document = new Document(documentReference,type,name, revision,status, issueDate, revisionDate,revisionInterval,processOwner);

        documentService.update(document);

        return "Document with reference: " + documentReference + " has been added into database";
    }

}
