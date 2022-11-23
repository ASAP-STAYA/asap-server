package com.staya.asap.Model.DB;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class ParkingDTO {

    private int id;

    private Integer PARKING_CODE;
    private String PARKING_NAME;
    private String ADDR;
    private String PARKING_TYPE;
    private String TEL;

    private Integer CAPACITY;
    private Integer CAPACITY_AVAILABLE;

    private Integer WEEKDAY_BEGIN_TIME;
    private Integer WEEKDAY_END_TIME;
    private Integer WEEKEND_BEGIN_TIME;
    private Integer WEEKEND_END_TIME;
    private Integer HOLIDAY_BEGIN_TIME;
    private Integer HOLIDAY_END_TIME;

    private Boolean PAY_YN;
    private Boolean SATURDAY_PAY_YN;
    private Boolean HOLIDAY_PAY_YN;

    private Integer RATES;
    private Integer TIME_RATE;
    private Integer ADD_RATES;
    private Integer ADD_TIME_RATE;
    private Integer DAY_MAXIMUM;

    private Double LAT;
    private Double LNG;

    private Boolean WIDE_YN;
    private Boolean MECHANICAL_YN;
    private Integer RATES_PER_HOUR;

    private Float distance;
    private double Cost;
}
