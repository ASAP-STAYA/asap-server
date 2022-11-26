package com.staya.asap.Model.DB;

import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PreferenceDTO {

    private int id;

    private int user_id;
    private Double dist_prefer;
    private Double dist_weight;
    private Double cost_prefer;
    private Double cost_weight;

    private Boolean can_mechanical;
    private Boolean can_narrow;

}
