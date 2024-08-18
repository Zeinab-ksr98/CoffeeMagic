package com.dgpad.thyme.media;

import com.dgpad.thyme.model.Media;
 import com.dgpad.thyme.service.UserService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class FileUploadService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserService userService;

    public Media uploadMedia(MultipartFile file) throws IOException {
        // Upload file and get its name
        String fileName = uploadFile(file);
        // Generate the URL for the uploaded file
        String fileUrl = generateFileUrl(fileName);
        Media media = new Media();
        media.setFileName(fileUrl);
        media.setOwner(userService.getCurrentUser());
        return mediaRepository.save(media);
    }

    public String uploadFile(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    private String generateFileUrl(String fileName) {
        // Generate the URL for accessing the file
        return String.format("%s/%s/%s", minioEndpoint, bucketName, fileName);
    }
    public void delete(Media media) {
        mediaRepository.delete(media);
    }
}
