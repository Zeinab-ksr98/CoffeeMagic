package com.dgpad.thyme.controller;
import com.dgpad.thyme.media.FileUploadService;
import com.dgpad.thyme.model.*;
import com.dgpad.thyme.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class accountController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private FileUploadService fileUploadService;
    @GetMapping(value = "/Main")
    public String LandingPage(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "account/landing page";
    }
    @GetMapping(value = "/home")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String home(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "Admin/Dashboard";
    }
    @PostMapping("/createPost")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createMajlis(@RequestParam("title") String title, @RequestParam("description") String description,
                               @RequestParam("servings") int servings, @RequestParam("instructions") String instructions, @RequestParam("ingredients") String ingredients, @RequestParam("postImage") MultipartFile postImage, @Param("media") List<MultipartFile> media) throws Exception {
        Post post = postService.createPost(title,description,ingredients,servings,instructions);
        for (int i = 0; i < media.size(); i++) {
            postService.uploadPostMedia(media.get(i),post.getId(),false);
        }
        postService.uploadPostMedia(postImage,post.getId(),true);
        return "redirect:/home";
    }

    @GetMapping("/viewPost/{id}")
    public String viewPost(Model model,@PathVariable UUID id) {
        model.addAttribute("post", postService.getPostById(id));
        return "account/display";
    }
    @GetMapping("/deletePost/{id}")
    public String deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return "redirect:/home";
    }
    @GetMapping("/editPost")
    public String createPost(@RequestParam("id") UUID id,@RequestParam("title") String title, @RequestParam("description") String description,
                               @RequestParam("servings") int servings, @RequestParam("instructions") String instructions, @RequestParam("ingredients") String ingredients) throws Exception {
        Post post = postService.editPost(id,title,description,ingredients,servings,instructions);
        return "redirect:/home";
    }
    @PostMapping("/uploadPostMedia")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String uploadPostMedia(@RequestParam("media") List<MultipartFile> media,
                                  @RequestParam("PostId") UUID postId) throws Exception {
        Post post = postService.getPostById(postId);
        if (post != null) {
            for (int i = 0; i < media.size(); i++) {
                postService.uploadPostMedia(media.get(i),post.getId(),false);
            }
        }
        return "redirect:/home";
    }
    @PostMapping("/uploadPostImage")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String uploadPostImage(@RequestParam("media") MultipartFile media,
                                  @RequestParam("PostId") UUID postId) throws Exception {
        Post post = postService.getPostById(postId);
        if (post != null) {
                postService.uploadPostMedia(media,post.getId(),true);

        }
        return "redirect:/home";
    }
//manage blogs
    @GetMapping(value = "/blogs")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String blogs(Model model) {
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "Admin/blogs";
    }
    @PostMapping("/createPost")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createBlog(@RequestParam("title") String title, @RequestParam("description") String description,  @RequestParam("postImage") MultipartFile postImage) throws Exception {
        Blog post = blogService.createBlog(title,description);
        blogService.uploadBlogMedia(postImage,post.getId());
        return "redirect:/blogs";
    }

//    @GetMapping("/viewBlog/{id}")
//    public String viewBlog(Model model,@PathVariable UUID id) {
//        model.addAttribute("blog", blogService.getBlogById(id));
//        return "Admin/blogs";
//    }
    @GetMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable UUID id) {
        blogService.deleteBlog(id);
        return "redirect:/blogs";
    }
    @GetMapping("/editBlog")
    public String createBlog(@RequestParam("id") UUID id,@RequestParam("title") String title, @RequestParam("description") String description) throws Exception {
        Blog post = blogService.editBlog(id,title,description);
        return "redirect:/blogs";
    }
    @PostMapping("/uploadBlogImage")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String uploadBlogImage(@RequestParam("media") MultipartFile media,
                                  @RequestParam("PostId") UUID postId) throws Exception {
        Blog blog = blogService.getBlogById(postId);
        if (blog != null) {
            blogService.uploadBlogMedia(media,blog.getId());
        }
        return "redirect:/blogs";
    }

    @GetMapping("/manage-users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String manageusers(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("users", userService.getAllUsersNotBlocked());
        return "Admin/manage-users";
    }
    @PostMapping(value = "/admin-create-withRole")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createAdmin(@RequestParam("username") String userName,
                              @RequestParam("email") String email,
                              @RequestParam("phone") String phone,Model model) throws IOException {
        User user = new User(userName, email, "123", phone,Role.ADMIN);
        userService.createUser(user);
        return "redirect:/manage-users";
    }

}