package com.nodiumhosting.backrooms.level.generator;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.biome.Biome;
import net.minestom.server.world.biome.BiomeEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level0Generator implements Generator {
    private static final int ROOM_MIN_SIZE = 8;
    private static final int ROOM_MAX_SIZE = 16;
    private static final int ROOM_HEIGHT = 4;
    private static final DynamicRegistry.Key<Biome> biomeTypeKey = MinecraftServer.getBiomeRegistry().register("level0", Biome.builder().effects(BiomeEffects.builder().ambientSound(new BiomeEffects.AmbientSound(NamespaceID.from(Key.key("backrooms:event.ambience")))).build()).build());
    private final Random random = new Random();

    @Override
    public void generate(GenerationUnit unit) {
        Point start = unit.absoluteStart();
        Point end = start.add(unit.size());

        unit.modifier().fillBiome(biomeTypeKey);

        unit.modifier().fill(Block.BEDROCK);

        unit.modifier().fillHeight(-1, 0, Block.YELLOW_CONCRETE);
        unit.modifier().fillHeight(0, ROOM_HEIGHT, Block.BLUE_CONCRETE);

        // Generate main path using drunkard's walk
        List<Point> mainPath = generateMainPath(start, end);

        // Place rooms along the path
        List<Room> rooms = placeRoomsAlongPath(mainPath, start, end);

        // Carve everything out
        carveRooms(unit, rooms);
        carveCorridors(unit, mainPath);
    }

    private List<Point> generateMainPath(Point start, Point end) {
        List<Point> path = new ArrayList<>();
        int x = (int) start.x() + (int) (end.x() - start.x()) / 2;
        int z = (int) start.z() + (int) (end.z() - start.z()) / 2;

        path.add(new Pos(x, 0, z));

        int steps = 20;
        while (steps > 0) {
            int direction = random.nextInt(4);
            int stepLength = random.nextInt(8) + 4;

            for (int i = 0; i < stepLength; i++) {
                switch (direction) {
                    case 0:
                        x += 2;
                        break; // East
                    case 1:
                        x -= 2;
                        break; // West
                    case 2:
                        z += 2;
                        break; // South
                    case 3:
                        z -= 2;
                        break; // North
                }

                // Keep within bounds
                x = Math.min(Math.max(x, (int) start.x()), (int) end.x());
                z = Math.min(Math.max(z, (int) start.z()), (int) end.z());

                path.add(new Pos(x, 0, z));
            }
            steps--;
        }

        return path;
    }

    private List<Room> placeRoomsAlongPath(List<Point> path, Point start, Point end) {
        List<Room> rooms = new ArrayList<>();

        for (int i = 0; i < path.size(); i += 5) {
            Point p = path.get(i);
            int width = random.nextInt(ROOM_MAX_SIZE - ROOM_MIN_SIZE) + ROOM_MIN_SIZE;
            int height = random.nextInt(ROOM_MAX_SIZE - ROOM_MIN_SIZE) + ROOM_MIN_SIZE;

            int x = (int) p.x() - width / 2;
            int z = (int) p.z() - height / 2;

            // Ensure room is within bounds
            x = Math.min(Math.max(x, (int) start.x()), (int) end.x() - width);
            z = Math.min(Math.max(z, (int) start.z()), (int) end.z() - height);

            Room room = new Room(x, z, width, height);
            if (!overlapsAny(room, rooms)) {
                rooms.add(room);
            }
        }

        return rooms;
    }

    private boolean overlapsAny(Room newRoom, List<Room> rooms) {
        for (Room room : rooms) {
            if (newRoom.x < room.x + room.width + 2 &&
                    newRoom.x + newRoom.width + 2 > room.x &&
                    newRoom.z < room.z + room.height + 2 &&
                    newRoom.z + newRoom.height + 2 > room.z) {
                return true;
            }
        }
        return false;
    }

    private void carveRooms(GenerationUnit unit, List<Room> rooms) {
        Point start = unit.absoluteStart();
        Point end = start.add(unit.size());

        for (Room room : rooms) {
            for (int x = room.x; x < room.x + room.width; x++) {
                for (int y = 0; y < ROOM_HEIGHT; y++) {
                    for (int z = room.z; z < room.z + room.height; z++) {
                        if (y == ROOM_HEIGHT - 1) {
                            if (random.nextInt(100) < 50 && isInUnit(unit, x - 1, y, z - 1)) {
                                if (x % 4 == 1 && z % 4 == 1) {
                                    if (random.nextInt(100) < 50) {
                                        unit.modifier().setBlock(x, y, z, Block.DIORITE_SLAB.withProperty("type", "top"));
                                        unit.modifier().setBlock(x, y, z - 1, Block.DIORITE_SLAB.withProperty("type", "top"));
                                        continue;
                                    } else {
                                        unit.modifier().setBlock(x, y, z, Block.DIORITE_SLAB.withProperty("type", "top"));
                                        unit.modifier().setBlock(x - 1, y, z, Block.DIORITE_SLAB.withProperty("type", "top"));
                                        continue;
                                    }
                                }
                            }

                            if (isInUnit(unit, x, y, z)) {
                                unit.modifier().setBlock(x, y, z, Block.POLISHED_DIORITE_SLAB.withProperty("type", "top"));
                            }
                        } else {
                            if (isInUnit(unit, x, y, z)) {
                                unit.modifier().setBlock(x, y, z, Block.AIR);
                            }
                        }
                    }
                }
            }
        }
    }

    private void carveCorridors(GenerationUnit unit, List<Point> path) {
        Point start = unit.absoluteStart();
        Point end = start.add(unit.size());

        for (Point p : path) {
            for (int x = (int) p.x() - 1; x <= p.x() + 1; x++) {
                for (int y = 0; y < ROOM_HEIGHT; y++) {
                    for (int z = (int) p.z() - 1; z <= p.z() + 1; z++) {
                        if (y == ROOM_HEIGHT - 1) {
                            if (random.nextInt(100) < 50 && isInUnit(unit, x - 1, y, z - 1)) {
                                if (x % 4 == 1 && z % 4 == 1) {
                                    if (random.nextInt(100) < 50) {
                                        unit.modifier().setBlock(x, y, z, Block.DIORITE_SLAB.withProperty("type", "top"));
                                        unit.modifier().setBlock(x, y, z - 1, Block.DIORITE_SLAB.withProperty("type", "top"));
                                        continue;
                                    } else {
                                        unit.modifier().setBlock(x, y, z, Block.DIORITE_SLAB.withProperty("type", "top"));
                                        unit.modifier().setBlock(x - 1, y, z, Block.DIORITE_SLAB.withProperty("type", "top"));
                                        continue;
                                    }
                                }
                            }

                            if (isInUnit(unit, x, y, z)) {
                                unit.modifier().setBlock(x, y, z, Block.POLISHED_DIORITE_SLAB.withProperty("type", "top"));
                            }
                        } else {
                            if (isInUnit(unit, x, y, z)) {
                                unit.modifier().setBlock(x, y, z, Block.AIR);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isInUnit(GenerationUnit unit, int x, int y, int z) {
        Point start = unit.absoluteStart();
        Point end = start.add(unit.size());
        return x >= start.x() && x < end.x() &&
                y >= start.y() && y < end.y() &&
                z >= start.z() && z < end.z();
    }

    private static class Room {
        final int x;
        final int z;
        final int width;
        final int height;

        Room(int x, int z, int width, int height) {
            this.x = x;
            this.z = z;
            this.width = width;
            this.height = height;
        }
    }
}