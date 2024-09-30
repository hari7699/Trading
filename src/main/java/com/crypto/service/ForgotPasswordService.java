package com.crypto.service;

import com.crypto.domain.VerificationType;
import com.crypto.model.ForgotPasswordToken;
import com.crypto.model.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user,
                                    String id, String otp,
                                    VerificationType verificationType,
                                    String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken Token);
}
