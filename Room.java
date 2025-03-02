package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class Room {
    int height;
    int width;

    int x;
    int y;
    boolean turnOn;

    private Random random;

    public Room(int x, int y, int width, int height, TETile[][] tiles, long seed) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        random = new Random(seed);
        turnOn = false;

        for (int i = x; i < width + x; i++) {
            for (int j = y; j < height + y; j++) {
                if (i < WIDTH && j < HEIGHT) {
                    if (i == x || i == width + x - 1 || j == y || j == height + y - 1) {
                        tiles[i][j] = Tileset.WALL;
                    } else {
                        tiles[i][j] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    public int randomX() {
        return random.nextInt(width - 2) + x + 1;
    }

    public int randomY() {
        return random.nextInt(height - 2) + y + 1;
    }

    public boolean contains(int i, int j) {
        return i >= x && i <= x + width && j >= y && j <= y + height;
    }


}

