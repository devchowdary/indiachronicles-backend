package com.dev.projecttesting.controller;

import com.dev.projecttesting.model.ContactInquiry;
import com.dev.projecttesting.model.User;
import com.dev.projecttesting.repository.ContactInquiryRepository;
import com.dev.projecttesting.repository.UserRepo;
import com.dev.projecttesting.service.OtpService;
import com.dev.projecttesting.service.UserService;
import com.dev.projecttesting.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    UserService userService;
    @Autowired
    ContactInquiryRepository contactInquiryRepository;

    @Autowired
    OtpService otpService;

    @Autowired
    UserRepo userRepo;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.registerUser(user);

        String responseMessage = user.getEmail() ;

        return ResponseEntity.ok(responseMessage);
    }



    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp)
    {
        if(userService.verifyOtp(email, otp))
        {
            return ResponseEntity.ok("Registration Success");
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Otp");
        }
    }

//    @PostMapping("/verify-otp")
//    public ResponseEntity<Map<String, String>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
//        if (userService.verifyOtp(email, otp)) {
//            // Retrieve saved user
//            Optional<User> savedUserOpt = userService.getUserByEmail(email);
//            if (savedUserOpt.isPresent()) {
//                User savedUser = savedUserOpt.get();
//                String prefixedId = "IC00" + savedUser.getId();
//
//                // Build response with prefixed ID
//                Map<String, String> response = new HashMap<>();
//                response.put("message", "Registration successful");
//                response.put("prefixedId", prefixedId);
//                response.put("email", savedUser.getEmail());
//
//                return ResponseEntity.ok(response);
//            }
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid OTP"));
//    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String usernameOrEmail, @RequestParam String password) {
        System.out.println("Username/Email: " + usernameOrEmail + ", Password: " + password);

        // Check if the user is attempting to log in as an admin with static credentials
        if ("admin".equals(usernameOrEmail) && "admin".equals(password)) {
            // Admin login: hardcoded credentials
            String token = JwtUtil.generateToken("admin@domain.com", "ADMIN");  // Set a role of 'ADMIN' for admin users

            // Create a response map to include the token
            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin Login Success");
            response.put("token", token);
            response.put("username", "admin");
            response.put("role","ADMIN");

            return ResponseEntity.ok(response);
        }

        // Validate user credentials for regular users
        Optional<User> userOpt = userService.loginUser(usernameOrEmail, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Generate JWT token with user's email and role
            String token = JwtUtil.generateToken(user.getEmail(), user.getRole());

            // Create a response map to include the token
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login Success");
            response.put("token", token);
            response.put("username", user.getUserName());
            response.put("role",user.getRole());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid Username or Password"));
        }
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/respective-user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            // Validate token and extract the email
            String email = JwtUtil.validateTokenAndRetrieveSubject(token.substring(7)); // Remove "Bearer " prefix

            // Search for the user using the email
            Optional<User> userOpt = userRepo.findByUserNameOrEmail(email, email);

            // If user exists, return the profile
            if (userOpt.isPresent()) {
                return ResponseEntity.ok(userOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            // Handle token validation errors or other exceptions
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
        try {
            String email = JwtUtil.validateTokenAndRetrieveSubject(token.substring(7)); // Validate the token and retrieve the email
            Optional<User> userOpt = userRepo.findByUserNameOrEmail(email, email);

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Update user profile fields
                user.setUserName(updatedUser.getUserName());
                user.setPhone(updatedUser.getPhone());
                user.setLocation(updatedUser.getLocation());

                // Save the updated user profile to the database
                userRepo.save(user);

                // Send email notification after updating profile
                String subject = "Your Profile Information Has Been Updated";
                String body = "Hello " + user.getUserName() + ",\n\n" +
                        "We wanted to inform you that your profile information has been successfully updated.\n\n" +
                        "Best regards,\nThe Support Team";
                otpService.sendEmail(user.getEmail(), subject, body); // Send the email

                return ResponseEntity.ok("Profile updated successfully and email sent.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestHeader("Authorization") String token) {
        try {
            String email = JwtUtil.validateTokenAndRetrieveSubject(token.substring(7));
            Optional<User> userOpt = userRepo.findByUserNameOrEmail(email, email);

            if (userOpt.isPresent()) {
                userRepo.delete(userOpt.get());
                return ResponseEntity.ok("Account deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isPresent()) {
            String userName = userOpt.get().getUserName();
            userRepo.delete(userOpt.get());
            return ResponseEntity.ok("User '" + userName + "' deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }




    @PostMapping("/contact-expert")
    public ResponseEntity<String> handleContactForm(@RequestBody ContactInquiry contactInquiry) {
        contactInquiryRepository.save(contactInquiry);

        String subject = "Your Query Has Been Received!";
        String body = "Hello " + contactInquiry.getName() + ",\n\n" +
                "Thank you Mr./Mrs."+contactInquiry.getName()+" for reaching out to us. We have received your query regarding " +
                contactInquiry.getSubject() + ". Our team will get back to you shortly.Have a Nice Day❤️.\n\n" +
                "Best regards,\nThe Travel Experts,\nTeam IndiaChronicles.";
        otpService.sendEmail(contactInquiry.getEmail(), subject, body);

        return ResponseEntity.ok("Query submitted successfully!");
    }

    @GetMapping("/contact-inquiries")
    public ResponseEntity<List<ContactInquiry>> getAllQueries() {
        List<ContactInquiry> inquiries = contactInquiryRepository.findAll();
        return ResponseEntity.ok(inquiries);
    }
    @PostMapping("/send-reply")
    public ResponseEntity<String> sendReply(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String reply = payload.get("reply");
        String username = payload.get("username");

        String subject = "Response to Your Query";
        String body = "Hello, Mr/Mrs "+username+"\n\n" + reply + "\n\nBest regards,\nThe Support Team";

        otpService.sendEmail(email, subject, body);

        return ResponseEntity.ok("Reply sent successfully!");
    }



    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            // Extract email and role from the old token
            String email = JwtUtil.validateTokenAndRetrieveSubject(token.substring(7)); // Remove "Bearer " prefix
            String role = JwtUtil.getRoleFromToken(token.substring(7)); // Extract role from token

            // Generate a new token (with extended expiration time)
            String newToken = JwtUtil.refreshToken(email, role);

            // Send the new token back to frontend
            return ResponseEntity.ok(Map.of("token", newToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }


}

