package com.projekt.projekt.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.projekt.projekt.services.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<Double> getAmount(@RequestParam double amount, @RequestParam String currency) throws JsonProcessingException {
        Double realAmount = currencyService.getRealAmount(amount, currency);
        if(realAmount==null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(realAmount);
        }
    }
}
