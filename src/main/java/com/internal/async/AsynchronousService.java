package com.internal.async;

import com.internal.model.Period;
import com.internal.model.SendEmailsEntity;
import com.internal.model.Users;
import com.internal.service.DataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

@Service
public class AsynchronousService {
    @Autowired
    private DataDao dao;
    @Autowired
    private JavaMailSender mailSender;

    @Async("asyncTaskExecutor")
    public void sendEmais() {

        System.out.println(new Date());
        List<SendEmailsEntity> sendEmailsEntity = dao.getSendEmailsEntity();
        if (!sendEmailsEntity.isEmpty()) {
            for (ListIterator<SendEmailsEntity> iter = sendEmailsEntity.listIterator(); iter.hasNext(); ) {
                SendEmailsEntity element = iter.next();

                Users userTabn = dao.getUserTabn(String.valueOf(element.getTab()));

                if (userTabn != null) {
                    Period periodfromId = dao.getPeriodfromId(element.getPeriod());
                    try {
                        mailSender.send(constructResetTokenEmail(userTabn, periodfromId.getNamePeriod()));

                        dao.deletePasswordTokenUser(element.getTab(), element.getPeriod());
                        if (dao.deletePasswordTokenUser(element.getTab(), element.getPeriod()) == 1) {
                            iter.remove();
                        }
                    } catch (MailException ex) {
                        dao.deletePasswordTokenUser(element.getTab(), element.getPeriod());
                        if (dao.deletePasswordTokenUser(element.getTab(), element.getPeriod()) == 1) {
                            iter.remove();
                        }
                        System.err.println(ex.getMessage());
                    }
                } else {
                    dao.deletePasswordTokenUser(element.getTab(), element.getPeriod());
                    if (dao.deletePasswordTokenUser(element.getTab(), element.getPeriod()) == 1) {
                        iter.remove();
                    }
                }
            }

        }
        System.out.println("finish");
    }

    private SimpleMailMessage constructResetTokenEmail(final Users user, String period) {
        final String message = "У персональномому кабінеті співробітника, для періода: " + period + ", cформован розрахунковий лист";
        return constructEmail(message, user);
    }


    private SimpleMailMessage constructEmail(String body, Users user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Сформован розрахунковий лист (i.zpep.com.ua)");
        email.setText(body);
        email.setTo(user.getEmail());
        //email.setTo("o.koshurnikov@zpep.com.ua");
        email.setFrom("billing@zpep.com.ua");
        return email;
    }
}