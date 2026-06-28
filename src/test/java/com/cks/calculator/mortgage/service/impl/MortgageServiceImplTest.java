package com.cks.calculator.mortgage.service.impl;

import com.cks.calculator.mortgage.model.MortgageQuery;
import com.cks.calculator.mortgage.util.MortgageCalculationResult;
import com.cks.calculator.mortgage.util.MortgageCalculator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MortgageServiceImplTest {

    @Mock
    MortgageCalculator calculator;

    @InjectMocks
    MortgageServiceImpl service;

    @Nested
    class CalculateMortgageRest {

        @ParameterizedTest
        @ArgumentsSource(ValidTestArguments.class)
        void whenValidResult(MortgageCalculationResult calculationResult) {
            when(calculator.calculate(any(BigDecimal.class), any(BigDecimal.class), anyInt())).thenReturn(calculationResult);

            BigDecimal rate = BigDecimal.valueOf(0.0425);
            BigDecimal principal = BigDecimal.valueOf(231_234);
            int amortization = 300;
            int term = 5;

            var query = MortgageQuery.builder()
                    .rate(rate)
                    .principal(principal)
                    .amortization(amortization)
                    .term(term)
                    .build();

            MortgageCalculationResult actual = service.calculateMortgage(query);

            assertThat(actual)
                    .isNotEqualTo(calculationResult)
                    .hasNoNullFieldsOrProperties();

            verify(calculator).calculate(rate, principal, amortization);
        }

        static class ValidTestArguments implements ArgumentsProvider {
            @Override
            public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
                return Stream.of(
                        Arguments.of(new MortgageCalculationResult(1234.5678, 0.0123456, 200_000, 300))
                );
            }
        }
    }
}
