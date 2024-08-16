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

    public Post createPost(String title, String description, String ingredients, String servings, String instructions) {
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setIngredients(ingredients);
        post.setInstructions(instructions);
        post.setServings(servings);
        post.setCreatedDate(LocalDate.now());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public Post getPostById(UUID id) {
        return postRepository.findById(id).orElse(null);
    }

    public void deletePost(UUID id) {
        postRepository.deleteById(id);
    }

    public Post save(Post post){
        return postRepository.save(post);
    }

}
