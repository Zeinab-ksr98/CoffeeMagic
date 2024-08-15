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
        model.addAttribute("post", postService.findMajlisTodayAndUpcoming());
        return "account/landing page";
    }
    @GetMapping(value = "/test")
    public String test(Model model) {
        model.addAttribute("post", postService.findMajlisTodayAndUpcoming());
        return "Visitor/test";
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
    @GetMapping(value = "/home")
    public String displayHome(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        List<Post> todayMajlis = postService.findMajlisToday();
        model.addAttribute("todayMajlis", todayMajlis);
        model.addAttribute("topmonthlyvedios", mediaService.findVideosCreatedThisMonth());
        model.addAttribute("topmonthlyMusics", mediaService.findVideosCreatedThisMonth());
        model.addAttribute("topVedio", mediaService.findTopByViewsCount());
        model.addAttribute("newReleases", mediaService.findThisWeekVideos());
        return "Visitor/home";

    }

    @GetMapping(value = "/display")
    public String display(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "Visitor/display";
    }

    @PostMapping("/uploadMedia")
    public String uploadMedia(@RequestParam("file") MultipartFile file,
                              @RequestParam("title") String title,
                              @RequestParam("description") String description) throws IOException {
        Media media = fileUploadService.uploadMedia(file, title, description);
        User user = userService.getCurrentUser();
//        user.getVideoList().add(media);
        userService.save(user);
        return "redirect:/manageChannel";
    }
//for all users
    @GetMapping(value = "/majalis")
    public String majalis(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "Visitor/majalis";
    }
    @PostMapping("/uploadPostMedia")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public String uploadMajlisMedia(@RequestParam("file") MultipartFile file,
                                    @RequestParam("title") String title,
                                    @RequestParam("description") String description,
                                    @RequestParam("MajlisId") UUID majlisId) throws Exception {
        Media media = fileUploadService.uploadMedia(file, title, description);
        Post post = postService.getMajlisById(majlisId).orElse(null);
        if (post != null) {
            post.getPostMedias().add(media);
            postService.save(post);
        }
        return "redirect:/managemajlis";
    }

    @GetMapping(value = "/Majalis")
    public String displayMyMajalis(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
//        List<Post> myMajlis = userService.getCurrentUser().getMajalisList();
        List<Post> myMajlis = postService.getAllMajlis();

        List<Post> mostPopularMajlis = postService.getMostPopularMajlis();
        List<Post> todayMajlis = postService.findMajlisToday();
        List<Post> upcomingMajlis = postService.findUpcomingMajlis();
        List<Post> archivedPostWithVideos = postService.findArchivedMajlisWithVideos();
        // Add data to model
        model.addAttribute("myMajalis", myMajlis);
        model.addAttribute("myMajalissize", myMajlis.size());
        model.addAttribute("mostPopularMajlis", mostPopularMajlis);
        model.addAttribute("todayMajlis", todayMajlis);
        model.addAttribute("upcomingMajlis", upcomingMajlis);
        model.addAttribute("archivedMajlisWithVideos", archivedPostWithVideos);
        model.addAttribute("organizers", userService.findAllByRole(Role.Majlis_ORGANIZER));

        return "Visitor/majalis";
    }
    @PostMapping("/createMajlis")
    @PreAuthorize("hasAnyAuthority('Majlis_ORGANIZER')")
    public String createMajlis(@RequestParam("title") String title, @RequestParam("description") String description,
                               @RequestParam("location") String location, @RequestParam("date")LocalDate date,@RequestParam("file") MultipartFile file) throws Exception {
        Post post = postService.createMajlis(title,description,location,date);
        uploadMajlisMedia(file,title,description, post.getId());
        userService.getCurrentUser().getMajalisList().add(post);
        return "redirect:/Majalis";
    }

    @GetMapping("/viewmajlis/{id}")
    public String viewMajlis(Model model,@PathVariable UUID id) {
        model.addAttribute("majlis", postService.getMajlisById(id));
        model.addAttribute("user", userService.getCurrentUser());
        return "Visitor/display";
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
                              @RequestParam("phone") String phone,
                              @RequestParam("role") Role role,Model model) throws IOException {
        User user = new User(userName, email, "123", phone,role);
        userService.createUser(user);
        return "redirect:/manage-users";
    }

}