package com.staya.asap.Controller;

import com.staya.asap.Model.DB.UserDTO;
import com.staya.asap.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController //@Controller + @ResponseBody
@RequestMapping("/api/user")
@CrossOrigin(maxAge = 3600)
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserInfo(@PathVariable("id") String id){
        return userService.getUserByEmail(id);
    }

    @GetMapping("/")
    public String user(HttpServletRequest request) {
        System.out.println("user request: "+request.getHeader("Authorization"));
        return "user";
    }

}
