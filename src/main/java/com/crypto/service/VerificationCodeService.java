package com.crypto.service;

import com.crypto.domain.VerificationType;
import com.crypto.model.User;
import com.crypto.model.VerificationCode;
import org.springframework.stereotype.Service;

@Service
public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);
}
