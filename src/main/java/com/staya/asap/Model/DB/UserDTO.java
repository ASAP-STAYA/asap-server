package com.staya.asap.Model.DB;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Data
@Entity
@Table(name = "user")
public class UserDTO {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column
    private int id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String role;
}
