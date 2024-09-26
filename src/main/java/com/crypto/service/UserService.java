package com.crypto.service;

import com.crypto.model.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt);

    public User findUserByEmail(String email);

    public User findUserById(String id);

    public User enableTwoFactorAuthentication(User user);

    User updatePassword(User user, String newPassword);
}
