package com.smarthome;

import com.smarthome.director.SmartLockDirector;
import com.smarthome.model.SmartLock;

public class Main {
    public static void main(String[] args) {
        SmartLockDirector director = new SmartLockDirector();

        //safe construct
        SmartLock safeLock = director.buildSafePresetLock("LOCK-001", "Safeguard Pro");
        System.out.println("Built safe preset: " + safeLock);

        //remote wifi
        SmartLock remoteLock = director.buildRemoteWifiLock("LOCK-002", "CloudLock Ultra");

        //Console output requirement from part H
        System.out.println("\uD83C\uDF4C Successfully constructed Smart Lock: " + remoteLock);
    }
}