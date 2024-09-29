package com.crypto.service;

import com.crypto.domain.VerificationType;
import com.crypto.model.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt);

    public User findUserByEmail(String email);

    public User findUserById(Long userId);

    public User enableTwoFactorAuthentication(VerificationType verificationType,
                                              String sendTo,
                                              User user);

    User updatePassword(User user, String newPassword);
}
