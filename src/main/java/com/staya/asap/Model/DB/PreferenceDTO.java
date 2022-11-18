package com.staya.asap.Model.DB;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "user_preference")
public class PreferenceDTO {

    @Id
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserDTO USER_ID;

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
