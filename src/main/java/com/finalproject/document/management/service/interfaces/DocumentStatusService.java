package com.finalproject.document.management.service.interfaces;

import com.finalproject.document.management.entity.DocumentStatus;

public interface DocumentStatusService {
    DocumentStatus findByID(Long id);
}
