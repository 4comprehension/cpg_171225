package com.pivovarit.rental.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties("")
public class CalculatorConfiguration {

    public final Map<String, Integer> pricing;

    public Map<String, Integer> getPricing() {
        return pricing;
    }

    public CalculatorConfiguration(Map<String, Integer> pricing) {
        this.pricing = pricing;
    }

    @Override
    public String toString() {
        return "CalculatorConfiguration{pricing=%s}".formatted(pricing);
    }
}
