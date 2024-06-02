package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Document, Integer> {
}
