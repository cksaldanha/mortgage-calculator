package com.cks.calculator.mortgage.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@Builder
public class MortgageRequestDto {

    @Positive
    @DecimalMin("0.01")
    @DecimalMax("0.5")
    private double rate;

    @Positive
    @DecimalMin("1000")
    @DecimalMax("1000000")
    private double principal;

    @Positive
    @Range(min = 12, max = 360)
    private int amortization;

    @Positive
    @Range(min = 1, max = 10)
    private int term;

}
