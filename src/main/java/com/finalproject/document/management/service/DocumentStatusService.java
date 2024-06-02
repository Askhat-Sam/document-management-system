package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.DocumentStatus;

public interface DocumentStatusService {
    DocumentStatus findByID(int id);
}
