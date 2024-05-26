package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
