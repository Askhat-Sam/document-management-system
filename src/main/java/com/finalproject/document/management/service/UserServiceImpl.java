package com.finalproject.document.management.service;

import com.finalproject.document.management.entity.Department;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.repository.DepartmentRepository;
import com.finalproject.document.management.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EntityManager entityManager;

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    //    public UserServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
    @Override
    public List<User> findAll(Integer page, Integer size, String sortBy, String sortDirection, String keyword, String column) {
        Pageable pageable = doPagingAndSorting(page, size, sortBy, sortDirection);
        List<User> users;
        if (pageable != null) {
            users = userRepository.findAll(pageable).toList();
        } else {
            users = userRepository.findAll();
        }

        List<User> usersFiltered = new ArrayList<>();

        //if searching by keyword in certain column. Uses "contains" to search by the part of word.
        if (keyword != null && column != null) {
            switch (column) {
                case "Id":
                    usersFiltered = users.stream().filter(u -> Long.toString(u.getId()).contains(keyword)).collect(Collectors.toList());
                    return usersFiltered;
                case "User Id":
                    usersFiltered = users.stream().filter(u -> u.getUserId().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "First name":
                    usersFiltered = users.stream().filter(u -> u.getFirstName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "Last name":
                    usersFiltered = users.stream().filter(u -> u.getLastName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "Email":
                    usersFiltered = users.stream().filter(u -> u.getEmail().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "Department Id":
                    usersFiltered = users.stream().filter(u -> Long.toString(u.getDepartmentId()).contains(keyword)).collect(Collectors.toList());
                    return usersFiltered;
                case "Role":
                    usersFiltered = users.stream().filter(u -> u.getRole().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "All":
                    return users;
            }
            return usersFiltered;
        }
        return users;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Override
    @Transactional
    public void update(User theUser) {
        userRepository.save(theUser);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(Long theId) {
        entityManager.clear();
        Optional<User> result = userRepository.findById(theId);
        return result.orElseThrow(() -> new RuntimeException("Did not find user id: " + theId));
    }

    @Override
    public User getOldUserById(Long theId) {
        entityManager.clear();
        return userRepository.getOldUserById(theId);
    }


    @Override
    public void deleteUserById(User theUser) {
        userRepository.delete(theUser);
    }

    //    @Override
//    @Cacheable(cacheNames = "users", unless = "#result == null")
//    public User findById(Long theId) {
//        cacheManager.getCache("users").evict(theId);
//        Optional<User> result = userRepository.findById(theId);
//        System.out.println("result: " + result);
//
//        User theUser = null;
//
//        if (result.isPresent()) {
//            theUser = result.get();
//            if (theUser != null) {
//                System.out.println("Fetched user from database: " + theUser);
//            } else {
//                System.out.println("User not found with id: " + theId);
//            }
//            System.out.println("result.get(): " + theUser);
//        } else {
//            throw new RuntimeException("Did not find user id: " + theId);
//        }
//        return theUser;
//
//    }


    @Override
    public <T> String downloadList(List<T> list) {

        List<User> users = userRepository.findAll();
        String excelFilePath = "src/main/resources/donwloads/list.xlsx";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("List");

        // Set style for table body
        CellStyle styleBody = workbook.createCellStyle();
        styleBody.setBorderTop(BorderStyle.THIN);
        styleBody.setBorderBottom(BorderStyle.THIN);
        styleBody.setBorderLeft(BorderStyle.THIN);
        styleBody.setBorderRight(BorderStyle.THIN);
        styleBody.setAlignment(HorizontalAlignment.LEFT);

        int rowIndex = 1;
        // Add header values
        Row rowHeader = sheet.createRow(rowIndex - 1);
        Cell cellHeader = rowHeader.createCell(0);
        cellHeader.setCellValue("ID");
        Cell cellHeader1 = rowHeader.createCell(1);
        cellHeader1.setCellValue("User ID");
        Cell cellHeader2 = rowHeader.createCell(2);
        cellHeader2.setCellValue("First name");
        Cell cellHeader3 = rowHeader.createCell(3);
        cellHeader3.setCellValue("Last name");
        Cell cellHeader4 = rowHeader.createCell(4);
        cellHeader4.setCellValue("Email");
        Cell cellHeader5 = rowHeader.createCell(5);
        cellHeader5.setCellValue("Department ID");
        Cell cellHeader6 = rowHeader.createCell(6);
        cellHeader6.setCellValue("Role");


        // Add table body values
        for (User u : users) {
            Row row = sheet.createRow(rowIndex++);
            Cell cell = row.createCell(0);
            cell.setCellValue(u.getId());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(u.getUserId());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(u.getFirstName());
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(u.getLastName());
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(u.getEmail());
            Cell cell5 = row.createCell(5);
            // Get department by id
            Department department = departmentRepository.getById(u.getDepartmentId());
            cell5.setCellValue(department.getName());
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(u.getRole());
        }

        // Apply borders to all non-empty cells
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    cell.setCellStyle(styleBody);
                }
            }
        }

        for (int i = 0; i <= 11; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            workbook.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Workbook cannot be closed");
        }
        return "List has been downloaded";
    }


    @Override
    public User findUserByIdWithDocuments(Long id) {
        return userRepository.findUserWithDocuments(id);
    }

    private static Pageable doPagingAndSorting(Integer page, Integer size, String sortBy, String sortDirection) {
        if (sortBy != null) {
            Sort sort = Sort.by(Sort.DEFAULT_DIRECTION, sortBy);
            // Defining the sort direction: A - ascending and D - descending. If direction not provided, default direction to be applied
            if (sortDirection != null) {
                if (sortDirection.equals("A")) {
                    sort = Sort.by(ASC, sortBy);
                } else if (sortDirection.equals("D")) {
                    sort = Sort.by(DESC, sortBy);
                }
            }

            Pageable pageable;
            if (page != null) {
                pageable = PageRequest.of(page, size, sort);
            } else {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
            }
            return pageable;
        } else {
            Pageable pageable;
            if (page != null) {
                pageable = PageRequest.of(page, size);
            } else {
                pageable = PageRequest.of(0, Integer.MAX_VALUE);
            }
            return pageable;
        }
    }

}