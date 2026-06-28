package com.cks.calculator.mortgage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MortgageResponseDto {

    private double amount;
    private double principal;
    private RateDto rate;
    private int amortization;
    private int term;

}
