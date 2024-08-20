package com.dgpad.thyme.controller;
import com.dgpad.thyme.model.Role;
import com.dgpad.thyme.model.User;
import com.dgpad.thyme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.UUID;


@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/LogIn")
    public String Login(Model model) {
        return "account/login";
    }
    @GetMapping("/forgetPage")
    public String forgetPage() {
        return "account/forgetPass";
    }
    @PostMapping("/forget_pass")
    public String forgetPassword(@RequestParam String resetpassword, @RequestParam String reusername, @RequestParam String reemail, RedirectAttributes redirectAttributes) {
        User user = userService.findUserByEmailAndUserName(reusername, reemail).orElse(null);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(resetpassword));
            userService.save(user);
            return "redirect:/LogIn";
        } else {
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/forgetPage";
        }
    }
    @GetMapping(value = "/create-initial-admin")
    public String createInitialAdmin(){
        System.out.println("hi");
        User user = new User("Admin", "zk@gmail.com", "123", Role.ADMIN,true);
        try{
            User admin = userService.createUser(user);
            System.out.println("done");
            return "Admin Successfully Created";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @GetMapping(value = "/",  produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getUsers(){
        try{
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable("id") String id){
        try{
            return new ResponseEntity<>(userService.getUserById(UUID.fromString(id)), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/block/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String Block(@PathVariable("id") String id){
        userService.save(userService.BlockUser(UUID.fromString(id)));
        userService.save(userService.deActivateUser(UUID.fromString(id)));
        return "redirect:/manage-users";

    }
    @GetMapping(value = "/deactivate/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deActivateUser(@PathVariable("id") String id) throws IOException {
        userService.save(userService.deActivateUser(UUID.fromString(id)));
        return "redirect:/manage-users";

    }

    @GetMapping(value = "/activate/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String activateUser(@PathVariable("id") String id) throws IOException {
        userService.save(userService.activate(UUID.fromString(id)));
        return "redirect:/manage-users";

    }
    }