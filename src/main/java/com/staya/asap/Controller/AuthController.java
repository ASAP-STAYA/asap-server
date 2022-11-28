package com.staya.asap.Controller;

import com.staya.asap.Configuration.Security.Auth.JwtTokenProvider;
import com.staya.asap.Configuration.Security.Auth.PrincipalDetails;
import com.staya.asap.Model.DB.PreferenceDTO;
import com.staya.asap.Model.DB.UserDTO;
import com.staya.asap.Model.DB.UserWithPreferenceDTO;
import com.staya.asap.Service.PreferenceService;
import com.staya.asap.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/islogin")
    public Boolean userIsLogin() {
        final PrincipalDetails user = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if (user != null) {
            return true;
        }
        return false;
    }

    @PostMapping("/signin")
    public String userSignInActivity(@RequestBody UserDTO user, HttpServletRequest request) {

        UserDTO user_check = userService.getUserByEmail(user.getEmail());
        return jwtTokenProvider.createToken(user_check.getEmail(), Collections.singletonList(user_check.getRole()));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public String userJoinActivity(@RequestBody UserWithPreferenceDTO userWithPreference) {

        UserDTO user_check = userService.getUserByEmail(userWithPreference.getEmail());
        if (user_check != null) {
            return "[ERROR] same user";
        } else {
            final String rawPassword = userWithPreference.getPassword();
            final String encPassword = bCryptPasswordEncoder.encode(rawPassword);
            userWithPreference.setPassword(encPassword);
            userWithPreference.setRole("ROLE_USER");

            UserDTO user = new UserDTO();
            user.setUsername(userWithPreference.getUsername());
            user.setRole(userWithPreference.getRole());
            user.setPassword(userWithPreference.getPassword());
            user.setEmail(userWithPreference.getEmail());
            userService.saveUser(user);
            System.out.println("user" + user.getEmail());

            PreferenceDTO preference = new PreferenceDTO();
            preference.setUser_id(userService.getUserByEmail(user.getEmail()).getId());
            preference.setDist_prefer(userWithPreference.getDist_prefer());
            preference.setCost_prefer(userWithPreference.getCost_prefer());
            preference.setCan_mechanical(userWithPreference.getCan_mechanical());
            preference.setCan_narrow(userWithPreference.getCan_narrow());

            preferenceService.createPreference(preference);

            return "sign up success";
        }
    }

    @GetMapping("/signout")
    public String userSignOutActivity(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.removeAttribute("loginUser");
        session.invalidate();

        return "signout success";
    }
}
