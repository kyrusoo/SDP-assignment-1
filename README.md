# Assignment 1: Builder Pattern — Smart Home System

A Java 17+ implementation of a Smart Lock configuration module, demonstrating the
**Builder Design Pattern** applied to a device with required fields, optional
fields with defaults, and cross-field validation rules.

## Features
* Immutable `SmartLock` product class (private final fields, no setters).
* Fluent `SmartLockBuilder` with domain-oriented configuration methods.
* Single-field and cross-field validation, enforced at `build()` time.
* `SmartLockDirector` providing standard presets (`SAFE`, Basic BLE, Remote WiFi).
* JUnit 5 test suite covering valid, invalid, boundary, and reuse scenarios.

## Prerequisites
* JDK 17 or later
* No build tool required — plain `javac`/`java`

## Build & Run
```bash
# Compile main sources
javac -d out $(find src/main/java/com/smarthome -name "*.java")

# Run the demo client
java -cp out com.smarthome.Main
```

## Usage Example
```java
SmartLock customLock = new SmartLockBuilder(
        "LOCK-002", "CloudLock Ultra", ProtocolType.WIFI,
        new LockCredentials("0000", "1111"))
    .withBatteryLevel(85)
    .enableAutoLock(30)
    .enableRemoteUnlocking()
    .runningFirmware("v1.0.0")
    .build();
```

## Project Structure
```
src/main/java/com/smarthome/
├── SmartLock.java            — Product (immutable domain object)
├── SmartLockBuilder.java     — Builder (fluent API + validation)
├── SmartLockDirector.java    — Director (preset configurations)
├── LockCredentials.java      — Value Object
├── ProtocolType.java         — Enum (BLE, WIFI, Z_WAVE, ZIGBEE)
└── Main.java                 — Client / demo entry point

test/java/com/smarthome/
└── SmartLockBuilderTest.java — JUnit 5 test suite
```

## Testing
The JUnit 5 suite in `test/java/com/smarthome/SmartLockBuilderTest.java` covers
10 cases: valid construction, invalid inputs, boundary values, and builder-reuse
independence.

To run it from the command line (with JUnit's console launcher jar on hand):
```bash
javac -d out -cp junit-platform-console-standalone.jar \
    $(find src/main/java/com/smarthome test/java/com/smarthome -name "*.java")

java -jar junit-platform-console-standalone.jar -cp out --scan-classpath
```
Alternatively, run `SmartLockBuilderTest` directly from your IDE.

## Report
Full design rationale is documented in [`report.md`](./report.md).
