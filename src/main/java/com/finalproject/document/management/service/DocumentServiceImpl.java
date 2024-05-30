package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private DocumentRepository documentRepository;

    private static final Logger logger = Logger.getLogger(DocumentServiceImpl.class.getName());

    @Override
    public List<Document> findAll(Integer pageNo, Integer pageSize, String sortBy, String sortDirection,  String  keyword, String column) {
        Pageable pageable = doPagingAndSorting(pageNo, pageSize, sortBy, sortDirection);
        List<Document> documents;
        System.out.println("___________________");
        System.out.println(keyword);
        System.out.println(column);
        if (pageable != null) {
            documents = documentRepository.findAll(pageable).toList();
        } else {
            documents = documentRepository.findAll();
        }

        List<Document> documentsFiltered = new ArrayList<>();

        //if searching by keyword in certain column. Uses "contains" to search by the part of word.
        if (keyword!=null&&column!=null){
            switch(column) {
                case "id": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getId()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "documentReference": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getDocumentReference()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "type": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getType()).contains(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "name": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getName()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "status": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getStatus()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "issueDate": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getIssueDate()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "revisionDate": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getRevisionDate()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "revisionInterval": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getRevisionInterval()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "processOwner": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getProcessOwner()).equals(keyword)).collect(Collectors.toList());
                    return documentsFiltered;
                case "link": documentsFiltered = documents.stream().filter(d -> String.valueOf(d.getRevisionInterval()).equals(keyword)).collect(Collectors.toList());
            }
            return documentsFiltered;
        }
        return documents;
    }

    //Issue with using OPTIONAL ????
    @Override
    public Document findById(int id) {

        Optional<Document> result =documentRepository.findById(id);

        System.out.println("result: " + result);

        Document document = null;

        if (result.isPresent()) {
            document = result.get();
            System.out.println("result.get(): " + document);
        } else {
            throw new RuntimeException("Did not find document with id: " + id);
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

    @Override
    public String uploadDocument(String path, String actionType) {
        return moveFile(path, actionType);
    }

    private static Pageable doPagingAndSorting(Integer page, Integer size, String sortBy, String sortDirection) {
        if (sortBy != null) {
            Sort sort = Sort.by(Sort.DEFAULT_DIRECTION, sortBy);
            // Defining the sort direction: A - ascending and D - descending. If direction not provided, default direction to be applied
            if (sortDirection != null) {
                if (sortDirection.equals("A")) {
                    sort = Sort.by(ASC, sortBy);
                } else if (sortDirection.equals("D")) {
                    sort = Sort.by(DESC, sortBy);
                }
            }

            Pageable pageable;
            if (page != null) {
                pageable = PageRequest.of(page, size, sort);
            } else {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
            }
            return pageable;
        } else {
            Pageable pageable;
            if (page != null) {
                pageable = PageRequest.of(page, size);
            } else {
                pageable = PageRequest.of(0, Integer.MAX_VALUE);
            }
            return pageable;
        }
    }

    private static String moveFile(String filePath, String actionType) {

        Path oldFilePath = Paths.get(filePath);

        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

        String newPath = "src/main/resources/documentsUploaded/" + fileName;

        Path newFilePath = Paths.get(newPath);

        File file = actionType.equals("upload") ? new File(filePath): new File(newPath);

        if (!file.exists()){
            logger.log(Level.SEVERE, "File cannot be found");
        } else {
            if (actionType.equals("upload")) {
                try {
                    Files.copy(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(oldFilePath);
                    return "src/main/resources/documentsUploaded/" + fileName;
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "File cannot be uploaded");
                }
            } else if (actionType.equals("delete")) {
                try {
                    Files.delete(newFilePath);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "File cannot be deleted");
                }
            }
        }
        return null;
    }
}
