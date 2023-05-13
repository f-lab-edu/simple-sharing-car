package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.service.UserService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/create")
    public String signUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signUpForm";
    }

    @PostMapping
    public String signUp(@Valid User user, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "signUpForm";
        }
        userService.join(user);
        return "redirect:/users/create";
    }
}
