package com.projekt.projekt.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projekt.projekt.misc.CurrencyConverter;
import com.projekt.projekt.misc.Rate;
import com.zaxxer.hikari.util.ClockSource;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;

@Service
public class CurrencyService {

    private List<Rate> rates;

    private LocalDateTime localDateTime=null;
    private final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyConverter currencyConverter;

    public CurrencyService(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    public Double getRealAmount(double amount, String currency) throws JsonProcessingException {
        if(localDateTime==null || localDateTime.isBefore(localDateTime.plusHours(24))){
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange("http://api.nbp.pl/api/exchangerates/tables/A", HttpMethod.GET, httpEntity, String.class);
            String json = response.getBody();
            if(!response.getStatusCode().equals(HttpStatus.OK)){
                logger.error("bad status");
                return null;
            }
            localDateTime = LocalDateTime.now();
            try {
                rates = currencyConverter.getList(json.substring(1, json.length()-1));
            }
            catch (Exception e){
                logger.error("jsonMapper - ", e);
                return null;
            }
        }
        return amount*getExchangeRate(currency);
    }

    private Double getExchangeRate(String currency){
        for(Rate rate: rates){
            if(rate.getCurrency().equals(currency)){
                return rate.getRate();
            }
        }
        return null;
    }
}