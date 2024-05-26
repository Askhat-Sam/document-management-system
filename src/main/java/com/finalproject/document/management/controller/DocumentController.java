package com.finalproject.document.management.controller;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/document-management/documents")
@AllArgsConstructor
public class DocumentController {
    private DocumentService documentService;


    @GetMapping("/getDocuments")
    public List<Document> getDocuments(){
        return documentService.findAll();
    }

}
