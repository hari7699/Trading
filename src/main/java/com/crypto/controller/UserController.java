package com.crypto.controller;

import com.crypto.domain.VerificationType;
import com.crypto.model.User;
import com.crypto.service.EmailService;
import com.crypto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/getUserProfile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/verification/{verificationType}/sendOtp")
    public ResponseEntity<User> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable VerificationType verificationType) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PatchMapping("/enableTwoFactorAuthentication/verifyOtp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);

    }
}