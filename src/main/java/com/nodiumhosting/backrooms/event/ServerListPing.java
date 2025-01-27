package com.nodiumhosting.backrooms.event;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.ResponseData;

public class ServerListPing {
    public static void handle(ServerListPingEvent event) {
        ResponseData responseData = new ResponseData();
        responseData.setDescription(Component.text("Backrooms Server"));
        responseData.setPlayersHidden(true);
        event.setResponseData(responseData);
    }
}
