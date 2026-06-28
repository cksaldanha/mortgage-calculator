package com.cks.calculator.mortgage.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MortgageQuery(BigDecimal rate, BigDecimal principal, int amortization, int term) {

    public MortgageQuery(double rate, double principal, int amortization, int term) {
        this(BigDecimal.valueOf(rate),
                BigDecimal.valueOf(principal),
                amortization,
                term
        );
    }
}
