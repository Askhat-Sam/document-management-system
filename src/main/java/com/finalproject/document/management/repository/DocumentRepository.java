package com.finalproject.document.management.repository;

import com.finalproject.document.management.dto.DocumentCommentDTO;
import com.finalproject.document.management.entity.Document;
import com.finalproject.document.management.entity.DocumentComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query(value="SELECT * FROM document WHERE document.author = :author", nativeQuery = true)
    List<Document> findByUserId(String author);

    Document findByDocumentCode(String documentCode);
}
