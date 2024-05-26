package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService{
    private DocumentRepository documentRepository;
    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }
    //Issue with using OPTIONAL ????
    @Override
    public Document findById(int id) {
        return documentRepository.getById(id);
    }

    @Override
    public void update(Document document) {
        documentRepository.save(document);
    }

    @Override
    public void deleteDocumentById(int id) {
        documentRepository.deleteById(id);
    }
}
