# Stage 1: Build menggunakan Maven & JDK 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

# Stage 2: Runtime ringan menggunakan JRE 21 Alpine
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install bash agar script ./credit_simulator dan bin/credit_simulator bisa berjalan
RUN apk add --no-cache bash

# Copy hasil build JAR dan script dari project
COPY --from=builder /build/target/credit-simulator.jar ./target/credit-simulator.jar
COPY credit_simulator ./credit_simulator
COPY bin/credit_simulator ./bin/credit_simulator
COPY file_inputs.txt ./file_inputs.txt

# Bersihkan CRLF (jika ada) dan berikan permission eksekusi
RUN sed -i 's/\r$//' credit_simulator bin/credit_simulator file_inputs.txt && \
    chmod +x credit_simulator bin/credit_simulator

ENTRYPOINT ["./credit_simulator"]