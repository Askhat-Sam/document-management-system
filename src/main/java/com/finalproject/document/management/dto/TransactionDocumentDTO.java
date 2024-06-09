package com.finalproject.document.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class TransactionDocumentDTO {
    private Long id;
    private String date;
    private Long userId;
    private String transactionType;
}
