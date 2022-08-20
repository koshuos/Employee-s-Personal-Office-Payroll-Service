package com.internal.service;


import com.internal.controllers.error.UserNotFoundException;
import com.internal.model.*;

import java.util.List;

public interface DataDao {

    LastPay getlastPayEntity(Double pay, String drfo);

    Employees getEmployees(String tab_n, String drfo);
    Employees getEmployeesFromId(Integer id);

    Users getUserFromId(Integer id);

    Users getUser(String email, String tab_n);
    Users getUserTabn(String tab_n);
    Users getUserfromName(String name);

    Users getUserFromEmail(String email, String tab_n) throws UserNotFoundException;

    Boolean findUser(String email, String tab_n);

    void saveUser(Users users);
    void saveUserRoles(UserRoles userRoles);

    void createPasswordResetTokenForUser(Users user, String token);

    String validatePasswordResetToken(long id, String token);

    void changeUserPassword(final Users user, final String password);


    Period getPeriodfromId(Integer id);
    List<Period> getPeriodlist();

    void saveTabulagramData(List<TabulagramData> item, Boolean sendEmail);
    void saveTabulagramSpr(List<TabulagramSpr> item);

    void truncateLastpay(Integer period);

    List<Tab_show> getTabData(Integer period, Integer tab_n);

    List<SendEmailsEntity> getSendEmailsEntity();

    Integer deletePasswordTokenUser(Integer tabN, Integer period);
}
