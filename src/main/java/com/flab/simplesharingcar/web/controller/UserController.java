package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> signUp(@Valid @RequestBody User user) {
        User savedUser = userService.join(user);
        return ResponseEntity.ok(savedUser);
    }
}
