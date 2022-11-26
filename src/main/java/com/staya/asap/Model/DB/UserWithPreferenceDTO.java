package com.staya.asap.Model.DB;

import javax.persistence.Column;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserWithPreferenceDTO {
    private String username;
    private String email;
    private String password;
    private String role;

    private Double dist_prefer;
    private Double dist_weight;
    private Double cost_prefer;
    private Double cost_weight;

    private Boolean can_mechanical;
    private Boolean can_narrow;
}
