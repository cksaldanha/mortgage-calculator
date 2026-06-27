package com.cks.calculator.mortgage.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

@ExtendWith(MockitoExtension.class)
class MortgageCalculatorTest {

    @Spy
    MathContext mathContext = new MathContext(10, RoundingMode.HALF_UP);

    @InjectMocks
    MortgageCalculator calculator;

    @Nested
    class GetPaymentTest {
        @Test
        void whenValid() {
            BigDecimal rate = BigDecimal.valueOf(0.05);
            BigDecimal principal = BigDecimal.valueOf(200_000);
            int amortization = 300;

            MortgageCalculationResult result = calculator.calculate(rate, principal, amortization);

            assertThat(result.mortgagePayment()).isCloseTo(BigDecimal.valueOf(1163.21), withPercentage(0.1));
        }
    }

    @Nested
    class GetEffectiveMonthlyRateTest {
        @Test
        void whenValid() {
            BigDecimal rate = BigDecimal.valueOf(0.05);

            BigDecimal actual = calculator.getEffectiveMonthlyRate(rate);

            assertThat(actual).isCloseTo(BigDecimal.valueOf(0.00412392), withPercentage(0.1));
        }
    }
}
