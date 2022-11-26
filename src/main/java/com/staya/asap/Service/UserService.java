package com.staya.asap.Service;

import com.staya.asap.Model.DB.UserDTO;
import com.staya.asap.Repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UserDTO getUserByEmail(String email) {
        return this.userRepo.findByEmail(email);
    }

    public void saveUser(UserDTO user) {
        this.userRepo.createUser(user);
    }
}
