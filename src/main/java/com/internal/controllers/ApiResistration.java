package com.internal.controllers;

import com.internal.model.*;
import com.internal.service.DataDao;
import com.internal.service.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@RestController
public class ApiResistration {

    @Autowired
    private DataDao dao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VerificationToken verificationToken;
    @Autowired
    private Environment env;

    @RequestMapping(path = "registration", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> registration(@RequestBody Registration reg) {

        LastPay lastPay = dao.getlastPayEntity(Double.valueOf(reg.getPay().replace(',', '.')), reg.getDrfo());

        if (lastPay != null) {
            Employees employees = dao.getEmployees(reg.getTab_nomer(), lastPay.getEmployees());
            if (employees != null) {

                Users user = dao.getUserFromId(employees.getId());

                if (user != null) {
                    return new ResponseEntity<>("{\"success\":\"За цими даними користувач вже зареєстрований\"}", HttpStatus.OK);
                } else {
                    //пишем пользователя в users
                    Users auh_user = new Users();
                    auh_user.setUserid(employees.getId());
                    auh_user.setEmail(reg.getEmail());
                    auh_user.setEnabled(1);
                    auh_user.setShow_admin_tab(0);
                    auh_user.setPassword(passwordEncoder.encode(reg.getPassword()));
                    auh_user.setUsername(reg.getTab_nomer());

                    UserRoles userRoles = new UserRoles();
                    userRoles.setUser(auh_user);
                    userRoles.setRole("USER");
                    dao.saveUser(auh_user);
                    dao.saveUserRoles(userRoles);


                    return new ResponseEntity<>("{\"success\":true}", HttpStatus.OK);
                }


            } else {
                return new ResponseEntity<>("{\"success\":\"Реєстрація не відбулась. Невірно введені реєстраційні дані.\"}", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("{\"success\":\"Реєстрація не відбулась. Невірно введені реєстраційні дані.\"}", HttpStatus.OK);
        }


    }


    @PostMapping("resetPassword")
    @ResponseBody
    public GenericResponse resetPassword(final HttpServletRequest request,
                                         @RequestBody ResetPassword data_from_from) throws IOException {

        final Users user = dao.getUserFromEmail(data_from_from.getEmail(), data_from_from.getTab_n());

        if (user != null) {
            final String token = UUID.randomUUID().toString();

            dao.createPasswordResetTokenForUser(user, token);
            mailSender.send(constructResetTokenEmail(getAppUrl(request), token, user));
        }
        return new GenericResponse("success");
    }

    @PostMapping("savePassword")
    @ResponseBody
    public GenericResponse savePassword(@RequestBody PasswordDto passwordDto, Authentication authResult) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final Users user = (Users) principal;
        dao.changeUserPassword(user, passwordDto.getNewpassword());
        verificationToken.deletePasswordTokenUser(user);
        authResult.setAuthenticated(false);
        return new GenericResponse("resetPasswordSuc");
    }


    private String getAppUrl(HttpServletRequest request) {
        // return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return "https://i.zpep.com.ua" + request.getContextPath();
    }


    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final Users user) {
        final String url = contextPath + "/changePassword?id=" + user.getUserid() + "&token=" + token;
        final String message = "Скидання паролю";
        return constructEmail(message + " \r\n" + url, user);
    }


    private SimpleMailMessage constructEmail(String body, Users user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Скидання паролю від Персональний кабінет співробітника");
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}




