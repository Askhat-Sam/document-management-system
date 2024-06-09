package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.entity.Department;
import com.finalproject.document.management.repository.DepartmentRepository;
import com.finalproject.document.management.service.interfaces.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    @Override
    public Department findById(Long id) {
        return departmentRepository.getById(id);
    }
}
