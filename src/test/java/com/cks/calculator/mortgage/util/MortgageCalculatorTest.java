package com.cks.calculator.mortgage.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MortgageCalculatorTest {

    @Spy
    MathContext mathContext = new MathContext(10, RoundingMode.HALF_UP);

    @InjectMocks
    MortgageCalculator calculator;

    @Nested
    class GetPaymentTest {
        @ParameterizedTest
        @ArgumentsSource(GetPaymentTestArguments.class)
        void whenValid(TestModel model, BigDecimal expectedPayment) {
            MortgageCalculationResult actual = calculator.calculate(model.rate, model.principal, model.amortization);

            assertThat(actual.mortgagePayment()).isCloseTo(expectedPayment, withPercentage(0.01));

            verify(mathContext, atLeastOnce()).getPrecision();
            verify(mathContext, atLeastOnce()).getRoundingMode();
        }

        static class GetPaymentTestArguments implements ArgumentsProvider {
            @Override
            public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
                return Stream.of(
                        Arguments.of(new TestModel(0.05, 200_000, 25 * 12), BigDecimal.valueOf(1163.21)),
                        Arguments.of(new TestModel(0.04, 250_000, 30 * 12), BigDecimal.valueOf(1188.80)),
                        Arguments.of(new TestModel(0.035, 500_000, 20 * 12), BigDecimal.valueOf(2893.32))
                );
            }
        }

        record TestModel(BigDecimal rate, BigDecimal principal, int amortization) {
            public TestModel(double rate, double principal, int amortization) {
                this(BigDecimal.valueOf(rate), BigDecimal.valueOf(principal), amortization);
            }
        }
    }

    @Nested
    class GetEffectiveMonthlyRateTest {
        @ParameterizedTest
        @ArgumentsSource(GetEffectiveMonthlyRateTestArguments.class)
        void whenValid(TestModel model, BigDecimal expectedEffectiveMonthlyRate) {
            BigDecimal actual = calculator.getEffectiveMonthlyRate(model.rate);

            assertThat(actual).isCloseTo(expectedEffectiveMonthlyRate, withPercentage(0.001));

            verify(mathContext, atLeastOnce()).getPrecision();
            verify(mathContext, atLeastOnce()).getRoundingMode();
        }

        static class GetEffectiveMonthlyRateTestArguments implements ArgumentsProvider {
            @Override
            public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
                return Stream.of(
                        Arguments.of(new TestModel(0.035), BigDecimal.valueOf(0.289562 / 100)),
                        Arguments.of(new TestModel(0.04), BigDecimal.valueOf(0.330589 / 100)),
                        Arguments.of(new TestModel(0.05), BigDecimal.valueOf(0.412392 / 100))
                );
            }
        }

        record TestModel(BigDecimal rate) {
            public TestModel(double rate) {
                this(BigDecimal.valueOf(rate));
            }
        }
    }
}
