package com.staya.asap.Controller;

import com.staya.asap.Configuration.Security.Auth.JwtTokenProvider;
import com.staya.asap.Model.DB.PreferenceDTO;
import com.staya.asap.Model.DB.UserDTO;
import com.staya.asap.Service.PreferenceService;
import com.staya.asap.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(maxAge = 3600)
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PreferenceService preferenceService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthController(JwtTokenProvider jwtTokenProvider,
                          UserService userService,
                          PreferenceService preferenceService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.preferenceService = preferenceService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signin")
    public String userSignInActivity(@RequestBody UserDTO user, HttpServletRequest request) {

        UserDTO user_check = userService.getUserByEmail(user.getEmail());
        return jwtTokenProvider.createToken(user_check.getEmail(), Collections.singletonList(user_check.getRole()));
    }

    @PostMapping("/signup/user")
    @ResponseStatus(HttpStatus.OK)
    public String userJoinActivity(@RequestBody UserDTO user) {

        UserDTO user_check = userService.getUserByEmail(user.getEmail());
        if (user_check != null) {
            return "[ERROR] same user";
        } else {
            final String rawPassword = user.getPassword();
            final String encPassword = bCryptPasswordEncoder.encode(rawPassword);
            user.setPassword(encPassword);
            user.setRole("ROLE_USER");
            userService.saveUser(user);
            System.out.println("user" + user);

            return Integer.toString(userService.getUserByEmail(user.getEmail()).getId());
        }
    }

    @PostMapping("/signup/preference")
    @ResponseStatus(HttpStatus.OK)
    public void preferenceJoinActivity(@RequestBody PreferenceDTO preference) {
        preferenceService.createPreference(preference);
    }

    @GetMapping("/signout")
    public String userSignOutActivity(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.removeAttribute("loginUser");
        session.invalidate();

        return "signout success";
    }
}
