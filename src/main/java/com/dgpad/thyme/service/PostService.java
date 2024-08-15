package com.dgpad.thyme.service;

import com.dgpad.thyme.media.FileUploadService;
import com.dgpad.thyme.media.MediaRepository;
import com.dgpad.thyme.model.User;
import com.dgpad.thyme.model.Post;
import com.dgpad.thyme.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private FileUploadService minioFileUploadService;
    public Post createMajlis(String title, String description, String location, LocalDate date) {
       User user= userService.getCurrentUser();
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setTitle(title);
        post.setDescription(description);
        post.setLocation(location);
        post.setDate(date);
        post.setUser(user);

        // Assuming User object creation by id or use appropriate service for fetching User
        return postRepository.save(post);
    }

    public List<Post> getAllMajlis() {
        return postRepository.findAll();
    }
    public List<Post> findMajlisTodayAndUpcoming() {
        LocalDate today = LocalDate.now();
        List<Post> todayMajlis = postRepository.findByDate(today);
        List<Post> upcomingMajlis = postRepository.findByDateAfter(today);
        todayMajlis.addAll(upcomingMajlis);
        return todayMajlis;
    }
    public List<Post> findByUser(UUID user) {
        return postRepository.findByUserId(user);
    }

    public List<Post> getMajlisCreatedThisMonth() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        return postRepository.findMajlisCreatedThisMonth(startOfMonth, endOfMonth);
    }

    public Post findTopByViewsCount() {
        return postRepository.findTopByOrderByViewsCountDesc().orElse(null);
    }
    public List<Post> getMostPopularMajlis() {
        return postRepository.findMostPopular();
    }

    public List<Post> findMajlisToday() {
        LocalDate today = LocalDate.now();
        return postRepository.findByDate(today);
    }

    public List<Post> findUpcomingMajlis() {
        LocalDate today = LocalDate.now();
        return postRepository.findByDateAfter(today);
    }

    public List<Post> findArchivedMajlisWithVideos() {
        LocalDate today = LocalDate.now();
        return postRepository.findArchivedWithVideos(today);
    }
    public Optional<Post> getMajlisById(UUID id) {
        return postRepository.findById(id);
    }


    public void deleteMajlis(UUID id) {
        postRepository.deleteById(id);
    }

    public Post save(Post post){
        return postRepository.save(post);
    }

}
