package com.nodiumhosting.backrooms.event.player;

import com.nodiumhosting.backrooms.Backrooms;
import com.nodiumhosting.backrooms.resourcepack.ServerResourcePack;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;

public class PlayerConfigurationEvent {
    public static void onPlayerConfiguration(AsyncPlayerConfigurationEvent event) {
        final Player player = event.getPlayer();
        event.setSpawningInstance(Backrooms.getInstanceContainer());
        player.setRespawnPoint(new Pos(0, 0, 0));
        player.setGameMode(net.minestom.server.entity.GameMode.ADVENTURE);

        player.sendResourcePacks(
                ResourcePackRequest.resourcePackRequest()
                        .required(true)
                        .packs(ServerResourcePack.resourcePackInfo)
        );

        //DEBUG
        if (player.getUsername().equals("McMelonTV")) player.setPermissionLevel(4);
    }
}
