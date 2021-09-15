package com.molla.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @GetMapping("")
    public String getHomePage() {

        return "index";
    }

    @GetMapping("/login")
    public String viewLoginPage() {

        LOGGER.info("MainController | viewLoginPage is called");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        LOGGER.info("MainController | viewLoginPage | authentication : " + authentication.toString());
        LOGGER.info("MainController | viewLoginPage | authentication instanceof AnonymousAuthenticationToken : " + (authentication instanceof AnonymousAuthenticationToken));

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        return "redirect:/";
    }

}
