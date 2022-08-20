package com.internal.controllers;

import com.internal.jasperreports.Service.MakeReportServ;
import com.internal.service.DataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class WebController {

    @Autowired
    MakeReportServ makeReportServ;
    @Autowired
    private DataDao dao;

    @RequestMapping("/")
    public ModelAndView home(ModelAndView model) {
        model.setViewName("index.html");
        return model;
    }

    @RequestMapping("/instruction")
    public void instruction(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=instruction.pdf");
        //Resource resource = new ClassPathResource("file:instruction.pdf");
       // File file = resource.getFile();
        Path path = Paths.get("instruction.pdf");
        Files.copy(path,
                response.getOutputStream());
        response.getOutputStream().flush();
    }



    @RequestMapping("/login")
    public String login() {
        //  return "login";

        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            return "login";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping("/logout")
    public String logout() {
        return "login";
    }

    @RequestMapping("/ng-templates/{resource}")
    public String angularHtmlTemplate(@PathVariable("resource") String resource) {
        return "ng-templates/" + resource;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) throws IOException {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            return "registration";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/updatePassword")
    public String updatePassword() {
        return "updatePassword";
    }


    @GetMapping("/forgetPassword")
    public String showforgetPassword(Model model) throws IOException {
        return "forgetPassword";
    }

    @GetMapping("invalidSession")
    public String usingHttpServletResponse(HttpServletResponse response) {
        response.setStatus(500);
        return "invalidSession";
    }

    @GetMapping("/changePassword")
    public ModelAndView showChangePasswordPage(@RequestParam("id") final long id,
                                               @RequestParam("token") final String token, final ModelMap model) {
        final String result = dao.validatePasswordResetToken(id, token);
        if (result != null) {
            //выводить ошибку
            return new ModelAndView("redirect:/login", model);
        } else {
            //  model.addAttribute("token", token);
            return new ModelAndView("redirect:/updatePassword");
        }
    }

}
