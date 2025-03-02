
package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;


public class Avatar {
    int x;
    int y;
    int lightX;
    int lightY;
    MapGenerator mpg;
    TETile[][] tiles;
    String appearance;
    TETile lightOn;
    TETile lightOff;
    TETile prevLight;


    public Avatar(MapGenerator mpg, String appearance) {
        this.mpg = mpg;
        tiles = mpg.tiles;
        this.appearance = appearance;
        lightOff = noLightAppearance(appearance);
        lightOn = lightOnAppearance(appearance);
        addAvatar();
    }

    public TETile noLightAppearance(String s) {
        return switch (s) {
            case "flower" -> Tileset.FLOWER;
            case "boat" -> Tileset.BOAT;
            case "clover" -> Tileset.CLOVER;
            case "smiley" -> Tileset.SMILEY;
            case "heart" -> Tileset.HEART;
            default -> Tileset.AVATAR;
        };
    }

    public TETile lightOnAppearance(String s) {
        return switch (s) {
            case "flower" -> Tileset.FLOWERLIGHT;
            case "boat" -> Tileset.BOATLIGHT;
            case "clover" -> Tileset.CLOVERLIGHT;
            case "smiley" -> Tileset.SMILEYLIGHT;
            case "heart" -> Tileset.HEARTLIGHT;
            default -> Tileset.AVATARLIGHT;
        };
    }

    public void addAvatar() {
        ArrayList<Room> rooms = mpg.rooms;
        int i = mpg.random.nextInt(rooms.size());
        Room r = rooms.get(i);
        x = r.x + 2;
        y = r.y + 2;
        tiles[x][y] = lightOff;
    }


    public void move(char key) {
        switch (key) {
            case 'a' -> moveLeft();
            case 'd' -> moveRight();
            case 'w' -> moveUp();
            case 's' -> moveDown();
        }
    }

    public void changeAppearance(char key) {
        switch (key) {
            case 'z' -> appearance = "flower";
            case 'x' -> appearance = "clover";
            case 'c' -> appearance = "smiley";
            case 'v' -> appearance = "heart";
            case 'b' -> appearance = "boat";
        }
        lightOff = noLightAppearance(appearance);
        lightOn = lightOnAppearance(appearance);
    }

    private void resumeFloor() {
        if (tiles[x][y] == lightOn) {
            tiles[x][y] = Tileset.LIGHTFLOOR;
        } else {
            tiles[x][y] = Tileset.FLOOR;
        }
    }

    private void saveLight(int x, int y) {
        lightX = x;
        lightY = y;
        prevLight = tiles[x][y];
    }

    private void resumeLight() {
//        if (tiles[x][y] == lightOn) {
//            tiles[x][y] = Tileset.LIGHTWITHLIGHT;
//        } else {
//            tiles[x][y] = Tileset.LIGHT;
//        }
        if (tiles[lightX][lightY] == Tileset.FLOOR || tiles[lightX][lightY] == Tileset.LIGHTFLOOR) {
            if (prevLight == Tileset.LIGHT) {
                tiles[lightX][lightY] = Tileset.LIGHT;
            }
            if (prevLight == Tileset.LIGHTWITHLIGHT) {
                tiles[lightX][lightY] = Tileset.LIGHTWITHLIGHT;
            }
        }
    }


    private void moveUp() {
        int newY = y + 1;
        if (tiles[x][newY] == Tileset.FLOOR) {
            resumeFloor();
            tiles[x][newY] = lightOff;
            this.y += 1;
        }

        if (tiles[x][newY] == Tileset.LIGHTFLOOR) {
            resumeFloor();
            tiles[x][newY] = lightOn;
            this.y += 1;
        }

        if (tiles[x][newY] == Tileset.LIGHT) {
            saveLight(x, newY);
            tiles[x][newY] = lightOff;
            tiles[x][y] = Tileset.FLOOR;
            this.y += 1;

        }
        if (tiles[x][newY] == Tileset.LIGHTWITHLIGHT) {
            saveLight(x, newY);
            tiles[x][newY] = lightOn;
            tiles[x][y] = Tileset.LIGHTFLOOR;
            this.y += 1;
        }
        resumeLight();
    }

    private void moveDown() {
        int newY = y - 1;
        if (tiles[x][newY] == Tileset.FLOOR) {
            resumeFloor();
            tiles[x][newY] = lightOff;
            this.y -= 1;
        }

        if (tiles[x][newY] == Tileset.LIGHTFLOOR) {
            resumeFloor();
            tiles[x][newY] = lightOn;
            this.y -= 1;
        }
        if (tiles[x][newY] == Tileset.LIGHT) {
            saveLight(x, newY);
            tiles[x][newY] = lightOff;
            tiles[x][y] = Tileset.FLOOR;
            this.y -= 1;

        }
        if (tiles[x][newY] == Tileset.LIGHTWITHLIGHT) {
            saveLight(x, newY);
            tiles[x][newY] = lightOn;
            tiles[x][y] = Tileset.LIGHTFLOOR;
            this.y -= 1;
        }
        resumeLight();
    }

    private void moveLeft() {
        int newX = x - 1;

        if (tiles[newX][y] == Tileset.FLOOR) {
            resumeFloor();
            tiles[newX][y] = lightOff;
            this.x -= 1;
        }

        if (tiles[newX][y] == Tileset.LIGHTFLOOR) {
            resumeFloor();
            tiles[newX][y] = lightOn;
            this.x -= 1;
        }
        if (tiles[newX][y] == Tileset.LIGHT) {
            saveLight(newX, y);
            tiles[newX][y] = lightOff;
            tiles[x][y] = Tileset.FLOOR;
            this.x -= 1;

        }
        if (tiles[newX][y] == Tileset.LIGHTWITHLIGHT) {
            saveLight(newX, y);
            tiles[newX][y] = lightOn;
            tiles[x][y] = Tileset.LIGHTFLOOR;
            this.x -= 1;
        }
        resumeLight();
    }


    private void moveRight() {
        int newX = x + 1;

        if (tiles[newX][y] == Tileset.FLOOR) {
            resumeFloor();
            tiles[newX][y] = lightOff;
            this.x += 1;
        }

        if (tiles[newX][y] == Tileset.LIGHTFLOOR) {
            resumeFloor();
            tiles[newX][y] = lightOn;
            this.x += 1;
        }
        if (tiles[newX][y] == Tileset.LIGHT) {
            saveLight(newX, y);
            tiles[newX][y] = lightOff;
            tiles[x][y] = Tileset.FLOOR;
            this.x += 1;
        }
        if (tiles[newX][y] == Tileset.LIGHTWITHLIGHT) {
            saveLight(newX, y);
            tiles[newX][y] = lightOn;
            tiles[x][y] = Tileset.LIGHTFLOOR;
            this.x += 1;
        }
        resumeLight();
    }


    public TETile[][] getTiles() {
        return tiles;
    }
}
