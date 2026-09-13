# Assignment 1 Report: Builder Pattern Implementation

**Course:** Software Design Patterns
**Topic:** Builder Pattern: Design Under Changing Requirements
**Domain:** Smart Home System (Smart Locks)
**Programming Language:** Java 17+

---

## 1. Problem Description & Individual Variant

### Domain Overview
The selected domain for this project is a **Smart Home System (Smart Locks)**. Modern smart buildings and home automation ecosystems depend on secure, hardware-configured access control devices. Creating a `SmartLock` instance requires configuring core device identifiers, communication protocols, physical battery constraints, auto-lock security timers, and biometric or remote access flags.

### Variant Specification

* **Domain:** Smart Home System (Smart Locks)
* **Required Properties (4):**
  1. `deviceId` (*String*) — Unique hardware identifier (e.g., `"LOCK-001"`).
  2. `modelName` (*String*) — Commercial device model name.
  3. `protocol` (*ProtocolType*) — Radio communication protocol (`BLE`, `WIFI`, `Z_WAVE`, `ZIGBEE`).
  4. `credentials` (*LockCredentials*) — Value object encapsulating PIN codes and backup security keys.
* **Optional Properties (6):**
  1. `batteryLevel` (*int*) — Remaining battery percentage (default: `100`).
  2. `autoLockEnabled` (*boolean*) — Flag indicating whether the lock automatically re-engages (default: `false`).
  3. `autoLockDelaySeconds` (*int*) — Delay before auto-locking in seconds (default: `30`).
  4. `biometricAccess` (*boolean*) — Flag for fingerprint/facial recognition capability (default: `false`).
  5. `remoteUnlocking` (*boolean*) — Flag for over-the-air remote unlock functionality (default: `false`).
  6. `firmwareVersion` (*String*) — Installed device firmware version (default: `"v1.0.0"`).
* **Individual Constraints (Cross-Field Rules):**
  * **Constraint 1:** Remote Unlocking (`remoteUnlocking = true`) strictly requires the `ProtocolType.WIFI` communication protocol and a battery level of at least 20% (`batteryLevel >= 20`).
  * **Constraint 2:** High-security biometric access (`biometricAccess = true`) strictly requires auto-lock to be active (`autoLockEnabled = true`).

---

## 2. Part A — Initial Constructor-Based Solution & Design Problems

### Initial Constructor Implementation
Before refactoring to the Builder pattern, the `SmartLock` class relied on a multi-parameter (telescoping) constructor:

```java
// Anti-pattern: Overloaded telescoping constructor
public SmartLock(String deviceId, String modelName, ProtocolType protocol, LockCredentials credentials,
                 int batteryLevel, boolean autoLockEnabled, int autoLockDelaySeconds,
                 boolean biometricAccess, boolean remoteUnlocking, String firmwareVersion) {
    this.deviceId = deviceId;
    this.modelName = modelName;
    this.protocol = protocol;
    this.credentials = credentials;
    this.batteryLevel = batteryLevel;
    this.autoLockEnabled = autoLockEnabled;
    this.autoLockDelaySeconds = autoLockDelaySeconds;
    this.biometricAccess = biometricAccess;
    this.remoteUnlocking = remoteUnlocking;
    this.firmwareVersion = firmwareVersion;
}
```

**Client Instantiation Call:**

```java
SmartLock lock = new SmartLock(
    "LOCK-101", "SecureGuard Pro", ProtocolType.WIFI, new LockCredentials("1234", "5678"),
    85, true, 30, true, true, "v2.1.0"
);
```

### Identified Concrete Design Problems

1. **Telescoping Constructor / Poor Readability:** Passing 10 arguments in a single constructor call obfuscates field intent. Without inspecting the underlying class signature, a developer cannot determine what `85`, `30`, or the boolean sequence `true, true, true` represent.
2. **High Risk of Parameter Confusion (Positional Instability):** The constructor accepts multiple primitive parameters of identical types sequentially (e.g., two `int` parameters for battery percentage and delay seconds, followed by three contiguous boolean flags). Accidentally swapping delay seconds with battery percentage or flipping boolean flags leads to silent runtime defects that pass compilation.
3. **Inflexible Object Creation & Dummy Argument Dependency:** Forcing callers to populate optional fields requires passing dummy or default values explicitly (`null`, `0`, `false`) when standard defaults would suffice.

---

## 3. Part B — Refactor to Builder Pattern

### Architecture Overview
The system was refactored using a standalone `SmartLockBuilder` class to separate construction logic from the product representation.

* **Product (`SmartLock`):** An immutable domain class with private final fields, package-private constructor, and read-only getters.
* **Builder (`SmartLockBuilder`):** Handles step-by-step assembly, fluent configuration methods, default field initialization, and invariant verification.
* **Director (`SmartLockDirector`):** Encapsulates preset configurations (SAFE, Basic BLE, Remote WiFi).
* **Value Object (`LockCredentials`):** Immutable value object storing `masterPin` and `backupKeycode`.
* **Client (`Main`):** Demonstrates preset assembly and output formatting.

### Fluent API & Construction Example

```java
SmartLock customLock = new SmartLockBuilder("LOCK-002", "CloudLock Ultra", ProtocolType.WIFI, new LockCredentials("0000", "1111"))
        .withBatteryLevel(85)
        .enableAutoLock(30)
        .enableRemoteUnlocking()
        .runningFirmware("v1.0.0")
        .build();
```

---

## 4. Part C — Validation Strategy

To prevent invalid or unsafe hardware states, all single-field and cross-field invariant checks are executed inside `build()` before instantiating `SmartLock`.

### Implemented Validation Rules

**Single-Field Rules (3):**
1. **Device ID Check:** `deviceId` cannot be null, empty, or blank.
2. **Battery Range Bounds:** `batteryLevel` must fall inclusively between 0 and 100.
3. **Auto-Lock Delay Bounds:** `autoLockDelaySeconds` must fall inclusively between 5 and 600 seconds.

**Cross-Field Rules (2):**
1. **Remote Unlock Dependencies:** If `remoteUnlocking` is `true`, `protocol` must equal `ProtocolType.WIFI` AND `batteryLevel` must be `>= 20`.
2. **Biometric Security Dependency:** If `biometricAccess` is `true`, `autoLockEnabled` must equal `true`.

### Rationalization: Why Validation Belongs in the Builder
Executing validation inside `SmartLockBuilder.build()` before calling the private product constructor guarantees that an invalid object is never instantiated in memory. If validation fails, an `IllegalStateException` is thrown immediately, keeping the `SmartLock` product class strictly immutable and guaranteed valid upon creation.

---

## 5. Part D — Preset Configurations

The `SmartLockDirector` manages three standardized setup presets:

**`buildSafePresetLock()` (Required SAFE Preset):**
* Protocol: `ProtocolType.Z_WAVE`
* Auto-Lock: `true` (Delay: 15 seconds)
* Biometric Access: `true`
* Battery Level: 100%
* Firmware: `"v2.1.0-secure"`

**`buildBasicBLELock()`:**
* Protocol: `ProtocolType.BLE`
* Battery Level: 100% (Default)
* Auto-Lock: `false`

**`buildRemoteWifiLock()`:**
* Protocol: `ProtocolType.WIFI`
* Remote Unlocking: `true`
* Auto-Lock: `true` (Delay: 30 seconds)
* Battery Level: 85%

---

## 6. Part E — Clean Code Refactoring (Chapter 3)

### Example 1: Eliminating Flag Arguments (Domain-Oriented Methods)

**Before:**
```java
public SmartLockBuilder setBiometric(boolean enabled) {
    this.biometricAccess = enabled;
    return this;
}
```

**After:**
```java
public SmartLockBuilder enableBiometricAccess() {
    this.biometricAccess = true;
    return this;
}
```

**Clean Code Principle:** Avoid Flag Arguments & Prefer Domain-Oriented Names.

**Explanation:** Passing boolean flags (`true`/`false`) forces the developer to inspect implementation details to deduce function behavior. Expressive zero-argument methods like `enableBiometricAccess()` make the fluent invocation self-documenting.

### Example 2: Extracting Single Responsibility Helper Functions

**Before:**
```java
public SmartLock build() {
    if (deviceId == null || deviceId.isBlank()) throw new IllegalStateException(...);
    if (batteryLevel < 0 || batteryLevel > 100) throw new IllegalStateException(...);
    if (remoteUnlocking && (protocol != ProtocolType.WIFI || batteryLevel < 20)) throw new IllegalStateException(...);
    // Monolithic inline validation blocks...
    return new SmartLock(...);
}
```

**After:**
```java
public SmartLock build() {
    validateSingleFields();
    validateCrossFields();
    return new SmartLock(...);
}
```

**Clean Code Principle:** Do One Thing & Maintain One Level of Abstraction.

**Explanation:** Decomposing monolithic checks into `validateSingleFields()` and `validateCrossFields()` isolates validation categories, reduces function complexity, and improves maintainability.

---

## 7. Part F — Architectural Design Decision

**Selected Decision:** Enforcing complete immutability on `SmartLock` (all fields private final, no setter methods) and centralizing validation inside `SmartLockBuilder.build()`.

**Alternative Considered:** Defining a mutable `SmartLock` object with public setters alongside a separate builder.

**Reasoning:**
* **Thread Safety:** Immutable lock states can be shared safely across concurrent threads without requiring explicit synchronization mechanisms.
* **Encapsulation:** Preventing post-instantiation modification ensures that once a `SmartLock` is validated and constructed, its operational state cannot be corrupted later during application lifecycle.
* **Prevention of Invalid Transient States:** Mutability allows an object to exist in a partially modified, invalid state between setter calls. Immutability with builder validation guarantees all-or-nothing construction.

---

## 8. Part G — UML Diagram Traceability Table

| Class / Interface | Role | Architectural Description |
|---|---|---|
| `SmartLock` | Product | Immutable domain object representing lock configuration. |
| `SmartLockBuilder` | Builder | Constructs `SmartLock` instances, manages defaults, and validates invariants. |
| `SmartLockDirector` | Director | Encapsulates standard configuration presets (SAFE, Basic BLE, Remote WiFi). |
| `LockCredentials` | Value Object | Immutable holder for security credential pairs. |
| `ProtocolType` | Enum | Enumeration defining hardware connectivity protocols. |
| `Main` | Client | Application entry point executing configuration demonstrations. |

---

## 9. Part H — Automated Testing Summary

The project includes a 10-case JUnit 5 unit test suite (`SmartLockBuilderTest.java`) verifying positive paths, negative invariant rejections, boundary limits, and builder instance reuse:

**Valid Scenarios (3):**
1. `testValidBasicLock()` — Verifies successful default creation for a basic BLE lock.
2. `testValidRemoteWifiLock()` — Confirms valid construction of a WiFi remote lock with 50% battery.
3. `testValidBiometricLock()` — Verifies biometric activation when auto-lock is explicitly enabled.

**Invalid Scenarios (3):**
4. `testInvalidEmptyDeviceId()` — Asserts that an empty device ID throws `IllegalStateException`.
5. `testInvalidBatteryLevel()` — Asserts that out-of-bounds battery values (150%) are rejected.
6. `testInvalidRemoteUnlockNonWifi()` — Asserts that enabling remote unlock on a non-WiFi protocol (BLE) throws `IllegalStateException`.

**Boundary Cases (2):**
7. `testBoundaryAutoLockDelayMin()` — Verifies lower bound auto-lock delay (5s succeeds, 4s fails).
8. `testBoundaryBatteryLevelZero()` — Verifies valid construction at the absolute minimum battery boundary (0%).

**Variant Constraint & Reuse Cases (2):**
9. `testBiometricRequiresAutoLock()` — Asserts that enabling biometric access without auto-lock throws `IllegalStateException`.
10. `testBuilderReuseIndependence()` — Ensures sequential calls to `build()` on a mutated builder yield independent `SmartLock` instances.

---

## 10. Sample Program Output

Executing `Main.java` produces the following console output:

```
Built Safe Preset: SmartLock{id='LOCK-001', model='SecureGuard Pro', protocol=Z_WAVE, credentials=[Master PIN: ****, Backup Key: ****], battery=100%, autoLock=true, delay=15s, biometric=true, remoteUnlock=false, firmware='v2.1.0-secure'}
🍌 Successfully constructed Smart Lock: SmartLock{id='LOCK-002', model='CloudLock Ultra', protocol=WIFI, credentials=[Master PIN: ****, Backup Key: ****], battery=85%, autoLock=true, delay=30s, biometric=false, remoteUnlock=true, firmware='v1.0.0'}
```

---

## 11. Repository Information

**GitHub Repository:** https://github.com/kyrusoo/SDP-assignment-1
