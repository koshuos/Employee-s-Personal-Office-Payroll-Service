package com.internal.service;


import com.internal.controllers.error.UserNotFoundException;
import com.internal.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Transactional
@Service
public class DataDaoImpl implements DataDao {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private VerificationToken verificationToken;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LastPay getlastPayEntity(Double pay, String drfo) {
        return sessionFactory.getCurrentSession().createQuery(
                        "from LastPay as lp  where lp.pay =:pay and lp.employees =:drfo", LastPay.class)
                .setParameter("pay", pay)
                .setParameter("drfo", drfo)
                .uniqueResult();
    }

    @Override
    public Employees getEmployees(String tab_n, String drfo) {
        return sessionFactory.getCurrentSession().createQuery(
                        "from Employees as e  where e.drfo =:drfo and e.tabelNomer =:tab_n", Employees.class)
                .setParameter("tab_n", tab_n)
                .setParameter("drfo", drfo)
                .uniqueResult();
    }

    @Override
    public Employees getEmployeesFromId(Integer id) {
        return sessionFactory.getCurrentSession().createQuery(
                        "from Employees as e  where e.id =:id", Employees.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public Users getUserFromId(Integer id) {
        return sessionFactory.getCurrentSession().createQuery(
                        "from Users as e  where e.userid =:id", Users.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public Users getUser(String email, String tab_n) {
        return sessionFactory.getCurrentSession().createQuery(
                        "from Users as e  where e.email =:email and e.username =:tab_n", Users.class)
                .setParameter("email", email)
                .setParameter("tab_n", tab_n)
                .uniqueResult();
    }

    @Override
    public Users getUserTabn(String tab_n) {
        return sessionFactory.getCurrentSession().createQuery(
                        "from Users as e  where e.username =:tab_n", Users.class)
                .setParameter("tab_n", tab_n)
                .uniqueResult();
    }

    @Override
    public Users getUserfromName(String name) {
        return sessionFactory.getCurrentSession().createQuery(
                        "from Users as e  where e.username =:name", Users.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public Users getUserFromEmail(String email, String tab_n) throws UserNotFoundException {
        if (!findUser(email, tab_n)) {
            throw new UserNotFoundException();
        }

        return getUser(email, tab_n);
    }

    @Override
    public Boolean findUser(String email, String tab_n) {
        try (Session session = sessionFactory.openSession()) {
            return (Long) session.createQuery(
                            "select count(u.userid) from Users as u " +
                                    "where u.email=:email and u.username=:tab_n ")
                    .setParameter("email", email)
                    .setParameter("tab_n", tab_n)
                    .uniqueResult() > 0;
        }
    }

    @Override
    public void saveUser(Users users) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(users);
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUserRoles(UserRoles userRoles) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(userRoles);
            session.getTransaction().commit();
        }
    }

    @Override
    public void createPasswordResetTokenForUser(Users user, String token) {
        VerificationtokenPassword findmyToken = verificationToken.findByUser(user);

        if (findmyToken != null) {
            verificationToken.deletePasswordToken(findmyToken);
        }

        final VerificationtokenPassword myToken = new VerificationtokenPassword(token, user);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(myToken);
            session.getTransaction().commit();
        }
    }

    @Override
    public String validatePasswordResetToken(long id, String token) {
        final VerificationtokenPassword passToken = verificationToken.findByTokenPassword(token);
        if ((passToken == null) || (passToken.getUser()
                .getUserid() != id)) {
            return "invalidToken";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpirydate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            return "expired";
        }

        final Users user = passToken.getUser();
        final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Collections.singletonList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;

    }

    @Override
    public void changeUserPassword(Users user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public Period getPeriodfromId(Integer id) {
        return sessionFactory.getCurrentSession().createQuery(
                        "from Period as e  where e.id =:id", Period.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<Period> getPeriodlist() {
        return sessionFactory.getCurrentSession().createQuery(
                        "from Period order by id", Period.class)
                .list();
    }

    @Override
    public void saveTabulagramData(List<TabulagramData> data_list, Boolean sendEmail) {
        for (TabulagramData item : data_list) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.save(item);
                session.getTransaction().commit();
            } finally {
                if (sendEmail) {
                    String tabN = String.valueOf(item.getTabN());
                    if (tabN.equals(item.getData())) {
                        SendEmailsEntity sendEmailsEntity = new SendEmailsEntity();
                        sendEmailsEntity.setTab(item.getTabN());
                        sendEmailsEntity.setPeriod(item.getPeriod().getId());
                        try (Session session = sessionFactory.openSession()) {
                            session.beginTransaction();
                            session.save(sendEmailsEntity);
                            session.getTransaction().commit();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void saveTabulagramSpr(List<TabulagramSpr> item) {
        try (Session session = sessionFactory.openSession()) {
            for (TabulagramSpr xxx : item) {
                session.beginTransaction();
                session.save(xxx);
                session.getTransaction().commit();
            }
        }
    }

    @Override
    public void truncateLastpay(Integer period) {

        String hql = "delete from tabulagram_data as t where t.period =:period";
        NativeQuery nativeQuery = sessionFactory.getCurrentSession().createNativeQuery(hql)
                .setParameter("period", period);
        nativeQuery.executeUpdate();

        String hql2 = "delete from tabulagram_spr as t where t.period =:period";
        NativeQuery nativeQuery2 = sessionFactory.getCurrentSession().createNativeQuery(hql2)
                .setParameter("period", period);
        nativeQuery2.executeUpdate();

    }

    @Override
    public List<Tab_show> getTabData(Integer period, Integer tab_n) {
        return sessionFactory.getCurrentSession().createQuery(
                        "select new com.internal.model.Tab_show(d.str, s.data, d.kod, d.type) from TabulagramData as s left join TabulagramSpr as d  on d.kod = s.kod and d.period.id = s.period.id  where s.period.id =:period and s.tabN =:tab_n order by d.kod", Tab_show.class)
                .setParameter("period", period)
                .setParameter("tab_n", tab_n)
                .list();
    }

    @Override
    public List<SendEmailsEntity> getSendEmailsEntity() {
        return sessionFactory.getCurrentSession().createQuery(
                        "from SendEmailsEntity", SendEmailsEntity.class)
                .list();
    }

    @Override
    public Integer deletePasswordTokenUser(Integer tabN, Integer period) {
        String hql = "delete from send_emails as s where s.tab =:tabN and s.period  =:period";
        NativeQuery nativeQuery = sessionFactory.getCurrentSession().createNativeQuery(hql)
                .setParameter("tabN", tabN)
                .setParameter("period", period);
        return nativeQuery.executeUpdate();
    }


}