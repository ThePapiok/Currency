package com.projekt.projekt.misc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CurrencyConverterTest {
    private CurrencyConverter currencyConverter;

    @BeforeEach
    public void setUp(){
        currencyConverter = new CurrencyConverter();
    }


    @Test
    public void shouldSuccessWhenGetListTest() throws JsonProcessingException {
        String json = "{\"rates\": [{\"currency\": \"dolar amerykański\"," +
                "\"code\": \"USD\"," +
                "\"mid\": 3.9983}, {\"currency\": \"bat (Tajlandia)\"," +
                "\"code\": \"THB\"," +
                "\"mid\": 0.1097} ]}";
        List<Rate> expected = List.of(new Rate(3.9983, "USD"), new Rate(0.1097, "THB"));

        List<Rate> list = currencyConverter.getList(json);
        Assertions.assertEquals(list.get(0).getCurrency(), expected.get(0).getCurrency());
        Assertions.assertEquals(list.get(1).getCurrency(), expected.get(1).getCurrency());
        Assertions.assertEquals(list.get(0).getRate(), expected.get(0).getRate());
        Assertions.assertEquals(list.get(1).getRate(), expected.get(1).getRate());
    }

    @Test
    public void shouldThrowExceptionWhenGetListTest() throws JsonProcessingException {
        String json = "{\"ratess\": [{\"currency\": \"dolar amerykański\"," +
                "\"code\": \"USD\"," +
                "\"mid\": 3.9983}, {\"currency\": \"bat (Tajlandia)\"," +
                "\"code\": \"THB\"," +
                "\"mid\": 0.1097} ]}";

        Assertions.assertThrows(NullPointerException.class, () ->{
            currencyConverter.getList(json);

        });

    }
}
