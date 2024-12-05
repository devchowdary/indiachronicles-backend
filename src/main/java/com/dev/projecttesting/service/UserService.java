package com.dev.projecttesting.service;


import com.dev.projecttesting.model.User;
import com.dev.projecttesting.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService
{
    @Autowired
    UserRepo userRepo;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    OtpService otpService;

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, User> unverifiedUsers = new HashMap<>();


    public void registerUser(User user)
    {

        String otp = otpService.generateOtp();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        otpService.sendOtp(user.getEmail(), otp);
        otpStorage.put(user.getEmail(), otp);
        unverifiedUsers.put(user.getEmail(), user);
    }

    public boolean verifyOtp(String email, String otp)
    {
     String storedOtp = otpStorage.get(email);

     if(storedOtp != null && otp.equals(storedOtp))
     {
         User user = unverifiedUsers.get(email);
         if(user!=null)
         {
             userRepo.save(user);
             otpStorage.remove(email);
             unverifiedUsers.remove(email);
             return true;
         }
     }
     return false;
    }




    public Optional<User> loginUser(String usernameOrEmail, String password) {
        // Split the input into two parameters for the repository method
        User user = userRepo.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElse(null);

        // Validate user existence and check the password
        if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return Optional.of(user);
        }

        return Optional.empty();
    }
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    private String generatePrefixedId(Long id) {
        return "IC00" + id;
    }

    // Retrieve all users
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByUserNameOrEmail(email, email);
    }

}
