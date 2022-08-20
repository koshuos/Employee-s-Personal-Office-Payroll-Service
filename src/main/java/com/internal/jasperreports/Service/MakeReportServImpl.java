package com.internal.jasperreports.Service;

import com.internal.jasperreports.Repository.MakeReportDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MakeReportServImpl implements MakeReportServ {

    @Autowired
    MakeReportDAO makeReportDAO;


    @Override
    public String getCurentRole() {
        String authority = "";

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (GrantedAuthority iterable_element : authorities) {
            authority = iterable_element.getAuthority();
        }
        return authority;

    }

}
