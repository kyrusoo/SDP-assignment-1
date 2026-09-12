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
}