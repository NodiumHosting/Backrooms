package com.nodiumhosting.backrooms;

import com.nodiumhosting.backrooms.generator.BackroomsGenerator;
import com.nodiumhosting.backrooms.resourcepack.ServerResourcePack;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.ping.ResponseData;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.biome.Biome;
import net.minestom.server.world.biome.BiomeEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Backrooms {
    public static final Logger LOGGER = LoggerFactory.getLogger(Backrooms.class);
    public static DynamicRegistry.Key<Biome> biomeTypeKey;
    @Getter
    private static InstanceContainer instanceContainer;
    @Getter
    private static InstanceManager instanceManager;

    public static void main(String[] args) {
        try {
            ServerResourcePack.init();
        } catch (Exception e) {
            LOGGER.error("Failed to load resource pack or start resource pack server", e);
            e.printStackTrace();
        }

        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.setBrandName("backrooms");

        instanceManager = MinecraftServer.getInstanceManager();
        biomeTypeKey = MinecraftServer.getBiomeRegistry().register("backrooms", Biome.builder().effects(BiomeEffects.builder().ambientSound(new BiomeEffects.AmbientSound(NamespaceID.from(Key.key("backrooms:event.ambience")))).build()).build());
        instanceContainer = instanceManager.createInstanceContainer(DimensionTypes.BACKROOMS.getDimensionType());

        MinecraftServer.getCommandManager().register(new GameModeCommand());

        instanceContainer.setGenerator(new BackroomsGenerator());
        instanceContainer.setChunkSupplier(LightingChunk::new);

        // clear out a spawn area around 0 0 0
        int radius = 3;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = 0; y < 6; y++) {
                    instanceContainer.setBlock(x, y, z, Block.AIR);
                }
            }
        }

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
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
            if (player.getUuid().equals(UUID.fromString("951f9d92-8737-4d99-984f-7230160d8bb0"))) {
                player.setPermissionLevel(4);
            }
        });

        globalEventHandler.addListener(ServerListPingEvent.class, event -> {
            ResponseData responseData = new ResponseData();
            responseData.setDescription(Component.text("Backrooms Server"));
            responseData.setPlayersHidden(true);
            event.setResponseData(responseData);
        });

        globalEventHandler.addListener(AsyncPlayerPreLoginEvent.class, event -> {
            event.setGameProfile(new GameProfile(event.getGameProfile().uuid(), "Explorer"));
        });

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            Player player = event.getPlayer();
            player.sendPlayerListHeader(Component.text("Backrooms").style(Style.style(TextDecoration.OBFUSCATED)));
            player.setSkin(PlayerSkin.fromUuid("caa6a7c8-10a6-4ded-9bfd-ab49497bf9bc"));
        });

        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);
    }

    static class GameModeCommand extends Command {
        public GameModeCommand() {
            super("gamemode");

            setDefaultExecutor((sender, context) -> {
                sender.sendMessage("Usage: /gamemode <gamemode>");
            });

            var gamemode = ArgumentType.Enum("gamemode", GameMode.class).setFormat(ArgumentEnum.Format.LOWER_CASED);

            // Callback executed if the argument has been wrongly used
            gamemode.setCallback((sender, exception) -> {
                final String input = exception.getInput();
                sender.sendMessage("Invalid gamemode: " + input);
            });

            setCondition((sender, commandString) -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("You must be a player to use this command!");
                    return false;
                }
                return player.getPermissionLevel() >= 4;
            });

            addSyntax((sender, context) -> {
                final GameMode gameMode = context.get(gamemode);
                final Player player = (Player) sender;
                player.setGameMode(gameMode);
            }, gamemode);
        }
    }
}