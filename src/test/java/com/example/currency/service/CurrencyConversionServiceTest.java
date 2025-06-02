package com.example.currency.service;

import com.example.currency.cache.SimpleCache;
import com.example.currency.models.CurrencyRate;
import com.example.currency.repository.CurrencyRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyConversionServiceTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Mock
    private SimpleCache cacheService;

    @InjectMocks
    private CurrencyConversionService conversionService;

    @Test
    public void testGetBulkRates() {

        List<String> abbreviations = Arrays.asList("USD", "EUR");
        CurrencyRate usdRate = new CurrencyRate();
        usdRate.setCurOfficialRate(new java.math.BigDecimal("1.0"));
        CurrencyRate eurRate = new CurrencyRate();
        eurRate.setCurOfficialRate(new java.math.BigDecimal("0.85"));

        when(currencyService.getCurrencyRateByAbbreviation("USD")).thenReturn(usdRate);
        when(currencyService.getCurrencyRateByAbbreviation("EUR")).thenReturn(eurRate);


        List<CurrencyRate> rates = conversionService.getBulkRates(abbreviations);


        assertEquals(2, rates.size());
        assertEquals(usdRate, rates.get(0));
        assertEquals(eurRate, rates.get(1));
    }

    @Test
    public void testGetBulkRatesWithEmptyList() {

        List<String> abbreviations = Collections.emptyList();


        List<CurrencyRate> rates = conversionService.getBulkRates(abbreviations);


        assertTrue(rates.isEmpty());
    }

    @Test
    public void testGetBulkRatesWithInvalidAbbreviation() {

        List<String> abbreviations = Arrays.asList("USD", "XYZ");
        CurrencyRate usdRate = new CurrencyRate();
        usdRate.setCurOfficialRate(new java.math.BigDecimal("1.0"));
        when(currencyService.getCurrencyRateByAbbreviation("USD")).thenReturn(usdRate);
        when(currencyService.getCurrencyRateByAbbreviation("XYZ")).thenThrow(new IllegalArgumentException("Currency not found for abbreviation: XYZ"));


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                conversionService.getBulkRates(abbreviations));
        assertEquals("Currency not found for abbreviation: XYZ", exception.getMessage());
    }
}