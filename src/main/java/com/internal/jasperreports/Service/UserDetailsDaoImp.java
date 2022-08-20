package com.internal.jasperreports.Service;

import com.internal.model.auth.User;
import com.internal.service.UserDetailsDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserDetailsDaoImp implements UserDetailsDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public User findUserByUsername(String username) {
        return sessionFactory.getCurrentSession().createQuery(
                "from User  where username=:user", User.class)
                .setParameter("user", username)
                .uniqueResult();
    }


}

