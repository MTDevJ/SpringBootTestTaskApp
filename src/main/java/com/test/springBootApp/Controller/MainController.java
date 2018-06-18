package com.test.springBootApp.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/homePage")
    public Model getAll(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return model;
    }
}
