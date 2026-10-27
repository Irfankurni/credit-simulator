package com.simulator;

import com.simulator.controller.LoanController;
import com.simulator.service.ApiService;
import com.simulator.service.LoanCalculationService;
import com.simulator.service.LoanValidationService;
import com.simulator.view.ConsoleView;

public class MainApplication {

    public static void main(String[] args) {
        LoanValidationService validationService = new LoanValidationService();
        LoanCalculationService calculationService = new LoanCalculationService();
        ApiService apiService = new ApiService(validationService);
        ConsoleView view = new ConsoleView();

        LoanController controller = new LoanController(validationService, calculationService, apiService, view);
        if (args.length > 0) {
            controller.runBatchFileMode(args[0]);
        } else {
            controller.runInteractiveMode();
        }

    }
}
