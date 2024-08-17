package com.dgpad.thyme.controller;
import com.dgpad.thyme.media.FileUploadService;
import com.dgpad.thyme.model.User;
import com.dgpad.thyme.model.Role;
import com.dgpad.thyme.model.Post;
import com.dgpad.thyme.model.Media;
import com.dgpad.thyme.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "Admin/Dashboard";
    }


    @GetMapping(value = "/test2")
    public String test2(Model model) {
        return "account/display1";
    }


    //for all users
    @GetMapping(value = "/managePosts")
    public String managePosts(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "Admin/managePosts";
    }
    @PostMapping("/uploadPostMedia")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String uploadPostMedia(@RequestParam("file") MultipartFile file,
                                    @RequestParam("title") String title,
                                    @RequestParam("description") String description,
                                    @RequestParam("PostId") UUID postId) throws Exception {
        Media media = fileUploadService.uploadMedia(file);
        Post post = postService.getPostById(postId);
        if (post != null) {
            post.postMedias.add(media);
            postService.save(post);
        }
        return "redirect:/managePosts";
    }

    @PostMapping("/createPost")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createMajlis(@RequestParam("title") String title, @RequestParam("description") String description,
                               @RequestParam("servings") int servings,@RequestParam("instructions") String instructions,@RequestParam("ingredients") String ingredients,@RequestParam("media") List<MultipartFile> media,@RequestParam("postImage") MultipartFile postImage) throws Exception {
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
        model.addAttribute("user", userService.getCurrentUser());
        return "account/displayPost";
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