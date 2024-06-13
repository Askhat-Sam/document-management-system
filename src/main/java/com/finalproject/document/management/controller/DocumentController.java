package com.finalproject.document.management.controller;

import com.finalproject.document.management.dto.DocumentCommentDTO;
import com.finalproject.document.management.dto.DocumentDTO;
import com.finalproject.document.management.dto.DocumentRevisionDTO;
import com.finalproject.document.management.dto.DocumentValidationDTO;
import com.finalproject.document.management.entity.*;
import com.finalproject.document.management.service.implementations.DocumentValidationServiceImpl;
import com.finalproject.document.management.service.interfaces.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/document-management/documents")
@AllArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    private final UserService userService;
    private final DocumentCommentService documentCommentService;
    private final DocumentRevisionService documentRevisionService;
    private final DocumentValidationService documentValidationService;
    private final DocumentValidationServiceImpl documentValidationServiceImpl;
    @Value("${headersDocument}")
    private List<String> headers;
    @Value("${documentStatuses}")
    private List<String> documentStatuses;
    @Value("${documentTypes}")
    private List<String> documentTypes;
    @Value("${documentRevisionStatuses}")
    private List<String> documentRevisionStatuses;;
    private static final Logger logger = Logger.getLogger(DocumentController.class.getName());
    @GetMapping("/getDocuments")
    public String getDocuments(@RequestParam(name = "page", required = false) Integer page,
                               @RequestParam(name = "size", required = false) Integer size,
                               @RequestParam(name = "sortBy", required = false) String sortBy,
                               @RequestParam(name = "sortDirection", required = false) String sortDirection,
                               @RequestParam(name = "keyword", required = false) String keyword,
                               @RequestParam(name = "column", required = false) String column,
                               Model model) {
        Search search = new Search();

        List<DocumentDTO> documents = documentService.findAll(page, size, sortBy, sortDirection, keyword, column);
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Long countAwaitingValidation = documentValidationService.countByStatusAndUserId("Awaiting validation", loggedUser);
        model.addAttribute("documents", documents);
        model.addAttribute("search", search);
        model.addAttribute("headers", headers);
        model.addAttribute("countAwaitingValidation", countAwaitingValidation);

        return "documents/documents";
    }


    @GetMapping("/getDocument/{id}")
    public DocumentDTO getDocument(@PathVariable("id") Long id) {
        return documentService.findById(id);
    }


    @GetMapping("/view/{id}")
    public String viewDocument(@PathVariable Long id, Model model) {
        DocumentDTO document = documentService.findById(id);
        List<DocumentRevisionDTO> revisions = documentRevisionService.findAllByDocumentId(document.getId());
        List<DocumentCommentDTO> comments = documentCommentService.findAllByDocumentId(id);
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Long countAwaitingValidation = documentValidationService.countByStatusAndUserId("Awaiting validation", loggedUser);
        DocumentComment comment = new DocumentComment();
        List<String> userIds = userService.findAllUserIds();
        model.addAttribute("document", document);
        model.addAttribute("revisions", revisions);
        model.addAttribute("comments", comments);
        model.addAttribute("comment", comment);
        model.addAttribute("documentStatuses", documentStatuses);
        model.addAttribute("documentTypes", documentTypes);
        model.addAttribute("userIds", userIds);

        model.addAttribute("countAwaitingValidation", countAwaitingValidation);
        return "documents/view-document";
    }

    @GetMapping("/viewFile")
    public ResponseEntity<byte[]> viewFile(@RequestParam("link") String link) {
        return documentService.viewFile(link);
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("link") String link) {
        return documentService.downloadFile(link);
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
                             @RequestParam("documentId") Long documentId,
                             Model model) {
        documentService.uploadfile(file, redirectAttributes, documentId, model);
        return "redirect:/document-management/documents/addNewRevisionPage?documentId=" + documentId;
    }

    @GetMapping("/downloadListAsExcel")
    public ResponseEntity<byte[]> downloadListAsExcel() {
        return documentService.downloadListAsExcel();
    }

    @GetMapping("/addNewDocumentPage")
    public String addNewDocumentPage(Model model) {
        Document document = new Document();
        List<String> userIds = userService.findAllUserIds();
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Long countAwaitingValidation = documentValidationService.countByStatusAndUserId("Awaiting validation", loggedUser);
        model.addAttribute("document", document);
        model.addAttribute("documentTypes", documentTypes);
        model.addAttribute("documentStatuses", documentStatuses);
        model.addAttribute("userIds", userIds);
        model.addAttribute("countAwaitingValidation", countAwaitingValidation);
        return "documents/add-document";
    }

    @PostMapping("/addNewDocument")
    public String addDocument(
            @RequestParam("documentCode") String documentCode,
            @RequestParam("documentType") String documentType,
            @RequestParam("name") String name,
            @RequestParam("revisionNumber") Long revisionNumber,
            @RequestParam("status") String status,
            @RequestParam("creationDate") String creationDate,
            @RequestParam("modificationDate") String modificationDate,
            @RequestParam("author") String author) {

        String link = "TBD";

        // Create a new document
        Document document = new Document(documentCode, documentType, name, revisionNumber, status, creationDate, modificationDate, author, link);

        // Add document into database
        documentService.save(document);
        return "redirect:/document-management/documents/getDocuments";
    }


    @GetMapping("/downloadDocument")
    public String downloadDocument(@RequestParam Long id) {

        DocumentDTO document = documentService.findById(id);

        // Move the file into "documentsUploaded" folder
        documentService.uploadDocument(document.getName() + ".pdf", "src/main/resources/documentsUploaded/", "download");

        return "redirect:/document-management/documents/getDocuments";
    }

    @GetMapping("/deleteDocument") ///??????????????????????????????
    public String deleteDocument(@RequestParam Long id) {

        DocumentDTO document = documentService.findById(id);


        // Delete the file from "documentsUploaded" folder
//        documentService.uploadDocument(document.getName() + ".pdf", document.getLink(), "delete");

        documentService.deleteDocumentById(id);

        return "Document with id: " + id + " has been successfully deleted from database";
    }


    @PostMapping("/updateDocument")
    public String updateDocument(@ModelAttribute Document document) {
        // Retrieve the existing document from the database

        documentService.updateDocument(document);

        return "redirect:/document-management/documents/getDocuments";
    }

    @GetMapping("/addNewCommentPage")
    public String addNewCommentPage(@RequestParam Long documentId,
                                    Model model) {

        DocumentComment comment = new DocumentComment();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long countAwaitingValidation = documentValidationService.countByStatusAndUserId("Awaiting validation", userId);
        model.addAttribute("documentId", documentId);
        model.addAttribute("comment", comment);
        model.addAttribute("userId", userId);
        model.addAttribute("countAwaitingValidation", countAwaitingValidation);
        return "documents/add-comment";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam Long documentId,
                             @RequestParam String userId,
                             @RequestParam String comment) {
        DocumentDTO document = documentService.findById(documentId);

        DocumentComment documentComment = new DocumentComment(documentId, userId,
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()),
                comment);

        // Convert DTO back to entity before update
        Document documentUpdated = documentService.convertToEntity(document);

        // Add comment to the document
        documentUpdated.addComment(documentComment);

        documentService.update(documentUpdated);
        return "redirect:/document-management/documents/view/" + documentId;
    }

    @GetMapping("/addNewRevisionPage")
    public String addNewRevisionPage(@RequestParam Long documentId,
                                     Model model) {
        List<String> userIds = userService.findAllUserIds();
        DocumentRevision documentRevision = new DocumentRevision();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long countAwaitingValidation = documentValidationService.countByStatusAndUserId("Awaiting validation", userId);
        model.addAttribute("userIds", userIds);
        model.addAttribute("userId", userId);
        model.addAttribute("documentId", documentId);
        model.addAttribute("documentRevision", documentRevision);
        model.addAttribute("documentRevisionStatuses", documentRevisionStatuses);
        model.addAttribute("countAwaitingValidation", countAwaitingValidation);

        return "documents/add-revision";
    }

    @PostMapping("/addNewRevision")
    public String addNewRevision(@RequestParam String userId,
                                 @RequestParam Long documentId,
                                 @RequestParam String date,
                                 @RequestParam Long revisionNumber,
                                 @RequestParam String status,
                                 @RequestParam String description,
                                 @RequestParam String link,
                                 @RequestParam String validatingUser) {
        DocumentDTO document = documentService.findById(documentId);

        // Add revision to the document
        DocumentRevision documentRevision =new DocumentRevision(userId, revisionNumber, status, date, description, link, validatingUser);

        // Convert DTO back to entity before update
        Document documentUpdated = documentService.convertToEntity(document);

        documentUpdated.addRevision(documentRevision);

        DocumentValidation documentValidation = new DocumentValidation(document.getDocumentCode(), documentId, document.getName(),
                revisionNumber, validatingUser, "Awaiting validation");


        documentService.update(documentUpdated);
        documentValidationService.save(documentValidation);
        return "redirect:/document-management/documents/view/" + documentId;
    }
    @RequestMapping("/getDocumentValidations")
    public String documentValidationPage(Model model) {
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        List<DocumentValidationDTO> documentValidations = documentValidationService.findAllByUserId(loggedUser);
        Long countAwaitingValidation = documentValidationService.countByStatusAndUserId("Awaiting validation", loggedUser);
        model.addAttribute("countAwaitingValidation", countAwaitingValidation);
        model.addAttribute("documentValidations",documentValidations);

        return "documents/document-validations";
    }

    @GetMapping("/view-validation/{id}")
    public String viewValidation(@PathVariable Long id, Model model) {
        DocumentValidationDTO documentValidation = documentValidationService.findById(id);
        DocumentDTO document = documentService.findById(documentValidation.getDocumentId());
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Long countAwaitingValidation = documentValidationService.countByStatusAndUserId("Awaiting validation", loggedUser);
        model.addAttribute("countAwaitingValidation", countAwaitingValidation);
        model.addAttribute("documentValidation", documentValidation);
        model.addAttribute("documentRevisionStatuses", documentRevisionStatuses);
        model.addAttribute("document", document);

        return "documents/view-document-validations";
    }

    @PostMapping("/updateRevision")
    public String updateRevision(@RequestParam Long id,
                                 @RequestParam String status,
                                 Model model) {
        DocumentValidationDTO documentValidationDTO = documentValidationService.findById(id);
        // Update status in document validation
        DocumentValidation documentValidation = DocumentValidationServiceImpl.fromDTOToEntity(documentValidationDTO);
        documentValidation.setStatus(status);
        // Update status in document revision
        DocumentRevision documentRevision = documentRevisionService.findByRevisionNumber(documentValidation.getRevisionNumber());
        documentRevision.setStatus(status);
        documentRevisionService.save(documentRevision);
        documentValidationService.save(documentValidation);

        List<DocumentValidationDTO> documentValidations = documentValidationService.findAll();
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Long countAwaitingValidation = documentValidationService.countByStatusAndUserId("Awaiting validation", loggedUser);

        model.addAttribute("countAwaitingValidation", countAwaitingValidation);
        model.addAttribute("documentValidations",documentValidations);

        return "documents/document-validations";
    }
}
