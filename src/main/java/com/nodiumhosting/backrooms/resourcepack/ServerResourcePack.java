package com.nodiumhosting.backrooms.resourcepack;

import com.nodiumhosting.backrooms.Backrooms;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.resource.ResourcePackInfo;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;
import team.unnamed.creative.server.ResourcePackServer;
import team.unnamed.creative.sound.Sound;
import team.unnamed.creative.sound.SoundEntry;
import team.unnamed.creative.sound.SoundEvent;
import team.unnamed.creative.texture.Texture;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class ServerResourcePack {
    public static ResourcePackInfo resourcePackInfo;

    public static void init() throws IOException {
        ClassLoader classLoader = Backrooms.class.getClassLoader();
        String CARPET = "carpet.png";
        String CEILING = "ceiling.png";
        String WALLPAPER = "wallpaper.png";
        String LAMP = "lamp.png";
        String AMBIENCE = "ambience.ogg";
        String EMPTY = "empty.png";
        Writable CARPET_WRITABLE = Writable.resource(classLoader, CARPET);
        Writable CEILING_WRITABLE = Writable.resource(classLoader, CEILING);
        Writable WALLPAPER_WRITABLE = Writable.resource(classLoader, WALLPAPER);
        Writable LAMP_WRITABLE = Writable.resource(classLoader, LAMP);
        Writable AMBIENCE_WRITABLE = Writable.resource(classLoader, AMBIENCE);
        Writable EMPTY_WRITABLE = Writable.resource(classLoader, EMPTY);

        Texture carpet = Texture.texture()
                .key(Key.key("minecraft", "block/yellow_concrete.png"))
                .data(CARPET_WRITABLE)
                .build();

        Texture ceiling = Texture.texture()
                .key(Key.key("minecraft", "block/polished_diorite.png"))
                .data(CEILING_WRITABLE)
                .build();

        Texture wallpaper = Texture.texture()
                .key(Key.key("minecraft", "block/blue_concrete.png"))
                .data(WALLPAPER_WRITABLE)
                .build();

        Texture lamp = Texture.texture()
                .key(Key.key("minecraft", "block/diorite.png"))
                .data(LAMP_WRITABLE)
                .build();

        Sound sound = Sound.sound(
                Key.key("backrooms", "ambience"),
                AMBIENCE_WRITABLE
        );

        SoundEntry soundEntry = SoundEntry.soundEntry()
                .type(SoundEntry.Type.FILE) // <-- Specify type to FILE
                .key(Key.key("backrooms", "ambience")) // <-- set the key of a Sound
                .volume(1.0F)
                .pitch(1.0F)
                .weight(1)
                .stream(false)
                .attenuationDistance(16)
                .preload(false)
                .build();

        SoundEvent soundEvent = SoundEvent.soundEvent()
                .key(Key.key("backrooms", "event.ambience"))
                .sounds(
                        soundEntry
                )
                .replace(false)
                .build();

        Texture hotbar = Texture.texture()
                .key(Key.key("minecraft", "gui/sprites/hud/hotbar.png"))
                .data(EMPTY_WRITABLE)
                .build();

        Texture hotbarSelection = Texture.texture()
                .key(Key.key("minecraft", "gui/sprites/hud/hotbar_selection.png"))
                .data(EMPTY_WRITABLE)
                .build();

        Texture expBarEmpty = Texture.texture()
                .key(Key.key("minecraft", "gui/sprites/hud/experience_bar_background.png"))
                .data(EMPTY_WRITABLE)
                .build();

        Texture expBarFull = Texture.texture()
                .key(Key.key("minecraft", "gui/sprites/hud/experience_bar_progress.png"))
                .data(EMPTY_WRITABLE)
                .build();

        Texture food = Texture.texture()
                .key(Key.key("minecraft", "gui/sprites/hud/food_full.png"))
                .data(EMPTY_WRITABLE)
                .build();

        Texture foodEmpty = Texture.texture()
                .key(Key.key("minecraft", "gui/sprites/hud/food_empty.png"))
                .data(EMPTY_WRITABLE)
                .build();

        Texture heart = Texture.texture()
                .key(Key.key("minecraft", "gui/sprites/hud/heart/full.png"))
                .data(EMPTY_WRITABLE)
                .build();

        Texture heartEmpty = Texture.texture()
                .key(Key.key("minecraft", "gui/sprites/hud/heart/container.png"))
                .data(EMPTY_WRITABLE)
                .build();

        Texture inventory = Texture.texture()
                .key(Key.key("minecraft", "gui/container/inventory.png"))
                .data(EMPTY_WRITABLE)
                .build();

        Texture recipeBookButton = Texture.texture()
                .key(Key.key("minecraft", "gui/sprites/recipe_book/button.png"))
                .data(EMPTY_WRITABLE)
                .build();

        Texture recipeBookButtonHighlighted = Texture.texture()
                .key(Key.key("minecraft", "gui/sprites/recipe_book/button_highlighted.png"))
                .data(EMPTY_WRITABLE)
                .build();

        ResourcePack resourcePack = ResourcePack.resourcePack();
        resourcePack.packMeta(42, "Backrooms Resources");
        resourcePack.texture(carpet);
        resourcePack.texture(ceiling);
        resourcePack.texture(wallpaper);
        resourcePack.texture(lamp);
        resourcePack.sound(sound);
        resourcePack.soundEvent(soundEvent);
//        resourcePack.texture(hotbar);
//        resourcePack.texture(hotbarSelection);
//        resourcePack.texture(expBarEmpty);
//        resourcePack.texture(expBarFull);
//        resourcePack.texture(food);
//        resourcePack.texture(foodEmpty);
//        resourcePack.texture(heart);
//        resourcePack.texture(heartEmpty);
//        resourcePack.texture(inventory);
//        resourcePack.texture(recipeBookButton);
//        resourcePack.texture(recipeBookButtonHighlighted);

        BuiltResourcePack builtResourcePack = MinecraftResourcePackWriter.minecraft().build(resourcePack);

        ResourcePackServer rpServer = ResourcePackServer.server()
                .address("127.0.0.1", 7270)
                .pack(builtResourcePack)
                .build();

        rpServer.start();
        Backrooms.LOGGER.info("Resource pack server started at " + rpServer.address());

        resourcePackInfo = ResourcePackInfo.resourcePackInfo(UUID.fromString("cb53c527-9011-479d-8984-81d4968f95ca"), URI.create("http:/" + rpServer.address()), builtResourcePack.hash());
    }
}
