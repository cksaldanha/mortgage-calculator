package com.cks.calculator.mortgage.util;


import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.ONE;

@Slf4j
@Component
public class MortgageCalculator {

    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private final MathContext mathContext;

    @Autowired
    public MortgageCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    public MortgageCalculationResult calculate(BigDecimal rate, BigDecimal principal, int amortization) {
        BigDecimal effectiveRate = getEffectiveMonthlyRate(rate);

        BigDecimal payment = getPayment(principal, effectiveRate, amortization);

        return MortgageCalculationResult.builder()
                .effectiveMonthlyRate(effectiveRate)
                .mortgagePayment(payment)
                .amortization(amortization)
                .principal(principal)
                .build();
    }

    public BigDecimal getPayment(BigDecimal principal, BigDecimal effectiveRate, int amortization) {
        MathContext mc = mathContext;
        BigDecimal numerator = principal.multiply(effectiveRate, mc);
        BigDecimal denominator = ONE.subtract(BigDecimalMath.pow(effectiveRate.add(ONE, mc), BigDecimal.valueOf(amortization).negate(), mc));
        return numerator.divide(denominator, mc);
    }

    public BigDecimal getEffectiveMonthlyRate(BigDecimal rate) {
        final MathContext mc = mathContext;
        BigDecimal rateOverTwo = rate.divide(BigDecimal.valueOf(2), mc);
        BigDecimal squared = BigDecimalMath.pow(rateOverTwo.add(ONE, mc), TWO, mc);
        BigDecimal oneOverTwelve = ONE.divide(BigDecimal.valueOf(12), mc);
        return BigDecimalMath.pow(squared, oneOverTwelve, mc).subtract(ONE, mc);
    }
}
