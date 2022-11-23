package com.staya.asap.Model.DB;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Data
@Entity
@Table(name = "parkinglot")
public class ParkingDTO {

    @Id
    private int id;

    @Column
    private Integer PARKING_CODE;
    @Column
    private String PARKING_NAME;
    @Column
    private String ADDR;
    @Column
    private String PARKING_TYPE;
    @Column
    private String TEL;

    @Column
    private Integer CAPACITY;
    @Column
    private Integer CAPACITY_AVAILABLE;

    @Column
    private Integer WEEKDAY_BEGIN_TIME;
    @Column
    private Integer WEEKDAY_END_TIME;
    @Column
    private Integer WEEKEND_BEGIN_TIME;
    @Column
    private Integer WEEKEND_END_TIME;
    @Column
    private Integer HOLIDAY_BEGIN_TIME;
    @Column
    private Integer HOLIDAY_END_TIME;

    @Column
    private Boolean PAY_YN;
    @Column
    private Boolean SATURDAY_PAY_YN;
    @Column
    private Boolean HOLIDAY_PAY_YN;

    @Column
    private Integer RATES;
    @Column
    private Integer TIME_RATE;
    @Column
    private Integer ADD_RATES;
    @Column
    private Integer ADD_TIME_RATE;
    @Column
    private Integer DAY_MAXIMUM;

    @Column
    private Double LAT;
    @Column
    private Double LNG;

    @Column
    private Boolean WIDE_YN;
    @Column
    private Boolean MECHANICAL_YN;
    @Column
    private Integer RATES_PER_HOUR;

    @Column
    private Float distance;
    @Column
    private double Cost;
}
