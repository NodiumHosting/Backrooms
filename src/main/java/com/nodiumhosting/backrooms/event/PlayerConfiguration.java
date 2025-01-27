package com.nodiumhosting.backrooms.event;

import com.nodiumhosting.backrooms.Backrooms;
import com.nodiumhosting.backrooms.resourcepack.ServerResourcePack;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;

import java.util.UUID;

public class PlayerConfiguration {
    public static void handle(AsyncPlayerConfigurationEvent event) {
        final Player player = event.getPlayer();
        event.setSpawningInstance(Backrooms.getInstanceContainer());
        player.setRespawnPoint(new Pos(0, 5, 0));
        player.setGameMode(net.minestom.server.entity.GameMode.ADVENTURE);

        player.sendResourcePacks(
                ResourcePackRequest.resourcePackRequest()
                        .required(true)
                        .packs(ServerResourcePack.resourcePackInfo)
        );

        //DEBUG
        if (player.getUuid().equals(UUID.fromString("951f9d92-8737-4d99-984f-7230160d8bb0"))) {
            player.setPermissionLevel(4);
        }
    }
}
