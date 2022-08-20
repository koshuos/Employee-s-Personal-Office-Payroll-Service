package com.internal.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException, ServletException {
        setDefaultFailureUrl("/login?error");

        super.onAuthenticationFailure(request, response, exception);

        String errorMessage = "Невірні облікові дані";

        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = "Обліковий запис не підтверджен";
        } else if (exception.getMessage().equalsIgnoreCase("Could not get JDBC Connection; nested exception is java.sql.SQLException: The Network Adapter could not establish the connection")) {
            errorMessage = "Сервіс не доступний";
        }else if (exception.getMessage().equalsIgnoreCase("InvalidReCaptcha")) {
            errorMessage = "Невірний ReCaptcha";
        }
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }
}