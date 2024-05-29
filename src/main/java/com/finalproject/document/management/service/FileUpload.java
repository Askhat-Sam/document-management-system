package com.finalproject.document.management.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUpload {
    public static void moveFile(String filePath){

        File tempFile = new File(filePath);
        String oldFilePath = filePath;
        String newFilePath = "src/main/resources/documentsUploaded";

        try {
            Files.copy(Paths.get(oldFilePath), Paths.get(newFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
