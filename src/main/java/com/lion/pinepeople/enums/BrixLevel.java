package com.lion.pinepeople.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BrixLevel {
    BAD("BAD", 15),
    SOSO("SO-SO",25),
    GOOD("GOOD",35),
    BETTER("BETTER",45),
    BEST("BEST",50);
    private String brixName;
    private Integer brixNum;
}
