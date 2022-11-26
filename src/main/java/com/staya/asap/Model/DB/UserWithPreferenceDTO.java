package com.staya.asap.Model.DB;

import javax.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithPreferenceDTO {
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String role;

    @Column
    private Double dist_prefer;
    @Column
    private Double dist_weight;
    @Column
    private Double cost_prefer;
    @Column
    private Double cost_weight;

    @Column
    private Boolean can_mechanical;
    @Column
    private Boolean can_narrow;
}
