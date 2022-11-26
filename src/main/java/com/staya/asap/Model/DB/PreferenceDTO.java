package com.staya.asap.Model.DB;

import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "preference")
public class PreferenceDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private int user_id;

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
