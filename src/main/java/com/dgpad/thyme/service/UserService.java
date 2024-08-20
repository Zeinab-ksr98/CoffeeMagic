package com.dgpad.thyme.service;


import com.dgpad.thyme.model.User;
import com.dgpad.thyme.model.Role;
import com.dgpad.thyme.repository.UserRepository;
import com.dgpad.thyme.security.UserInfoDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public User createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        return userRepository.save(user);
    }
    public Optional<User> findUserByUserName(String username){
        return userRepository.findUserByUserName(username);
    }
    public Optional<User> findUserByEmailAndUserName(String username,String email){
        return userRepository.findUserByEmailAndName(email,username);
    }
    public User save(User user){
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }
    public List<User> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    public Boolean userEmailExists(String email){ return userRepository.existsByEmail(email);}

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> getAllUsersNotBlocked(){
        return userRepository.findAllNotBlocked();
    }
    @Transactional
    public User getUserById(UUID id){
        return userRepository.findById(id).orElse(null);
    }

    public User BlockUser(UUID id){
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(value -> value.setDeleted(true));
        return user.orElse(null);
    }
    public User deActivateUser(UUID id){
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(value -> value.setEnabled(false));
        return user.orElse(null);
    }
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails) {
            UserInfoDetails userInfoDetails = (UserInfoDetails) authentication.getPrincipal();
            return userInfoDetails.getUser();
        }

        return null;
    }

    public User activate(UUID id){
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(value -> value.setEnabled(true));
        return user.orElse(null);
    }

    public boolean validId(UserDetails userDetails, String id){
        UserInfoDetails userInfoDetails = (UserInfoDetails) userDetails;
        return userInfoDetails.getUserId().equals(id);
    }
    public class UnsupportedUserTypeException extends RuntimeException {
        public UnsupportedUserTypeException(String message) {
            super(message);
        }
    }
}
