# Assignment 1: Builder Pattern - Smart Home System Report

**Course:** Software Design Patterns  
**Instructor:** PhD Makpal Zhartybayeva

---

## 1. Domain & Individual Variant
* **Domain:** Smart Home System (Smart Locks)
* **Individual Variant Constraints:**
    1. `remoteUnlocking == true` requires `ProtocolType.WIFI` and `batteryLevel >= 20`.
    2. `biometricAccess == true` strictly requires `autoLockEnabled == true`.
* **Required Preset:** `SAFE` (High-security configuration with biometric access, auto-lock enabled, and strict credential verification).

---

## 2. Part A: Initial Constructor Design Problems
Initial implementation used a long constructor:
```java
SmartLock lock = new SmartLock("L-101", "Guard", ProtocolType.BLE, creds, 100, true, 30, true, false, "v1.0");