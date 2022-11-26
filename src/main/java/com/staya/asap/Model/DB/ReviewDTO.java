package com.staya.asap.Model.DB;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Data

public class ReviewDTO {

    private int id;

    private int user_id;
    private double dist;
    private double cost;
    private int discontent;

}
