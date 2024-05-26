package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService{
    private DocumentRepository documentRepository;
    @Override
    public List<Document> findAll(Integer pageNo,  Integer pageSize, String sortBy) {
        Pageable pageable = doPagingAndSorting(pageNo,  pageSize, sortBy);
        List<Document> documents;
        if (pageable!=null){
            documents = documentRepository.findAll(pageable).toList();
        } else {
            documents = documentRepository.findAll();
        }
        return documents;
    }
    //Issue with using OPTIONAL ????
    @Override
    public Document findById(int id) {

        Optional<Document> result = documentRepository.findById(id);

        System.out.println("result: " + result);

        Document document = null;

        if (result.isPresent()){
            document = result.get();
            System.out.println("result.get(): " + document);
        } else {
            throw new RuntimeException("Did not find user id: "+ id);
        }
        return document;
    }

    @Override
    public void update(Document document) {
        documentRepository.save(document);
    }

    @Override
    public void deleteDocumentById(int id) {
        documentRepository.deleteById(id);
    }

    private static Pageable doPagingAndSorting(Integer pageNo, Integer pageSize, String sortBy){
        if (sortBy != null) {
            Sort sort = Sort.by(sortBy);

            Pageable pageable;
            if (pageNo != null) {
                pageable = PageRequest.of(pageNo, pageSize, sort);
            } else {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
            }
            return pageable;
        } else {
            return null;
        }
    }
}
