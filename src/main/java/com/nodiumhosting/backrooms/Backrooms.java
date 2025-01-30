package com.nodiumhosting.backrooms;

import com.nodiumhosting.backrooms.event.PlayerConfiguration;
import com.nodiumhosting.backrooms.event.PlayerPreLogin;
import com.nodiumhosting.backrooms.event.PlayerSpawn;
import com.nodiumhosting.backrooms.event.ServerListPing;
import com.nodiumhosting.backrooms.level.Levels;
import com.nodiumhosting.backrooms.level.generator.FlatGenerator;
import com.nodiumhosting.backrooms.resourcepack.ServerResourcePack;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Backrooms {
    public static final Logger LOGGER = LoggerFactory.getLogger(Backrooms.class);
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

        instanceContainer = instanceManager.createInstanceContainer(DimensionType.OVERWORLD);
        instanceContainer.setGenerator(new FlatGenerator());
        instanceContainer.setChunkSupplier(LightingChunk::new);

        Levels.init();

        registerEvents();
        registerCommands();

        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);

        InstanceContainer icl0 = Levels.LEVEL0.getLevel().instanceContainer;
        Entity entity = new Entity(EntityType.ENDERMAN);
        entity.setInstance(icl0, Pos.ZERO);
    }

    private static void registerEvents() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, PlayerConfiguration::handle);
        globalEventHandler.addListener(ServerListPingEvent.class, ServerListPing::handle);
        globalEventHandler.addListener(AsyncPlayerPreLoginEvent.class, PlayerPreLogin::handle);
        globalEventHandler.addListener(PlayerSpawnEvent.class, PlayerSpawn::handle);
    }

    private static void registerCommands() {
        MinecraftServer.getCommandManager().register(
                new GameModeCommand(),
                new LevelCommand()
        );
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

    static class LevelCommand extends Command {
        public LevelCommand() {
            super("level", "lvl");

            var levelArg = ArgumentType.Enum("level", Levels.class);
            var intArg = ArgumentType.Integer("level");
            var rootArg = ArgumentType.Literal("root");

            setDefaultExecutor((sender, context) -> {
                sender.sendMessage("Usage: /level <level>");
            });

            setCondition((sender, commandString) -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("You must be a player to use this command!");
                    return false;
                }
                return player.getPermissionLevel() >= 4;
            });

            addSyntax((sender, context) -> {
                final Levels level = context.get(levelArg);
                final Player player = (Player) sender;
                InstanceContainer instanceContainer = level.getLevel().instanceContainer;
                if (player.getInstance().equals(instanceContainer)) return;
                player.setInstance(instanceContainer, Pos.ZERO);
            }, levelArg);

            addSyntax((sender, context) -> {
                final int lvl = context.get(intArg);
                final Player player = (Player) sender;
                Levels[] levels = Levels.values();
                int len = levels.length;
                if (lvl >= len) return;
                @Nullable Levels level = Levels.values()[lvl];
                if (level == null) return;
                InstanceContainer instanceContainer = level.getLevel().instanceContainer;
                if (player.getInstance().equals(instanceContainer)) return;
                player.setInstance(instanceContainer, Pos.ZERO);
            }, intArg);

            addSyntax((sender, context) -> {
                Player player = (Player) sender;
                if (player.getInstance().equals(instanceContainer)) return;
                player.setInstance(instanceContainer, Pos.ZERO);
            }, rootArg);
        }
    }
}