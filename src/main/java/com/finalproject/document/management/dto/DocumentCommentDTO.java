package com.finalproject.document.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class DocumentCommentDTO {
    private Long id;
    private Long documentId;
    private Long userId;
    private String date;
    private String comment;

}
