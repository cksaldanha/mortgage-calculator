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
}
