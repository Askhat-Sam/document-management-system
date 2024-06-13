package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.dto.DocumentDTO;
import com.finalproject.document.management.entity.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface DocumentService {
    List<DocumentDTO> findAll(Integer page, Integer size, String sortBy, String sortDirection, String  keyword, String column);
    List<DocumentDTO> findAll();

    DocumentDTO findById(Long id);
    Document findByDocumentId(Long id);

    void update(Document document);
    void save(Document document);

    void deleteDocumentById(Long id);

    String uploadDocument(String fileName, String path, String actionType);

    ResponseEntity<byte[]> downloadListAsExcel();
    List<DocumentDTO> findByUserId(String userId);

    void uploadFile(MultipartFile file, RedirectAttributes redirectAttributes, Long documentId, Model model);
    ResponseEntity<byte[]> viewFile(String link);
    ResponseEntity<byte[]> downloadFile(String link);
    void updateDocument(Document document);
    DocumentDTO fromEntityToDTO(Document document);
    Document convertToEntity(DocumentDTO documentDTO);
}
