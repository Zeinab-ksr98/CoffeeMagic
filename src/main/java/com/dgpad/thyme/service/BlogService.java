package com.dgpad.thyme.service;

import com.dgpad.thyme.model.Blog;
import com.dgpad.thyme.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public Blog createBlog(String title, String description) {
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setCreatedDate(LocalDate.now());
        return blogRepository.save(blog);
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
