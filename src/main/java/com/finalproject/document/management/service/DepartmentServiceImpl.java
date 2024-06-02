package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Department;
import com.finalproject.document.management.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    @Override
    public Department findById(int id) {
        return departmentRepository.getById(id);
    }
}
