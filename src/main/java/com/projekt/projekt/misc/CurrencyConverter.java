package com.projekt.projekt.misc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projekt.projekt.services.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CurrencyConverter {
    private final Logger logger = LoggerFactory.getLogger(CurrencyConverter.class);

    public List<Rate> getList(String json) throws JsonProcessingException {
        List<Rate> list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        logger.info(objectMapper.readTree(json).asText());
        for(JsonNode jsonNode : objectMapper.readTree(json).get("rates")){
            list.add(new Rate(Double.parseDouble(jsonNode.get("mid").asText()), jsonNode.get("code").asText()));
        }
        return list;
    }
}
