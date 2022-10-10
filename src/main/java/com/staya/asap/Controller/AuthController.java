package com.staya.asap.Controller;

import com.staya.asap.Model.DB.UserDTO;
import com.staya.asap.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signin")
    public void userSignInActivity(@RequestBody UserDTO user) {
        return;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public String userJoinActivity(@RequestBody UserDTO user) {
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userService.saveUser(user);
        System.out.println("user"+user);
        return "redirect:/api/auth/signin";

    }
}
