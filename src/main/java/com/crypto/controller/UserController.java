package com.crypto.controller;

import com.crypto.domain.VerificationType;
import com.crypto.model.ForgotPasswordToken;
import com.crypto.model.User;
import com.crypto.model.VerificationCode;
import com.crypto.request.ForgotPasswordTokenRequest;
import com.crypto.request.ResetPasswordRequest;
import com.crypto.response.ApiResponse;
import com.crypto.response.AuthResponse;
import com.crypto.service.EmailService;
import com.crypto.service.ForgotPasswordService;
import com.crypto.service.UserService;
import com.crypto.service.VerificationCodeService;
import com.crypto.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    private String jwt;

    @GetMapping("/getUserProfile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/verification/{verificationType}/sendOtp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable VerificationType verificationType) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if (verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }

        return new ResponseEntity<>("verification otp sent successfully", HttpStatus.OK);
    }

    @PatchMapping("/enableTwoFactorAuthentication/verifyOtp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @PathVariable String otp,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ?
                verificationCode.getEmail() : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);
        if (isVerified) {
            User udateUser = userService.enableTwoFactorAuthentication(
                    verificationCode.getVerificationType(), sendTo, user);

            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return new ResponseEntity<>(udateUser, HttpStatus.OK);
        }
        throw new Exception("wrong otp");
    }

    @PostMapping("/auth/users/resetPassword/sendOtp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(

            @RequestBody ForgotPasswordTokenRequest req) throws Exception {

        User user = userService.findUserByEmail(req.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());

        if (token == null) {
            token = forgotPasswordService.createToken(user, id, otp, req.getVerificationType(), req.getSendTo());
        }

        if (req.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());
        }

        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("password reset otp sent successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/resetPassword/verifyOtp/")
    public ResponseEntity<ApiResponse> resetPasswordOtp(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp());
        if (isVerified) {
            userService.updatePassword(forgotPasswordToken.getUser(), req.getPassword());
            ApiResponse res = new ApiResponse();
            res.setMessage("password updated successfully");
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        }
        throw new Exception("wrong otp");
    }

}