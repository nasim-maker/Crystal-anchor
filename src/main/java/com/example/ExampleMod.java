package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ExampleMod implements ClientModInitializer {
    public static final AutoCrystal autoCrystal = new AutoCrystal();
    public static final AutoAnchor autoAnchor = new AutoAnchor();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null) {
                autoCrystal.onTick(client);
                autoAnchor.onTick(client);
            }
        });
    }
}
