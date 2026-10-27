# Credit Simulator

A **Java 21**-based Console (CLI) application to simulate monthly vehicle loan installments (*Car* and *Motorcycle*) with an annual progressive interest rate scheme.

---

## Prerequisites

- **Java JDK 21** (or at least JDK 17+)
- **Apache Maven 3.8+**
- **Bash Shell** (Linux / macOS / Git Bash / WSL on Windows)

---

## Building & Running Unit Tests

1. Navigate to the project directory:
   ```bash
   cd credit-simulator
   ```

2. Run **Unit Tests** (JUnit 5):
   ```bash
   mvn test
   ```

3. Build the executable JAR:
   ```bash
   mvn clean package
   ```

---

## Running the Application (Step-by-Step)

Ensure the `credit_simulator` script has executable permissions:
```bash
chmod +x bin/credit_simulator
```
*(Note: If the JAR file has not been built yet, the `credit_simulator` script will automatically run `mvn clean package` first).*

### 1. Interactive CLI Mode
Run the following command:
```bash
bin/credit_simulator
```
Select a menu option:
- **`1`**: Enter a new simulation step-by-step (Vehicle Type, Condition, Year, Total Loan Amount, Tenor, and Down Payment).
- **`2`**: Load calculations from the web service `https://run.mocky.io/v3/9108b1da-beec-409e-ae14-e8091955666c` and automatically display the calculation results.
- **`3`**: Exit the application.

### 2. Batch File Input Mode (`file_inputs.txt`)
Run the command by passing the text file path:
```bash
bin/credit_simulator file_inputs.txt
```

Format of `file_inputs.txt` (6 consecutive lines):
```text
Mobil
Bekas
2022
100000000
3
25000000
```

Sample Output:
```text
tahun 1 : Rp. 2,250,000.00/bln , Suku Bunga : 8%
tahun 2 : Rp. 2,432,250.00/bln , Suku Bunga : 8,1%
tahun 3 : Rp. 2,641,423.50/bln , Suku Bunga : 8,6%
```
