package io.francoisbotha.namazingserver.services;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    public void downloadFile(String keyName);
    public void uploadFile(String keyName, String uploadFilePath);
    public void deleteFile(String keyName);
    public String storeProfileImage(MultipartFile uploadedFile, String username);
}
