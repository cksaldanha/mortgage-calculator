package com.cks.calculator.mortgage.mapper;

import com.cks.calculator.mortgage.dto.MortgageResponseDto;
import com.cks.calculator.mortgage.dto.RateDto;
import com.cks.calculator.mortgage.model.MortgageQuery;
import com.cks.calculator.mortgage.util.MortgageCalculationResult;
import org.springframework.stereotype.Component;

@Component
public class MortgageResponseMapper {

    public MortgageResponseDto fromQueryAndResult(MortgageQuery query, MortgageCalculationResult result) {
        return MortgageResponseDto.builder()
                .term(query.term())
                .amortization(query.amortization())
                .amount(result.mortgagePayment().doubleValue())
                .principal(query.principal().doubleValue())
                .rate(RateDto.builder()
                        .nominal(query.rate().doubleValue())
                        .effective(result.effectiveMonthlyRate().doubleValue() * 12)
                        .build())
                .build();
    }
}
