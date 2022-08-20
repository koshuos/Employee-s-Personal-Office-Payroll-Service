package com.internal.controllers;

import com.internal.model.Employees;
import com.internal.model.Users;
import com.internal.service.DataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private DataDao dao;


    @GetMapping("/refreshsession")
    @ResponseBody
    public ResponseEntity<?> refreshsession() {

        return new ResponseEntity<>("{\"success\":true}", HttpStatus.OK);
    }

    @RequestMapping(path = "/getperiod", method = RequestMethod.POST)
    public List getListLawApps() {
        return dao.getPeriodlist();
    }


    @GetMapping("/getusername")
    @ResponseBody
    public ResponseEntity<?> getusername(Authentication authResult) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Users userfromName = dao.getUserfromName(currentPrincipalName);
        Employees employeesFromId = null;

        try {
            employeesFromId = dao.getEmployeesFromId(userfromName.getUserid());
        } catch (NullPointerException ex) {

            authResult.setAuthenticated(false);
        }

        if (employeesFromId == null) {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        } else {
            boolean ts = userfromName.getShow_admin_tab() == 1;

            return new ResponseEntity<>("{\"pib\":" + "\"" + employeesFromId.getPib() + "\"" + ", \"tabadmin\":" + ts + "}", HttpStatus.OK);
        }

    }
}