package com.smarthome.model;

import java.util.Objects;

public final class LockCredentials {
    private final String masterPin;
    private final String backupKeycode;

    public LockCredentials(String masterPin, String backupKeycode) {
        this.masterPin = Objects.requireNonNull(masterPin, "Master PIN cannot be null!");
        this.backupKeycode = Objects.requireNonNull(backupKeycode, "Backup keycode cannot be null!");
    }

    public String getMasterPin() { return masterPin; }
    public String getBackupKeycode() { return backupKeycode; }

    @Override
    public String toString() {
        return "[Master PIN: ****, Backup Key: ****]";
    }
}