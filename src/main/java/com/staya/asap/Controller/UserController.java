package com.staya.asap.Controller;

import com.staya.asap.Model.DB.UserDTO;
import com.staya.asap.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserInfo(@PathVariable("id") Integer id){
        return userService.getUserById(id);
    }

    @PostMapping("/signup")
    public Integer userJoinActivity() {
        System.out.println("userJoinActivity");
        return 0;
    }
}
