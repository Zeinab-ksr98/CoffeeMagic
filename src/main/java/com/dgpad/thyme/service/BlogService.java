package com.dgpad.thyme.service;

import com.dgpad.thyme.media.FileUploadService;
import com.dgpad.thyme.model.Blog;
import com.dgpad.thyme.model.Media;
import com.dgpad.thyme.model.Post;
import com.dgpad.thyme.repository.BlogRepository;
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
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private FileUploadService fileUploadService;
    public Blog createBlog(String title, String description) {
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setCreatedDate(LocalDate.now());
        return blogRepository.save(blog);
    }
    public Blog uploadBlogMedia(MultipartFile file, UUID postId) throws IOException {
        Media media = fileUploadService.uploadMedia(file);
        Blog post = getBlogById(postId);
        post.setBlogImage(media);
        return save(post);

    }
    public Blog editBlog(UUID id ,String title, String description) {
        Blog post = getBlogById(id);
        post.setTitle(title);
        post.setDescription(description);
        return blogRepository.save(post);
    }
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
    public Blog getBlogById(UUID id) {
        return blogRepository.findById(id).orElse(null);
    }

    public void deleteBlog(UUID id) {
        blogRepository.deleteById(id);
    }

    public Blog save(Blog post){
        return blogRepository.save(post);
    }

}
