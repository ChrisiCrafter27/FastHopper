package de.chrisicrafter.fasthopper.client;


import de.chrisicrafter.fasthopper.data.HopperData;

public class ClientDebugScreenData {
    private static HopperData data;

    public static void set(HopperData data) {
        ClientDebugScreenData.data = data;
    }

    public static HopperData getHopperData() {
        return data;
    }
}
