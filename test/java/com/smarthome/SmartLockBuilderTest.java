package com.smarthome;

import com.smarthome.builder.SmartLockBuilder;
import com.smarthome.model.LockCredentials;
import com.smarthome.model.ProtocolType;
import com.smarthome.model.SmartLock;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmartLockBuilderTest {

    private final LockCredentials validCreds = new LockCredentials("1234", "5678");

    //3 Valid Construction Scenarios
    @Test
    void testValidBasicLock() {
        SmartLock lock = new SmartLockBuilder("L1", "BasicLock", ProtocolType.BLE, validCreds).build();
        assertNotNull(lock);
        assertEquals("L1", lock.getDeviceId());
    }

    @Test
    void testValidRemoteWifiLock() {
        SmartLock lock = new SmartLockBuilder("L2", "WifiLock", ProtocolType.WIFI, validCreds)
                .enableRemoteUnlocking()
                .withBatteryLevel(50)
                .build();
        assertTrue(lock.isRemoteUnlocking());
    }

    @Test
    void testValidBiometricLock() {
        SmartLock lock = new SmartLockBuilder("L3", "BioLock", ProtocolType.ZIGBEE, validCreds)
                .enableAutoLock(10)
                .enableBiometricAccess()
                .build();
        assertTrue(lock.isBiometricAccess());
    }

    //3 Invalid Construction Scenarios
    @Test
    void testInvalidEmptyDeviceId() {
        assertThrows(IllegalStateException.class, () ->
                new SmartLockBuilder("", "ModelX", ProtocolType.BLE, validCreds).build()
        );
    }

    @Test
    void testInvalidBatteryLevel() {
        assertThrows(IllegalStateException.class, () ->
                new SmartLockBuilder("L4", "ModelY", ProtocolType.BLE, validCreds)
                        .withBatteryLevel(150)
                        .build()
        );
    }

    @Test
    void testInvalidRemoteUnlockNonWifi() {
        assertThrows(IllegalStateException.class, () ->
                new SmartLockBuilder("L5", "ModelZ", ProtocolType.BLE, validCreds)
                        .enableRemoteUnlocking()
                        .build()
        );
    }

    //2 Boundary Cases
    @Test
    void testBoundaryAutoLockDelayMin() {
        SmartLock lock = new SmartLockBuilder("L6", "ModelA", ProtocolType.BLE, validCreds)
                .enableAutoLock(5)
                .build();
        assertEquals(5, lock.getAutoLockDelaySeconds());

        assertThrows(IllegalStateException.class, () ->
                new SmartLockBuilder("L6", "ModelA", ProtocolType.BLE, validCreds)
                        .enableAutoLock(4)
                        .build()
        );
    }

    @Test
    void testBoundaryBatteryLevelZero() {
        SmartLock lock = new SmartLockBuilder("L7", "ModelB", ProtocolType.BLE, validCreds)
                .withBatteryLevel(0)
                .build();
        assertEquals(0, lock.getBatteryLevel());
    }

    //1 Test for Individual Constraint
    @Test
    void testBiometricRequiresAutoLock() {
        assertThrows(IllegalStateException.class, () ->
                new SmartLockBuilder("L8", "BioLockError", ProtocolType.BLE, validCreds)
                        .enableBiometricAccess() // Fails because auto-lock was not enabled
                        .build()
        );
    }

    //1 Builder Reuse / Product Independence Test
    @Test
    void testBuilderReuseIndependence() {
        SmartLockBuilder builder = new SmartLockBuilder("L9", "MultiBuild", ProtocolType.BLE, validCreds)
                .withBatteryLevel(80);

        SmartLock firstLock = builder.build();

        builder.withBatteryLevel(30);
        SmartLock secondLock = builder.build();

        assertEquals(80, firstLock.getBatteryLevel());
        assertEquals(30, secondLock.getBatteryLevel());
    }
}