package com.staya.asap.Model.DB;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Data
public class UserDTO {

    private int id;

    private String username;
    private String email;
    private String password;
    private String role;
}
