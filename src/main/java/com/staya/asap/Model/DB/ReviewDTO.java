package com.staya.asap.Model.DB;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Data
@Entity
@Table(name = "userreview")
public class ReviewDTO {
    @Id
    private int id;

    @Column
    private double dist;
    @Column
    private double cost;
    @Column
    private int discontent;
    @Column
    private int user_id;

}
