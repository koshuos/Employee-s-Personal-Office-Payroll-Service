package com.internal.service;

import com.internal.model.Users;
import com.internal.model.VerificationtokenPassword;

import java.util.Date;

public interface VerificationToken {

    VerificationtokenPassword findByUser(Users user);

    void deletePasswordToken(VerificationtokenPassword verificationtokenPassword);

    void deleteExpiredPasswordTokenUser(Date date);

    VerificationtokenPassword findByTokenPassword (String token);

    void deletePasswordTokenUser(Users user);

}
