package com.mikhail.tarasevich.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }

}
