package com.cda.PayYouPayMe.controller;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(RedirectAttributes attributes) {
        attributes.addFlashAttribute("error", "Vous n'avez pas accès à cette page.");
        return "redirect:/";
    }
}

	

