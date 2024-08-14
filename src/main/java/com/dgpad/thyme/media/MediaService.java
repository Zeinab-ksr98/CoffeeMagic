package com.dgpad.thyme.media;

import com.dgpad.thyme.model.Media;
import com.dgpad.thyme.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private FileUploadService minioFileUploadService;
    @Autowired
    private UserService userService;

    public Media uploadVideo(MultipartFile file, String title, String description) throws IOException {
        String fileName = minioFileUploadService.uploadFile(file);
        Media media = new Media();
        media.setFileName(fileName);
        media.setFileType(file.getContentType());
        media.setTitle(title);
        media.setDescription(description);
        media.setCreatedDate(LocalDate.now());
        media.setOwner(userService.getCurrentUser()); // Assuming you get the current user

        // Save only metadata in the database
        return mediaRepository.save(media);
    }

    public void deleteVideo(UUID id) {
        mediaRepository.deleteById(id);
    }
    public List<Media> findByOwner(UUID user) {
        return mediaRepository.findMediaByOwnerId(user);
    }
    public List<Media> findThisWeekVideos() {
        LocalDate startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = LocalDate.now().with(java.time.DayOfWeek.SUNDAY);

        return mediaRepository.findthisweekMedia(startOfWeek, endOfWeek);
    }
    public List<Media> findVideosCreatedThisMonth() {
        return mediaRepository.findMediasCreatedThisMonth();
    }
    public Media findTopByViewsCount() {
        return mediaRepository.findTopByOrderByViewsCountDesc().orElse(null);
    }
    public void incrementViews(UUID mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new EntityNotFoundException("Media not found"));
        media.incrementViews();
        mediaRepository.save(media);
    }
}
