package com.dgpad.thyme.service;

import com.dgpad.thyme.media.FileUploadService;
import com.dgpad.thyme.media.MediaRepository;
import com.dgpad.thyme.model.Media;
import com.dgpad.thyme.model.User;
import com.dgpad.thyme.model.Post;
import com.dgpad.thyme.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private FileUploadService fileUploadService;

    public Post createPost(String title, String description, String ingredients, int servings, String instructions) {
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setIngredients(ingredients);
        post.setInstructions(instructions);
        post.setServings(servings);
        post.setCreatedDate(LocalDate.now());
        return postRepository.save(post);
    }
    public Post uploadPostMedia( MultipartFile file, UUID postId, boolean IsPostImage) throws IOException {
        Media media = fileUploadService.uploadMedia(file);
        Post post = getPostById(postId);
        if (post != null) {
            if(IsPostImage)
                post.setPostImage(media);
            else
                post.postMedias.add(media);
            return save(post);
        }
        return null;
    }
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public Post getPostById(UUID id) {
        return postRepository.findById(id).orElse(null);
    }

    public void deletePost(UUID id) {
        fileUploadService.delete(getPostById(id).getPostImage());
        postRepository.deleteById(id);
    }

    public Post save(Post post){
        return postRepository.save(post);
    }

}
