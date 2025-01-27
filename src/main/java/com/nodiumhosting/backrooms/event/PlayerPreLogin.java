package com.nodiumhosting.backrooms.event;

import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.network.player.GameProfile;

public class PlayerPreLogin {
    public static void handle(AsyncPlayerPreLoginEvent event) {
        event.setGameProfile(new GameProfile(event.getGameProfile().uuid(), "Explorer"));
    }
}
