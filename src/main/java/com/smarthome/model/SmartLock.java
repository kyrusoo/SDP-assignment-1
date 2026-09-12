package com.smarthome.model;

public class SmartLock {
    private String deviceId;
    private String modelName;
    private ProtocolType protocol;
    private LockCredentials credentials;

    private int batteryLevel;
    private boolean autoLockEnabled;
    private int autoLockDelaySeconds;
    private boolean biometricAccess;
    private boolean remoteUnlocking;
    private String firmwareVersion;

    //Part A: Conventional 10-parameter constructor (anti-pattern)
    public SmartLock(
            String deviceId,
            String modelName,
            ProtocolType protocol,
            LockCredentials credentials,
            int batteryLevel,
            boolean autoLockEnabled,
            int autoLockDelaySeconds,
            boolean biometricAccess,
            boolean remoteUnlocking,
            String firmwareVersion
    ) {
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

    //getters
    public String getDeviceId() { return deviceId; }
    public String getModelName() { return modelName; }
    public ProtocolType getProtocol() { return protocol; }
    public LockCredentials getCredentials() { return credentials; }
    public int getBatteryLevel() { return batteryLevel; }
    public boolean isAutoLockEnabled() { return autoLockEnabled; }
    public int getAutoLockDelaySeconds() { return autoLockDelaySeconds;}
    public boolean isBiometricAccess() { return biometricAccess; }
    public boolean isRemoteUnlocking() { return remoteUnlocking; }
    public String getFirmwareVersion() { return firmwareVersion; }

    @Override
    public String toString() {
        return "SmartLock{" +
                "id='" + deviceId + '\'' +
                ", model='" + modelName + '\'' +
                ", protocol=" + protocol +
                ", credentials=" + credentials +
                ", battery=" + batteryLevel + "%" +
                ", autoLock=" + autoLockEnabled +
                ", delay=" + autoLockDelaySeconds + "s" +
                ", biometric=" + biometricAccess +
                ", remoteUnlock=" + remoteUnlocking +
                ", firmware=" + firmwareVersion + '\'' + '}';
    }
}
