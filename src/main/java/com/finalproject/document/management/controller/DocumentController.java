package com.finalproject.document.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents")
public class DocumentController {



    @GetMapping("/getDocuments")
    public String getDocuments(){
        return
    }

}
