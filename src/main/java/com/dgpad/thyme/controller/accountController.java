package com.dgpad.thyme.controller;
import com.dgpad.thyme.media.FileUploadService;
import com.dgpad.thyme.media.MediaService;
import com.dgpad.thyme.model.User;
import com.dgpad.thyme.model.Role;
import com.dgpad.thyme.model.Post;
import com.dgpad.thyme.model.Media;
import com.dgpad.thyme.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
public class accountController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private PostService postService;
    @Autowired
    private FileUploadService fileUploadService;
    @GetMapping(value = "/Main")
    public String home(Model model) {
        model.addAttribute("post", postService.getAllPosts());
        return "account/landing page";
    }
    @PostMapping("/forget_pass")
    public String forgetPassword(@RequestParam String resetpassword, @RequestParam String reusername, @RequestParam String reemail, RedirectAttributes redirectAttributes) {
        User user = userService.findUserByEmailAndUserName(reusername, reemail).orElse(null);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(resetpassword));
            userService.save(user);
            return "redirect:/SignIn";
        } else {
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/forgetPage";
        }
    }


//for all users
    @GetMapping(value = "/managePosts")
    public String managePosts(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "Admin/managePosts";
    }
    @PostMapping("/uploadPostMedia")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String uploadPostMedia(@RequestParam("file") MultipartFile file,
                                    @RequestParam("title") String title,
                                    @RequestParam("description") String description,
                                    @RequestParam("PostId") UUID postId) throws Exception {
        Media media = fileUploadService.uploadMedia(file, title, description);
        Post post = postService.getPostById(postId);
        if (post != null) {
            post.getPostMedias().add(media);
            postService.save(post);
        }
        return "redirect:/managePosts";
    }

    @PostMapping("/createPost")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String createMajlis(@RequestParam("title") String title, @RequestParam("description") String description,
                               @RequestParam("servings") String servings,@RequestParam("instructions") String instructions,@RequestParam("ingredients") String ingredients,@RequestParam("media") List<MultipartFile> media,@RequestParam("postImage") MultipartFile postImage) throws Exception {
        Post post = postService.createPost(title,description,ingredients,servings,instructions);
        for (int i = 0; i < media.size(); i++) {
            uploadPostMedia(media.get(i),title,description, post.getId());
        }
        uploadPostMedia(postImage,title,description,post.getId());
        return "redirect:/managePosts";
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