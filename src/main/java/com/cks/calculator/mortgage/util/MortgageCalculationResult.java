package com.cks.calculator.mortgage.util;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MortgageCalculationResult(
        BigDecimal mortgagePayment,
        BigDecimal effectiveMonthlyRate,
        BigDecimal principal,
        int amortization
) {

    public MortgageCalculationResult(double mortgagePayment,
                                     double effectiveMonthlyRate,
                                     double principal,
                                     int amortization) {
        this(BigDecimal.valueOf(mortgagePayment),
                BigDecimal.valueOf(effectiveMonthlyRate),
                BigDecimal.valueOf(principal),
                amortization);
    }
}
