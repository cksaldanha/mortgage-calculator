package com.cks.calculator.mortgage.controller;

import com.cks.calculator.mortgage.mapper.MortgageRequestMapper;
import com.cks.calculator.mortgage.mapper.MortgageResponseMapper;
import com.cks.calculator.mortgage.model.MortgageQuery;
import com.cks.calculator.mortgage.service.MortgageService;
import com.cks.calculator.mortgage.util.MortgageCalculationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Named.named;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MortgageController.class)
class MortgageControllerTest {

    static final String BASE_PATH = "/v1/mortgages";

    @MockBean
    MortgageService mortgageService;

    @SpyBean
    MortgageRequestMapper requestMapper;

    @SpyBean
    MortgageResponseMapper responseMapper;

    @Autowired
    MockMvc mvc;

    @Nested
    class GetMortgageTest {

        MortgageCalculationResult sampleResult;

        @BeforeEach
        void setUp() {
            sampleResult = MortgageCalculationResult.builder()
                    .mortgagePayment(BigDecimal.valueOf(1234.56))
                    .effectiveMonthlyRate(BigDecimal.valueOf(0.03245))
                    .amortization(300)
                    .principal(BigDecimal.valueOf(200000))
                    .build();
        }

        @ParameterizedTest
        @MethodSource("validParams")
        void whenValid(MultiValueMap<String, String> params) throws Exception {
            when(mortgageService.calculateMortgage(any(MortgageQuery.class))).thenReturn(sampleResult);

            mvc.perform(get(BASE_PATH)
                            .params(params)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amount", is(1234.56)));
        }

        @ParameterizedTest
        @MethodSource("invalidParams")
        void whenInvalid(MultiValueMap<String, String> params) throws Exception {
            when(mortgageService.calculateMortgage(any(MortgageQuery.class))).thenReturn(sampleResult);

            mvc.perform(get(BASE_PATH)
                            .params(params)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }

        static Stream<MultiValueMap<String, String>> validParams() {
            return Stream.of(
                buildParams(withValidParams())
            );
        }

        static Stream<Named<MultiValueMap<String, String>>> invalidParams() {
            return Stream.of(
                named("without rate", buildParams(withValidParams().andThen(deleteParam("rate")))),
                named("without principal", buildParams(withValidParams().andThen(deleteParam("principal")))),
                named("without term", buildParams(withValidParams().andThen(deleteParam("term")))),
                named("without amortization", buildParams(withValidParams().andThen(deleteParam("amortization")))),
                named("negative rate", buildParams(withValidParams().andThen(setParam("rate", "-0.05")))),
                named("negative principal", buildParams(withValidParams().andThen(setParam("principal", "-200000")))),
                named("negative term", buildParams(withValidParams().andThen(setParam("term", "-5")))),
                named("negative amortization", buildParams(withValidParams().andThen(setParam("amortization", "-300"))))
            );
        }
    }

    static Consumer<MultiValueMap<String, String>> withValidParams() {
        return params -> {
            params.set("rate", "0.05");
            params.set("principal", "200000");
            params.set("amortization", "300");
            params.set("term", "5");
        };
    }

    static Consumer<MultiValueMap<String, String>> setParam(String key, String value) {
        return params -> params.set(key, value);
    }

    static Consumer<MultiValueMap<String, String>> deleteParam(String key) {
        return params -> params.remove(key);
    }

    static MultiValueMap<String, String> buildParams(Consumer<MultiValueMap<String, String>> modifier) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        modifier.accept(params);
        return params;
    }
}
