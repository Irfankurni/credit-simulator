package com.simulator.controller;

import com.simulator.exception.ValidationException;
import com.simulator.factory.VehicleFactory;
import com.simulator.model.*;
import com.simulator.service.ApiService;
import com.simulator.service.LoanCalculationService;
import com.simulator.service.LoanValidationService;
import com.simulator.view.ConsoleView;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class LoanController {
    private final LoanValidationService validationService;
    private final LoanCalculationService calculationService;
    private final ApiService apiService;
    private final ConsoleView view;

    public LoanController(LoanValidationService validationService, LoanCalculationService calculationService, ApiService apiService, ConsoleView view) {
        this.validationService = validationService;
        this.calculationService = calculationService;
        this.apiService = apiService;
        this.view = view;
    }

    public void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            view.displayMenu();
            if (!scanner.hasNextLine()) break;
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleManualInput(scanner);
                case "2" -> handleApiLoad();
                case "3" -> {
                    System.out.println("Terima Kasih telah menggunakan Credit Simulator.");
                    running = false;
                }
                default -> view.displayError("Pilihan Menu tidak valid, Silahkan pilih 1, 2 atau 3.");
            }
        }
    }

    public void runBatchFileMode(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Path.of(filePath))
                    .stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                    .toList();
            if (lines.size() == 1 && lines.getFirst().equalsIgnoreCase("LOAD")) {
                handleApiLoad();
                return;
            }

            if (lines.size() < 6) {
                view.displayError("File input harus memiliki 6 baris parameter: Jenis, Kondisi, Tahun, Total Pinjaman, Tenor, DP.");
                return;
            }

            VehicleType type = VehicleType.fromString(lines.get(0));
            VehicleCondition condition = VehicleCondition.fromString(lines.get(1));
            int year = validationService.validateAndParseYear(lines.get(2), condition);
            BigDecimal totalLoan = validationService.validateAndParseLoanAmount(lines.get(3));
            int tenor = validationService.validateAndParseTenor(lines.get(4));
            BigDecimal dp = validationService.validateAndParseDownPayment(lines.get(5), totalLoan, condition);

            Vehicle vehicle = VehicleFactory.createVehicle(type.name(), condition.name(), year);
            LoanRequest request = new LoanRequest(vehicle, totalLoan, tenor, dp);

            List<InstallmentResult> results = calculationService.calculateInstallments(request);
            view.displayInstallments(results);
        } catch (IOException e) {
            view.displayError("Gagal membaca file: " + filePath + "(" + e.getMessage() + ")");
        } catch (ValidationException e) {
            view.displayError(e.getMessage());
        }
    }

    private void handleManualInput(Scanner scanner) {
        VehicleType type = promptUntilValid(() -> {
            System.out.print("1. Input Jenis Kendaraan (Motor/Mobil): ");
            return VehicleType.fromString(scanner.nextLine());
        });

        VehicleCondition condition = promptUntilValid(() -> {
            System.out.print("2. Input Kendaraan (Bekas/Baru): ");
            return VehicleCondition.fromString(scanner.nextLine());
        });

        int year = promptUntilValid(() -> {
            System.out.print("3. Input Tahun Kendaraan (4 Digit): ");
            return validationService.validateAndParseYear(scanner.nextLine(), condition);
        });

        BigDecimal totalLoan = promptUntilValid(() -> {
            System.out.print("4. Input Jumlah Pinjaman Total (<= 1 Miliar): ");
            return validationService.validateAndParseLoanAmount(scanner.nextLine());
        });

        int tenor = promptUntilValid(() -> {
            System.out.print("5. Input Tenor Pinjaman (1-6 thn): ");
            return validationService.validateAndParseTenor(scanner.nextLine());
        });

        BigDecimal dp = promptUntilValid(() -> {
            System.out.print("6. Input Jumlah DP: ");
            return validationService.validateAndParseDownPayment(scanner.nextLine(), totalLoan, condition);
        });

        Vehicle vehicle = VehicleFactory.createVehicle(type.name(), condition.name(), year);
        LoanRequest request = new LoanRequest(vehicle, totalLoan, tenor, dp);

        System.out.println("\nOutput Jumlah Cicilan Perbulan:");
        List<InstallmentResult> results = calculationService.calculateInstallments(request);
        view.displayInstallments(results);
    }

    private void handleApiLoad() {
        try {
            LoanRequest request = apiService.fetchExistingCalculation();
            view.displayLoadedData(request);
            List<InstallmentResult> results = calculationService.calculateInstallments(request);
            view.displayInstallments(results);
        } catch (ValidationException e) {
            view.displayError(e.getMessage());
        }
    }

    private <T> T promptUntilValid(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (ValidationException e) {
                view.displayError(e.getMessage());
            }
        }
    }
}
