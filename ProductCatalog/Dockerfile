# ==========================================
# Stage 1: Build the Application JAR
# ==========================================
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Copy parent pom.xml and service pom.xml
COPY pom.xml .
COPY ProductCatalog/pom.xml ./ProductCatalog/

# Download dependencies
RUN cd ProductCatalog && mvn dependency:go-offline -B

# Copy source code
COPY ProductCatalog/src ./ProductCatalog/src

# Build JAR
RUN cd ProductCatalog && mvn clean package -DskipTests

# ==========================================
# Stage 2: Create Lightweight Runtime Image
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR
COPY --from=builder /build/ProductCatalog/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
