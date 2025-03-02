
package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class MapGenerator {
    public ArrayList<Room> rooms;
    private int height;
    private int width;
    TETile[][] tiles;
    private long seed;
    Random random;

    private static final int MIN_ROOM_WIDTH = 5;
    private static final int MAX_ROOM_WIDTH = 8;
    private static final int MIN_ROOM_HEIGHT = 5;
    private static final int MAX_ROOM_HEIGHT = 8;
    private static final int MIN_PARTITION_SIZE = 10;


    public MapGenerator(int height, int width, long seed) {
        this.height = height;
        this.width = width;
        this.seed = seed;
        rooms = new ArrayList<>();
        random = new Random(seed);

        tiles = new TETile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        generateRooms();
        connectAllRooms(rooms);
        setAllLight();
    }

    public void generateRooms() {
        Region initialRegion = new Region(0, 0, width, height);
        partition(initialRegion);
    }


    private void partition(Region region) {
        // Base case: If the region is too small, stop partitioning and create a room in the region
        if (region.width < MIN_PARTITION_SIZE || region.height < MIN_PARTITION_SIZE) {
            createRoomInRegion(region);
            return;
        }

        // Randomly decide whether to split horizontally or vertically
        boolean splitHorizontally = random.nextBoolean();

        if (splitHorizontally && region.height >= 2 * MIN_PARTITION_SIZE) {
            // Split the region horizontally
            int possibleY = region.height - (2 * MIN_PARTITION_SIZE);
            int splitY = (possibleY > 0) ? random.nextInt(possibleY) + MIN_PARTITION_SIZE : MIN_PARTITION_SIZE;
            Region topRegion = new Region(region.x, region.y + splitY, region.width, region.height - splitY);
            Region bottomRegion = new Region(region.x, region.y, region.width, splitY);
            partition(topRegion);
            partition(bottomRegion);

        } else if (!splitHorizontally && region.width >= 2 * MIN_PARTITION_SIZE) {
            // Split the region vertically
            int possibleX = region.width - (2 * MIN_PARTITION_SIZE);
            int splitX = (possibleX > 0) ? random.nextInt(possibleX) + MIN_PARTITION_SIZE : MIN_PARTITION_SIZE;
            Region leftRegion = new Region(region.x, region.y, splitX, region.height);
            Region rightRegion = new Region(region.x + splitX, region.y, region.width - splitX, region.height);

            partition(leftRegion);
            partition(rightRegion);
        } else {
            createRoomInRegion(region);
        }
    }


    private void createRoomInRegion(Region region) {
        if (region.width < MIN_ROOM_WIDTH || region.height < MIN_ROOM_HEIGHT) {
            return;
        }
        int room_width = random.nextInt(Math.min(region.width - MIN_ROOM_WIDTH, MAX_ROOM_WIDTH - MIN_ROOM_WIDTH) - 1) + MIN_ROOM_WIDTH;
        int room_height = random.nextInt(Math.min(region.height - MIN_ROOM_HEIGHT, MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT) - 1) + MIN_ROOM_HEIGHT;
        int x = random.nextInt(region.width - room_width) + region.x;
        int y = random.nextInt(region.height - room_height) + region.y;

        Room newRoom = new Room(x, y, room_height, room_width, tiles, seed);
        rooms.add(newRoom);
    }


    private void generateHallways(Room a, Room b) {
        int x1 = a.randomX();
        int y1 = a.randomY();
        int x2 = b.randomX();
        int y2 = b.randomY();

        int xDirection = (x2 > x1) ? 1 : -1;
        int yDirection = (y2 > y1) ? 1 : -1;

        int currentX = x1;
        int currentY = y1;


        while (currentX != x2) {
            drawHallways(currentX, currentY);
            currentX += xDirection;
        }

        // Draw corner tile
        drawHallways(currentX, currentY);

        while (currentY != y2) {
            drawHallways(currentX, currentY);
            currentY += yDirection;
        }
    }

    private void drawHallways(int x, int y) {
        if (x < WIDTH && y < HEIGHT) {
            tiles[x][y] = Tileset.FLOOR;
        }

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                if (xOffset == 0 && yOffset == 0) {
                    continue;
                }

                int neighborX = x + xOffset;
                int neighborY = y + yOffset;

                int sumOffset = Math.abs(xOffset + yOffset);
                if (neighborX < WIDTH && neighborY < HEIGHT) {
                    if (tiles[neighborX][neighborY] == Tileset.NOTHING && (sumOffset == 1)) {
                        tiles[neighborX][neighborY] = Tileset.WALL;
                    }

                    if (tiles[neighborX][neighborY] == Tileset.NOTHING && (sumOffset == 0 || sumOffset == 2)) {
                        tiles[neighborX][neighborY] = Tileset.WALL;
                    }
                }
            }
        }
    }

    // Call this method after generating all rooms to connect them with hallways
    private void connectAllRooms(ArrayList<Room> rooms) {
        for (int i = 0; i < rooms.size() - 1; i++) {
            generateHallways(rooms.get(i), rooms.get(i + 1));
        }
    }




    /**
     * Returns the tiles associated with this world.
     */
    public TETile[][] getTiles() {
        return tiles;
    }

    private void setAllLight() {
        for (Room r:rooms
             ) {
            tiles[r.x + 1][r.y + 1] = Tileset.LIGHT;
        }

    }
}




