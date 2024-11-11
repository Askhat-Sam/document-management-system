package com.finalproject.document.management.service.implementations;

import com.finalproject.document.management.dto.UserDTO;
import com.finalproject.document.management.entity.User;
import com.finalproject.document.management.repository.UserRepository;
import com.finalproject.document.management.service.interfaces.UserService;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager entityManager;

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public List<UserDTO> findAll(Integer page, Integer size, String sortBy, String sortDirection, String keyword, String column) {
        Pageable pageable = doPagingAndSorting(page, size, sortBy, sortDirection);
        List<User> users;
        List<UserDTO> usersDTO = new ArrayList<>();
        if (pageable != null) {
            users = userRepository.findAll(pageable).toList();
            usersDTO = users.stream().map(this::fromEntityToDTO).collect(Collectors.toList());
        } else {
            users = userRepository.findAll();
            usersDTO = users.stream().map(this::fromEntityToDTO).collect(Collectors.toList());
        }

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        String loggedUserAuthority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        // For the users with role EMPLOYEE and MANAGER filter the  list of user to one (own account)
        if (loggedUserAuthority.equals("[ROLE_EMPLOYEE]")) {
            usersDTO = usersDTO.stream().filter(u -> u.getUserId().equals(loggedUser)).collect(Collectors.toList());
        }

        List<UserDTO> usersFiltered = new ArrayList<>();

        //if searching by keyword in certain column. Uses "contains" to search by the part of word.
        if (keyword != null && column != null) {
            switch (column) {
                case "Id":
                    usersFiltered = usersDTO.stream().filter(u -> Long.toString(u.getId()).contains(keyword)).collect(Collectors.toList());
                    return usersFiltered;
                case "User id":
                    usersFiltered = usersDTO.stream().filter(u -> u.getUserId().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "First name":
                    usersFiltered = usersDTO.stream().filter(u -> u.getFirstName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "Last name":
                    usersFiltered = usersDTO.stream().filter(u -> u.getLastName().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "Email":
                    usersFiltered = usersDTO.stream().filter(u -> u.getEmail().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "Department":
                    usersFiltered = usersDTO.stream().filter(u -> u.getDepartment().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "Role":
                    usersFiltered = usersDTO.stream().filter(u -> u.getRole().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
                case "Status":
                    usersFiltered = usersDTO.stream().filter(u -> u.getStatus().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
                    return usersFiltered;
            }
            return usersFiltered;
        }
        return usersDTO;
    }

    public UserDTO fromEntityToDTO(User user) {
        return new UserDTO(user.getId(), user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getDepartment(), user.getRole(), user.getStatus());
    }

    public User fromDTOToEntity(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getUserId(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                userDTO.getDepartment(), userDTO.getRole(), userDTO.getStatus());
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::fromEntityToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void update(User theUser) {
        userRepository.save(theUser);
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<String> findAllUserIds() {
        return userRepository.findAllUserIds();
    }

    @Override
    public UserDTO findById(Long theId) {
        User user = userRepository.findById(theId).get();
        UserDTO userDTO = new UserDTO(user.getId(), user.getUserId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getDepartment(), user.getRole(), user.getStatus());

        return userDTO;
    }

    @Override
    public User updateUser(User user, RedirectAttributes redirectAttributes) {
        User updatedUser = userRepository.getById(user.getId());
        // Get the userId og logged user
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        String loggedUserAuthority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        // Allow save own password for user with any role
        if (!user.getPassword().isEmpty() && loggedUser.equals(user.getUserId())) {
                //generate bcrypt hash
                String pw_hash = "{bcrypt}" + BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                updatedUser.setPassword(pw_hash);

        } else if (!user.getPassword().isEmpty()){
            redirectAttributes.addAttribute("errorMessage", "You can only change your own password!");
        }

        if (loggedUserAuthority.equals("[ROLE_ADMIN]")) {
            if (user.getUserId() != null) {
                // Check if the userID is already exist in DB
                if (userRepository.findAllUserIds().contains(user.getUserId())){
                    redirectAttributes.addAttribute("userIdExist", "User with id " + user.getUserId() + " already exist in database.");
                }else {
                    updatedUser.setUserId(user.getUserId());
                }
            }
            if (user.getFirstName() != null) {
                updatedUser.setFirstName(user.getFirstName() );
            }
            if (user.getLastName() != null) {
                updatedUser.setLastName(user.getLastName());
            }
            if (user.getEmail() != null) {
                updatedUser.setEmail(user.getEmail() );
            }
            if (user.getDepartment() != null) {
                updatedUser.setDepartment(user.getDepartment());
            }
//            if (password != "") {
//                //generate bcrypt hash
//                String pw_hash = "{bcrypt}" + BCrypt.hashpw(password, BCrypt.gensalt());
//                user.setPassword(pw_hash);
//            }
            if (user.getRole() != null) {
                updatedUser.setRole(user.getRole());
            }
            if (user.getStatus() != null) {
                updatedUser.setStatus(user.getStatus());
                if (user.getStatus().equals("Not active")) {
                    updatedUser.setActive(0);
                }
            }
        }

        return updatedUser;
    }

    @Override
    public String generateUserId(String firstName, String lastName) {
        int charCount = 0;
        int intCount = 1;
        //Generate userId based on first and last name
        StringBuilder userId = new StringBuilder().append(firstName).append(".").append(lastName.charAt(charCount++));

        // Check if the userId is already exist in DB
        while (userRepository.findAllUserIds().contains(userId.toString())){
            if (charCount < lastName.length()){
                userId.append(lastName.charAt(charCount++));
            } else {
                userId.append(intCount++);
            }
        };

        return userId.toString();
    }

    @Override
    public User findUserById(Long theId) {
        entityManager.clear();

        return userRepository.findUserById(theId);
    }

    public ResponseEntity<byte[]> downloadListAsExcel() {
        List<User> users = userRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("List");

        // Set style for table body
        CellStyle styleBody = workbook.createCellStyle();
        styleBody.setBorderTop(BorderStyle.THIN);
        styleBody.setBorderBottom(BorderStyle.THIN);
        styleBody.setBorderLeft(BorderStyle.THIN);
        styleBody.setBorderRight(BorderStyle.THIN);
        styleBody.setAlignment(HorizontalAlignment.LEFT);

        // Create header CellStyle
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.index);
        CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
        // fill foreground color ...
        headerCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 32, 91), null));
        // and solid fill pattern produces solid grey cell fill
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFont(headerFont);


        int rowIndex = 1;
        // Add header values
        Row rowHeader = sheet.createRow(rowIndex - 1);
        rowHeader.createCell(0).setCellValue("Id");
        rowHeader.createCell(1).setCellValue("User id");
        rowHeader.createCell(2).setCellValue("First name");
        rowHeader.createCell(3).setCellValue("Last name");
        rowHeader.createCell(4).setCellValue("Email");
        rowHeader.createCell(5).setCellValue("Department id");
        rowHeader.createCell(6).setCellValue("Role");
        // Add table body values
        for (User u : users) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(u.getId());
            row.createCell(1).setCellValue(u.getUserId());
            row.createCell(2).setCellValue(u.getFirstName());
            row.createCell(3).setCellValue(u.getLastName());
            row.createCell(4).setCellValue(u.getEmail());
            row.createCell(5).setCellValue(u.getDepartment());
            row.createCell(6).setCellValue(u.getRole());
        }

        // Apply style to cells
        for (Row row : sheet) {
            for (Cell cell : row) {
                // Apply border to all non empty cells
                if (cell.getCellType() != CellType.BLANK) {
                    cell.setCellStyle(styleBody);
                }
                // Apply style to the headers
                if (cell.getRowIndex() == 0) {
                    cell.setCellStyle(headerCellStyle);
                }
            }
        }

        for (int i = 0; i <= 9; i++) {
            sheet.autoSizeColumn(i);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=list.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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