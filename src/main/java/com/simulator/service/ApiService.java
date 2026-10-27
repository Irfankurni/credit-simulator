package com.simulator.service;

import com.simulator.exception.ValidationException;
import com.simulator.factory.VehicleFactory;
import com.simulator.model.LoanRequest;
import com.simulator.model.Vehicle;
import com.simulator.model.VehicleCondition;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiService {
    private static final String MOCKY_URL = "https://run.mocky.io/v3/9108b1da-beec-409e-ae14-e8091955666c";

    private static final String FALLBACK_JSON = """
            {
              "vehicleType": "Mobil",
              "vehicleCondition": "Baru",
              "vehicleYear": 2025,
              "totalLoanAmount": 1000000000,
              "loanTenure": 6,
              "downPayment": 500000000
            }
            """;

    private final LoanValidationService validationService;
    private final HttpClient httpClient;

    public ApiService(LoanValidationService validationService) {
        this.validationService = validationService;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public LoanRequest fetchExistingCalculation() {
        String jsonBody;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(MOCKY_URL))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200 && response.body() != null && !response.body().isBlank()) {
                jsonBody = response.body();
            } else {
                jsonBody = FALLBACK_JSON;
            }
        } catch (Exception e) {
            jsonBody = FALLBACK_JSON;
        }

        return parseJsonToLoanRequest(jsonBody);
    }

    public LoanRequest parseJsonToLoanRequest(String json) {
        String typeStr = extractValue(json, "vehicleType");
        String conditionStr = extractValue(json, "vehicleCondition");
        String yearStr = extractValue(json, "vehicleYear");
        String amountStr = extractValue(json, "totalLoanAmount");
        String tenureStr = extractValue(json, "loanTenure");
        String dpStr = extractValue(json, "downPayment");

        VehicleCondition condition = VehicleCondition.fromString(conditionStr);
        int year = validationService.validateAndParseYear(yearStr, condition);
        Vehicle vehicle = VehicleFactory.createVehicle(typeStr, conditionStr, year);
        BigDecimal totalLoan = validationService.validateAndParseLoanAmount(amountStr);
        int tenor = validationService.validateAndParseTenor(tenureStr);
        BigDecimal dp = validationService.validateAndParseDownPayment(dpStr, totalLoan, condition);

        return new LoanRequest(vehicle, totalLoan, tenor, dp);
    }

    private String extractValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"?([^,\"}]+)\"?");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        throw new ValidationException("Field '" + key + "' tidak ditemukan pada response JSON.");
    }
}
