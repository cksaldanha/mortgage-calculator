package com.cks.calculator.mortgage.service.impl;

import com.cks.calculator.mortgage.model.MortgageQuery;
import com.cks.calculator.mortgage.service.MortgageService;
import com.cks.calculator.mortgage.util.MortgageCalculationResult;
import com.cks.calculator.mortgage.util.MortgageCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

@Slf4j
@Service
public class MortgageServiceImpl implements MortgageService {

    private final MortgageCalculator calculator;

    @Autowired
    public MortgageServiceImpl(MortgageCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public MortgageCalculationResult calculateMortgage(MortgageQuery query) {
        MortgageCalculationResult result = calculator.calculate(query.rate(), query.principal(), query.amortization());

        return MortgageCalculationResult.builder()
                .mortgagePayment(result.mortgagePayment().setScale(2, RoundingMode.HALF_UP))
                .effectiveMonthlyRate(result.effectiveMonthlyRate().setScale(5, RoundingMode.HALF_UP))
                .amortization(result.amortization())
                .principal(result.principal())
                .build();
    }
}
