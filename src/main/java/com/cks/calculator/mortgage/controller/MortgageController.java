package com.cks.calculator.mortgage.controller;

import com.cks.calculator.mortgage.dto.MortgageRequestDto;
import com.cks.calculator.mortgage.dto.MortgageResponseDto;
import com.cks.calculator.mortgage.mapper.MortgageRequestMapper;
import com.cks.calculator.mortgage.mapper.MortgageResponseMapper;
import com.cks.calculator.mortgage.model.MortgageQuery;
import com.cks.calculator.mortgage.service.MortgageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/mortgages")
public class MortgageController {

    private final MortgageService mortgageService;
    private final MortgageRequestMapper requestMapper;
    private final MortgageResponseMapper responseMapper;

    @Autowired
    public MortgageController(MortgageService mortgageService,
                              MortgageRequestMapper requestMapper,
                              MortgageResponseMapper responseMapper) {
        this.mortgageService = mortgageService;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @GetMapping
    public ResponseEntity<MortgageResponseDto> getMortgage(@Valid @ModelAttribute MortgageRequestDto requestDto) {
        MortgageQuery query = requestMapper.toQuery(requestDto);

        MortgageResponseDto dto = responseMapper.fromQueryAndResult(query, mortgageService.calculateMortgage(query));

        return ResponseEntity.ok(dto);
    }
}
