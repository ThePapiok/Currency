package com.projekt.projekt.services;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.projekt.projekt.misc.CurrencyConverter;
import com.projekt.projekt.misc.Rate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

public class CurrencyServiceTest {

    @Mock
    private CurrencyConverter currencyConverter;
    @Mock
    private RestTemplate restTemplate;
    private CurrencyService currencyService;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        currencyService = new CurrencyService(currencyConverter);
        currencyService.setRestTemplate(restTemplate);
    }

    @Test
    public void shouldSuccessWhenGetRealAmountAtLocalDateTimeIsNullTest() throws JsonProcessingException {
        String json = "[" +
                "{" +
                "\"table\": \"A\"," +
                "\"no\": \"073/A/NBP/2024\"," +
                "\"effectiveDate\": \"2024-04-12\"," +
                "\"rates\": [" +
                "{" +
                "\"currency\": \"bat (Tajlandia)\"," +
                "\"code\": \"THB\"," +
                "\"mid\": 0.1097" +
                "}," +
                "{" +
                "\"currency\": \"dolar amerykański\"," +
                "\"code\": \"USD\"," +
                "\"mid\": 3.9983" +
                "}," +
                "{" +
                "\"currency\": \"dolar australijski\"," +
                "\"code\": \"AUD\"," +
                "\"mid\": 2.6044" +
                "}" +
                "]" +
                "}" +
                "]";
        ResponseEntity<String> response = new ResponseEntity<>(json,HttpStatus.OK);
        List<Rate> list = List.of(new Rate(0.1097, "THB"), new Rate(3.9983, "USD"), new Rate(2.6044, "AUD"));

        Mockito.when(restTemplate.exchange(eq("http://api.nbp.pl/api/exchangerates/tables/A"),
                eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(response);
        Mockito.when(currencyConverter.getList(json.substring(1, json.length()-1))).thenReturn(list);

        Assertions.assertEquals(81.96515, currencyService.getRealAmount(20.5, "USD"));
    }

    @Test
    public void shouldFailWhenGetRealAmountAtStatusIsNotOkTest() throws JsonProcessingException {
        String json = "[" +
                "{" +
                "\"table\": \"A\"," +
                "\"no\": \"073/A/NBP/2024\"," +
                "\"effectiveDate\": \"2024-04-12\"," +
                "\"rates\": [" +
                "{" +
                "\"currency\": \"bat (Tajlandia)\"," +
                "\"code\": \"THB\"," +
                "\"mid\": 0.1097" +
                "}," +
                "{" +
                "\"currency\": \"dolar amerykański\"," +
                "\"code\": \"USD\"," +
                "\"mid\": 3.9983" +
                "}," +
                "{" +
                "\"currency\": \"dolar australijski\"," +
                "\"code\": \"AUD\"," +
                "\"mid\": 2.6044" +
                "}" +
                "]" +
                "}" +
                "]";
        ResponseEntity<String> response = new ResponseEntity<>(json,HttpStatus.NOT_ACCEPTABLE);

        Mockito.when(restTemplate.exchange(eq("http://api.nbp.pl/api/exchangerates/tables/A"),
                eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(response);

        Assertions.assertNull(currencyService.getRealAmount(20.5, "USD"));
    }

    @Test
    public void shouldFailWhenGetRealAmountAtErrorInCurrencyConverterTest() throws JsonProcessingException {
        String json = "[[" +
                "{" +
                "\"table\": \"A\"," +
                "\"no\": \"073/A/NBP/2024\"," +
                "\"effectiveDate\": \"2024-04-12\"," +
                "\"rates\": [" +
                "{" +
                "\"currency\": \"bat (Tajlandia)\"," +
                "\"code\": \"THB\"," +
                "\"mid\": 0.1097" +
                "}," +
                "{" +
                "\"currency\": \"dolar amerykański\"," +
                "\"code\": \"USD\"," +
                "\"mid\": 3.9983" +
                "}," +
                "{" +
                "\"currency\": \"dolar australijski\"," +
                "\"code\": \"AUD\"," +
                "\"mid\": 2.6044" +
                "}" +
                "]" +
                "}" +
                "]]";
        ResponseEntity<String> response = new ResponseEntity<>(json,HttpStatus.OK);

        Mockito.when(restTemplate.exchange(eq("http://api.nbp.pl/api/exchangerates/tables/A"),
                eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(response);
        Mockito.when(currencyConverter.getList(json.substring(1, json.length()-1))).thenThrow(JsonProcessingException.class);

        Assertions.assertNull(currencyService.getRealAmount(20.5, "USD"));
    }

    @Test
    public void shouldSuccessWhenGetRealAmountAtBadCurrencyTest() throws JsonProcessingException {
        String json = "[" +
                "{" +
                "\"table\": \"A\"," +
                "\"no\": \"073/A/NBP/2024\"," +
                "\"effectiveDate\": \"2024-04-12\"," +
                "\"rates\": [" +
                "{" +
                "\"currency\": \"bat (Tajlandia)\"," +
                "\"code\": \"THB\"," +
                "\"mid\": 0.1097" +
                "}," +
                "{" +
                "\"currency\": \"dolar amerykański\"," +
                "\"code\": \"USD\"," +
                "\"mid\": 3.9983" +
                "}," +
                "{" +
                "\"currency\": \"dolar australijski\"," +
                "\"code\": \"AUD\"," +
                "\"mid\": 2.6044" +
                "}" +
                "]" +
                "}" +
                "]";
        ResponseEntity<String> response = new ResponseEntity<>(json,HttpStatus.OK);
        List<Rate> list = List.of(new Rate(0.1097, "THB"), new Rate(3.9983, "USD"), new Rate(2.6044, "AUD"));

        Mockito.when(restTemplate.exchange(eq("http://api.nbp.pl/api/exchangerates/tables/A"),
                eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(response);
        Mockito.when(currencyConverter.getList(json.substring(1, json.length()-1))).thenReturn(list);

        Assertions.assertNull(currencyService.getRealAmount(20.5, "PLN"));
    }

    @Test
    public void shouldSuccessGetRealAmountWhenLocalDateTimeIsBefore24HoursTest() throws JsonProcessingException {
        currencyService.setRates(List.of(new Rate(0.1097, "THB"), new Rate(3.9983, "USD"), new Rate(2.6044, "AUD")));
        currencyService.setLocalDateTime(LocalDateTime.now());

        Assertions.assertEquals(81.96515, currencyService.getRealAmount(20.5, "USD"));
    }

    @Test
    public void shouldSuccessWhenGetRealAmountAtLocalDateTimeIsAfter24HoursTest() throws JsonProcessingException {
        String json = "[" +
                "{" +
                "\"table\": \"A\"," +
                "\"no\": \"073/A/NBP/2024\"," +
                "\"effectiveDate\": \"2024-04-12\"," +
                "\"rates\": [" +
                "{" +
                "\"currency\": \"bat (Tajlandia)\"," +
                "\"code\": \"THB\"," +
                "\"mid\": 0.1097" +
                "}," +
                "{" +
                "\"currency\": \"dolar amerykański\"," +
                "\"code\": \"USD\"," +
                "\"mid\": 3.9983" +
                "}," +
                "{" +
                "\"currency\": \"dolar australijski\"," +
                "\"code\": \"AUD\"," +
                "\"mid\": 2.6044" +
                "}" +
                "]" +
                "}" +
                "]";
        ResponseEntity<String> response = new ResponseEntity<>(json,HttpStatus.OK);
        currencyService.setLocalDateTime(LocalDateTime.now().minusHours(25));
        List<Rate> list = List.of(new Rate(0.1097, "THB"), new Rate(3.9983, "USD"), new Rate(2.6044, "AUD"));

        Mockito.when(restTemplate.exchange(eq("http://api.nbp.pl/api/exchangerates/tables/A"),
                eq(HttpMethod.GET),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any())).thenReturn(response);
        Mockito.when(currencyConverter.getList(json.substring(1, json.length()-1))).thenReturn(list);

        Assertions.assertEquals(81.96515, currencyService.getRealAmount(20.5, "USD"));
    }

    @Test
    public void shouldSuccessWhenGetExchangeRateTest(){
        currencyService.setRates(List.of(new Rate(0.1097, "THB"), new Rate(3.9983, "USD"), new Rate(2.6044, "AUD")));

        Assertions.assertEquals(0.1097,currencyService.getExchangeRate("THB"));
    }

    @Test
    public void shouldFailWhenGetExchangeRateAtIncorrectCurrencyTest(){
        currencyService.setRates(List.of(new Rate(0.1097, "THB"), new Rate(3.9983, "USD"), new Rate(2.6044, "AUD")));

        Assertions.assertNull(currencyService.getExchangeRate("THBB"));
    }
}
