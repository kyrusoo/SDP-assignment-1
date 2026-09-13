package com.smarthome.builder;

import com.smarthome.model.LockCredentials;
import com.smarthome.model.ProtocolType;
import com.smarthome.model.SmartLock;

public class SmartLockBuilder {
    // Required fields
    private final String deviceId;
    private final String modelName;
    private final ProtocolType protocol;
    private final LockCredentials credentials;

    // Optional fields with sensible defaults
    private int batteryLevel = 100;
    private boolean autoLockEnabled = false;
    private int autoLockDelaySeconds = 30;
    private boolean biometricAccess = false;
    private boolean remoteUnlocking = false;
    private String firmwareVersion = "v1.0.0";

    public SmartLockBuilder(String deviceId, String modelName, ProtocolType protocol, LockCredentials credentials) {
        this.deviceId = deviceId;
        this.modelName = modelName;
        this.protocol = protocol;
        this.credentials = credentials;
    }

    // Fluent API Methods (Domain-Oriented Naming)
    public SmartLockBuilder withBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
        return this;
    }

    public SmartLockBuilder enableAutoLock(int delaySeconds) {
        this.autoLockEnabled = true;
        this.autoLockDelaySeconds = delaySeconds;
        return this;
    }

    public SmartLockBuilder enableBiometricAccess() {
        this.biometricAccess = true;
        return this;
    }

    public SmartLockBuilder enableRemoteUnlocking() {
        this.remoteUnlocking = true;
        return this;
    }

    public SmartLockBuilder runningFirmware(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
        return this;
    }

    public SmartLock build() {
        return new SmartLock(
                deviceId, modelName, protocol, credentials,
                batteryLevel, autoLockEnabled, autoLockDelaySeconds,
                biometricAccess, remoteUnlocking, firmwareVersion
        );
    }

    private void validateSingleFields() {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalStateException("Device ID cannot be null or empty.");
        }
        if (batteryLevel < 0 || batteryLevel > 100) {
            throw new IllegalStateException("Battery level must be between 0 and 100. Provided: " + batteryLevel);
        }
        if (autoLockDelaySeconds < 5 || autoLockDelaySeconds > 600) {
            throw new IllegalStateException("Auto-lock delay must be between 5 and 600 seconds. Provided: " + autoLockDelaySeconds);
        }
    }

    private void validateCrossField() {
        //constraint 1: remote unlocking requires Wi-Fi protocol and battery >= 20%
        if (remoteUnlocking) {
            if (protocol != ProtocolType.WIFI) {
                throw new IllegalStateException("Remote unlocking requires Wi-Fi protocol connection.");
            }
            if (batteryLevel < 20) {
                throw new IllegalStateException("Remote unlocking requires at least 20% battery level.");
            }
        }
        //constraint 2: biometric access strictly requires auto-lock to be enabled
        if (biometricAccess && !autoLockEnabled) {
            throw new IllegalStateException("High-security biometric locks require auto-lock to be enabled.");
        }
    }
}