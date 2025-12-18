package com.pivovarit.domain.rental;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("")
class CalculatorConfiguration {

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
