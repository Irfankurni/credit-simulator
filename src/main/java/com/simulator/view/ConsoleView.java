package com.simulator.view;

import com.simulator.model.InstallmentResult;
import com.simulator.model.LoanRequest;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class ConsoleView {
    private final DecimalFormat currencyFormat;
    private final DecimalFormat rateFormat;

    public ConsoleView() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        this.currencyFormat = new DecimalFormat("#,##0.00", symbols);

        DecimalFormatSymbols rateSymbols = new DecimalFormatSymbols(Locale.GERMANY);
        this.rateFormat = new DecimalFormat("#.##", rateSymbols);
    }

    public void displayMenu() {
        System.out.println("\n=== CREDIT SIMULATOR ===");
        System.out.println("1. Input Simulasi Baru");
        System.out.println("2. Load Existing Calculation (From API)");
        System.out.println("3. Exit");
        System.out.print("Pilih menu (1-3): ");
    }

    public void displayLoadedData(LoanRequest request) {
        System.out.println("\n--- Data Loaded ---");
        System.out.println("Jenis Kendaraan   : " + request.vehicle().getType());
        System.out.println("Kondisi           : " + request.vehicle().getCondition());
        System.out.println("Tahun Kendaraan   : " + request.vehicle().getYear());
        System.out.println("Total Pinjaman    : Rp. " + currencyFormat.format(request.totalLoanAmount()));
        System.out.println("Tenor Pinjaman    : " + request.tenorYears() + " tahun");
        System.out.println("Jumlah DP         : Rp. " + currencyFormat.format(request.downPayment()));
        System.out.println("-------------------");
    }

    public void displayInstallments(List<InstallmentResult> results) {
        for (InstallmentResult res : results) {
            System.out.printf("Tahun %d : Rp. %s/bln , Suku Bunga : %s%%%n",
                    res.year(),
                    currencyFormat.format(res.monthlyInstallment()),
                    rateFormat.format(res.interestRatePercent()));
        }
    }

    public void displayError(String message) {
        System.out.println("Error: " + message);
    }
}
