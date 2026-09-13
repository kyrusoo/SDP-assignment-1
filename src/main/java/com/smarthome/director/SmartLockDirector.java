package com.smarthome.director;

import com.smarthome.model.LockCredentials;
import com.smarthome.model.ProtocolType;
import com.smarthome.model.SmartLock;
import com.smarthome.builder.SmartLockBuilder;

import java.util.concurrent.locks.Lock;

public class SmartLockDirector {
    public SmartLock buildBasicBLELock(String id, String model) {
        return new SmartLockBuilder(id, model, ProtocolType.BLE, new LockCredentials("1234", "9999"))
                .build();
    }
    //SAFE preset
    public SmartLock buildSafePresetLock(String id, String model) {
        return new SmartLockBuilder(id, model, ProtocolType.Z_WAVE, new LockCredentials("8765", "4321"))
                .enableAutoLock(15)
                .enableBiometricAccess()
                .withBatteryLevel(100)
                .runningFirmware("v2.1.0-secure")
                .build();
    }

    public SmartLock buildRemoteWifiLock(String id, String model) {
        return new SmartLockBuilder(id, model, ProtocolType.WIFI, new LockCredentials("0000", "1111"))
                .enableRemoteUnlocking()
                .enableAutoLock(30)
                .withBatteryLevel(85)
                .build();
    }
}
