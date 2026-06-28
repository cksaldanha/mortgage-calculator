package com.cks.calculator.mortgage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RateDto {
    private double nominal;
    private double effective;
}
