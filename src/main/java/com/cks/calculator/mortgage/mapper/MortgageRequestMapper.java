package com.cks.calculator.mortgage.mapper;

import com.cks.calculator.mortgage.dto.MortgageRequestDto;
import com.cks.calculator.mortgage.model.MortgageQuery;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MortgageRequestMapper {

    public MortgageQuery toQuery(MortgageRequestDto dto) {
        return MortgageQuery.builder()
                .principal(BigDecimal.valueOf(dto.getPrincipal()))
                .rate(BigDecimal.valueOf(dto.getRate()))
                .term(dto.getTerm())
                .amortization(dto.getAmortization())
                .build();
    }
}
