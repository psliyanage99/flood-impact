package com.floodapp.flood_impact.controller;

import com.floodapp.flood_impact.model.User;
import com.floodapp.flood_impact.repository.UserRepository;
import com.floodapp.flood_impact.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "https://floodimpact.online"})
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // --- UPDATED: Registration with Email Verification ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already in use"));
        }

        user.setRole("user");
        user.setEnabled(false); // User cannot login yet

        // Generate random token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);

        User savedUser = userRepository.save(user);

        // Send Email
        try {
            emailService.sendVerificationEmail(user.getEmail(), token);
        } catch (Exception e) {
            e.printStackTrace();
            // Don't fail registration if email fails, but log it (in prod, handle better)
        }

        return ResponseEntity.ok(savedUser);
    }

    // --- NEW: Verify Endpoint ---
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        Optional<User> userOpt = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getVerificationToken()))
                .findFirst();

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEnabled(true);
            user.setVerificationToken(null); // Clear token after use
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid or expired verification token"));
        }
    }

    // --- UPDATED: Login Check ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check password
            if (!user.getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid credentials"));
            }

            // Check if verified
            if (!user.isEnabled()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Email not verified. Please check your inbox."));
            }

            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials"));
    }

    @PostMapping("/google")
    public User loginWithGoogle(@RequestBody Map<String, String> payload) {
        String accessToken = payload.get("token");
        String googleUserInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        RestTemplate restTemplate = new RestTemplate();

        try {
            String url = googleUserInfoUrl + "?access_token=" + accessToken;
            Map<String, Object> googleUser = restTemplate.getForObject(url, Map.class);

            String email = (String) googleUser.get("email");
            String name = (String) googleUser.get("name");

            Optional<User> existingUser = userRepository.findByEmail(email);

            if (existingUser.isPresent()) {
                return existingUser.get();
            } else {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setRole("user");
                newUser.setPassword("GOOGLE_AUTH");
                newUser.setEnabled(true); // Google users are auto-verified
                return userRepository.save(newUser);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid Google Token");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            try {
                emailService.sendResetEmail(email);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Reset link sent successfully to " + email);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError()
                        .body(Map.of("message", "Error sending email. Check server logs."));
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Email not found"));
        }
    }
}