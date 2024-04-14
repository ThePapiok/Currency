package com.projekt.projekt.controllers;

import com.projekt.projekt.services.CurrencyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CurrencyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Test
    public void shouldStatusOkTest() throws Exception {
        Mockito.when(currencyService.getRealAmount(50.4, "USD")).thenReturn(100.2);

        MvcResult mvcResult = mockMvc.perform(post("/currency").param("amount", "50.4").param("currency", "USD")).andExpect(status().isOk()).andReturn();
        Assertions.assertEquals("100.2",mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void shouldStatusNotAcceptableTest() throws Exception {
        Mockito.when(currencyService.getRealAmount(50.4, "USD")).thenReturn(null);

        mockMvc.perform(post("/currency").param("amount", "50.4").param("currency", "USD")).andExpect(status().isNotAcceptable());
    }

}
