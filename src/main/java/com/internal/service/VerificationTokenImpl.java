package com.internal.service;

import com.internal.model.Users;
import com.internal.model.VerificationtokenPassword;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
@Transactional
public class VerificationTokenImpl implements VerificationToken{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public VerificationtokenPassword findByUser(Users user) {
        return sessionFactory.getCurrentSession().createQuery(
                "from VerificationtokenPassword as v  where v.user =:user ", VerificationtokenPassword.class)
                .setParameter("user", user)
                .uniqueResult();
    }

    @Override
    public void deletePasswordToken(VerificationtokenPassword verificationtokenPassword) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(verificationtokenPassword);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteExpiredPasswordTokenUser(Date date) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("delete from VerificationtokenPassword t where t.expirydate < :date");
            query.setParameter("date", date);
            query.executeUpdate();
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public VerificationtokenPassword findByTokenPassword(String token) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "from VerificationtokenPassword as a where a.token=:token", VerificationtokenPassword.class)
                    .setParameter("token", token)
                    .uniqueResult();
        }
    }

    @Override
    public void deletePasswordTokenUser(Users user) {
        VerificationtokenPassword verificationtokenPassword = findByUser(user);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(verificationtokenPassword);
            session.getTransaction().commit();
            session.close();
        }
    }
}
