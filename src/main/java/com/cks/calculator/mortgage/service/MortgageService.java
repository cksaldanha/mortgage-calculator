package com.cks.calculator.mortgage.service;

import com.cks.calculator.mortgage.model.MortgageQuery;
import com.cks.calculator.mortgage.util.MortgageCalculationResult;

public interface MortgageService {

    MortgageCalculationResult calculateMortgage(MortgageQuery query);

}
