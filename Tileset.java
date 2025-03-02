package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.black, "you");

    public static final TETile AVATARLIGHT = new TETile('@', Color.white, Color.orange, "you");
    public static final TETile FLOWERLIGHT = new TETile('❀', Color.magenta, Color.orange, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile LIGHTFLOOR = new TETile('·', new Color(128, 192, 128), Color.orange,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");

    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile LIGHTON = new TETile('▲', Color.yellow, Color.orange,
            "light on");
    public static final TETile LIGHTOFF = new TETile('▲', Color.yellow, Color.black,
            "light off");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    public static final TETile BOAT = new TETile('⛴', Color.green, Color.black, "you");
    public static final TETile SMILEY = new TETile('☻', Color.yellow, Color.black, "you");
    public static final TETile CLOVER = new TETile('☘', Color.green, Color.black, "you");
    public static final TETile HEART = new TETile('♥', Color.pink, Color.black, "you");
    public static final TETile LIGHT = new TETile('☀', Color.YELLOW, Color.black, "light");

    public static final TETile BOATLIGHT = new TETile('⛴', Color.green, Color.orange, "you");
    public static final TETile SMILEYLIGHT = new TETile('☻', Color.yellow, Color.orange, "you");
    public static final TETile CLOVERLIGHT = new TETile('☘', Color.green, Color.orange, "you");
    public static final TETile HEARTLIGHT = new TETile('♥', Color.pink, Color.orange, "you");
    public static final TETile LIGHTWITHLIGHT = new TETile('☀', Color.YELLOW, Color.orange, "light");
}