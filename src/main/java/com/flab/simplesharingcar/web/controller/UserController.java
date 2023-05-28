package com.flab.simplesharingcar.web.controller;

import com.flab.simplesharingcar.constants.SessionKey;
import com.flab.simplesharingcar.domain.User;
import com.flab.simplesharingcar.service.user.SignInService;
import com.flab.simplesharingcar.service.user.SignUpService;
import com.flab.simplesharingcar.web.dto.SignInDto;
import com.flab.simplesharingcar.web.dto.SignUpDto;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final SignUpService signUpService;

    private final SignInService signInService;

    @GetMapping("/create")
    public String signUpForm(Model model) {
        model.addAttribute("user", new SignUpDto());
        return "signUpForm";
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<SignUpDto> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        User signUpUser = signUpDto.toDomain();

        User savedUser = signUpService.join(signUpUser);

        SignUpDto responseDto = SignUpDto.from(savedUser);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/login")
    public String signInForm(Model model) {
        model.addAttribute("user", new SignInDto());
        return "signInForm";
    }


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<SignInDto> signIn(@Valid @RequestBody SignInDto signInDto, HttpServletRequest request) {
        String email = signInDto.getEmail();
        String password = signInDto.getPassword();

        User loginUser = signInService.login(email, password);

        HttpSession session = request.getSession();
        session.setAttribute(SessionKey.LOGIN_USER, loginUser);
        SignInDto responseDto = SignInDto.from(loginUser);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
