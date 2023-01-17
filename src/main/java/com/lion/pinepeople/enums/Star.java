package com.lion.pinepeople.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Star {
    ONE(1, -1.0),
    TWO(2, -0.5),
    THREE(3, 0.3),
    FOUR(4, 1.0),
    FIVE(5, 1.3);
    private Integer starNum;
    private double brixNum;

    public static double starToBrix(Integer starNum){
        double result = 0.0;
        for (Star star: Star.values()) {
            if (star.starNum == starNum){
                result =  star.brixNum;
            }
        }
        return result;
    }
}

