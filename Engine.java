package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    private boolean menuPage = true;
    private boolean avatarPage = false;
    private boolean avatarChosen = false;
    private boolean avatarAfterSeed = true;
    private boolean changeAppearance = false;
    private Out out;
    private In in;
    private StringBuilder commands;
    private String newCommands = "";
    TETile[][] tiles;
    private static Avatar avatar;
    private static MapGenerator mpg;
    private boolean quit;
    private String appearance = "avatar";
    public static final int FIFTEEN = 15;
    public static final int SIXTEEN = 16;
    public static final int TWENTY_FIVE = 25;
    public static final int FIFTY = 50;
    public static final int FORTY = 40;
    public static final int THIRTY = 30;
    public static final int THREE_HUNDRED = 300;
    public static final int ONE_THOUSAND = 1000;
    public static final int TWO_THOUSAND = 2000;
    public static final int THREE_THOUSAND = 3000;
    public static final double TWO_POINT_FIVE = 2.5;
    public static final double TWO_POINT_SIX = 2.6;
    public static final double THREE_FOURTH = 0.75;
    public static final double POINT_NINE = 0.9;
    public static final double POINT_SEVEN = 0.7;
    public static final double POINT_FIVE = 0.5;
    public static final double POINT_THREE = 0.3;
    public static final double POINT_ONE = 0.1;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        commands = new StringBuilder();
        displayMenu();
        while (menuPage) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (key) {
                    case 'n':
                        initializeGame(); // sets menuPage = false
                        break;
                    case 'l':
                        if (!avatarChosen) {
                            changeAvatarAfterSeed();
                        }
                        loadGame(); // sets menuPage = false
                        loadPage();
                        ter.initialize(WIDTH, HEIGHT);
                        ter.renderFrame(avatar.getTiles());
                        break;
                    case 'q':
                        System.exit(0); // close page
                        break;
                    case 'c':
                        chooseAvatar();
                        displayMenu();
                        break;
                    default:
                        menuPage = true;
                }
            }
        }
        while (!menuPage) {
            hud();
            Room r = getRoom();
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (key) {
                    case ':':
                        commands.append(key);
                        break;
                    case 'q':
                        quitAndSave();
                        if (quit) {
                            System.exit(0);
                        }
                        break;
                    case 'a', 'd', 'w', 's':
                        commands.append(key);
                        avatar.move(key);
                        ter.renderFrame(avatar.getTiles());
                        break;
                    case 'o':
                        commands.append(key);
                        if (r != null) {
                            turnOnLight(r);
                            ter.renderFrame(avatar.getTiles());
                        }
                        break;
                    case 'f':
                        commands.append(key);
                        if (r != null) {
                            turnOffLight(r);
                            ter.renderFrame(avatar.getTiles());
                        }
                        break;
                    default:
                        menuPage = false;
                }
            }
        }
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        if (input.isEmpty()) {
            return tiles;
        }
        existedCommands();
        commands = new StringBuilder();
        menuPage = false;
        long seed = 0;
        String moves = "";
        String inputLower = input.toLowerCase();

        int nIndex = inputLower.indexOf('n');
        int sIndex = inputLower.indexOf('s');
        int colonIndex = inputLower.indexOf(':');
        int qIndex = inputLower.indexOf('q');

        if (inputLower.charAt(0) == 'l') {
            loadGame();
            String newMoves = inputLower.substring(1);
            inputLower = commands.toString() + newMoves;
            commands.setLength(0);
            nIndex = inputLower.indexOf('n');
            sIndex = inputLower.indexOf('s');
            colonIndex = inputLower.indexOf(':');
            qIndex = inputLower.indexOf('q');
        }
        if (nIndex >= 0 && sIndex >= 0) {
            String stringSeed = inputLower.substring(nIndex + 1, sIndex);
            seed = Long.parseLong(stringSeed);
            moves = inputLower.substring(sIndex + 1);
        }
        if (colonIndex >= 0 && colonIndex == qIndex - 1) {
            moves = inputLower.substring(0, qIndex);
            commands.append(moves);
            quitAndSave();
        }
        if (nIndex < 0 || sIndex < 0 && inputLower.charAt(0) != 'l') {
            int seedIndex = 0;
            int appearanceIndex = appearanceLetterIndex(inputLower);
            int firstLetter = firstLetterIndex(inputLower);
            if (appearanceIndex == 0) {
                moves = input.substring(0, 1);
                seedIndex = 1;
                seed = Long.parseLong(inputLower.substring(seedIndex, firstLetter));
                moves += input.substring(firstLetter, inputLower.length());
            } else {
                int end = firstLetter;
                if (appearanceIndex != -1) {
                    end = Math.min(firstLetter, appearanceIndex);
                }
                seed = Long.parseLong(inputLower.substring(seedIndex, end));
                moves = input.substring(end, inputLower.length());
            }
        }
        mpg = new MapGenerator(HEIGHT, WIDTH, seed);
        avatar = new Avatar(mpg, appearance);
        while (!moves.isEmpty()) {
            char c = moves.charAt(0);
            lightSetup(c);
            if (c == 'a' || c == 's' || c == 'w' || c == 'd') {
                avatar.move(c);
            } else {
                avatar.changeAppearance(c);
                possibleChangeAppearance();
            }
            moves = moves.substring(1);
        }
        roomSetup();
        tiles = avatar.getTiles();
        return tiles;
    }

    private void existedCommands() {
        if (commands != null) {
            newCommands = commands.toString();
        }
    }

    private void lightSetup(char c) {
        if (c == 'o') {
            Room r1 = getRoom();
            turnOnLight(r1);
        }
        if (c == 'f') {
            Room r2 = getRoom();
            turnOffLight(r2);
        }
    }

    private void roomSetup() {
        for (Room r : mpg.rooms
        ) {
            if (r.turnOn) {
                turnOnLight(r);
            }
        }
    }

    private void possibleChangeAppearance() {
        if (changeAppearance) {
            avatar.appearance = appearance;
            avatar.changeAppearance('j');
        }
        if (avatarChosen) {
            avatar.appearance = appearance;
            avatar.changeAppearance('j');
        }
    }

    private int firstLetterIndex(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'a' || c == 's' || c == 'w' || c == 'd') {
                return i;
            }
        }
        return -1;
    }

    private int appearanceLetterIndex(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'z' || c == 'x' || c == 'c' || c == 'v' || c == 'b') {
                return i;
            }
        }
        return -1;
    }

    /**
     * Displays the menu.
     */
    public void displayMenu() {
        menuPage = true;

        StdDraw.setCanvasSize(WIDTH * SIXTEEN, HEIGHT * SIXTEEN);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FIFTY);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2.0, HEIGHT * THREE_FOURTH, "CS61B: THE GAME");
        Font fontSmall = new Font("Monaco", Font.BOLD, THIRTY);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2.0, HEIGHT / TWO_POINT_FIVE, "NEW GAME (N)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / TWO_POINT_FIVE - 3, "LOAD GAME (L)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / TWO_POINT_FIVE - 6, "QUIT (Q)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / TWO_POINT_FIVE - 9, "CHOOSE APPEARANCE (C)");
        StdDraw.show();
    }

    public void changeAvatarAfterSeed() {
        avatarAfterSeed = true;
        askAppearance();
        while (avatarAfterSeed) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (key) {
                    case 'y':
                        changeAppearance = true;
                        chooseAvatar();
                        break;
                    case 'n':
                        avatarPage = false;
                        changeAppearance = false;
                        break;
                    default:
                        avatarAfterSeed = true;
                }
                avatarAfterSeed = false;
            }
        }
    }

    public void askAppearance() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FORTY);
        StdDraw.setFont(fontBig);

        StdDraw.text(WIDTH / 2, HEIGHT * THREE_FOURTH, "DO YOU WANT TO CHANGE APPEARANCE? (╹◡╹✿)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "YES(Y)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "NO, START GAME(N)");
        StdDraw.show();
    }

    public void chooseAvatar() {
        avatarPage = true;
        avatarPage();
        while (avatarPage) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                commands.append(key);
                switch (key) {
                    case 'z':
                        appearance = "flower";
                        break;
                    case 'x':
                        appearance = "clover";
                        break;
                    case 'c':
                        appearance = "smiley";
                        break;
                    case 'v':
                        appearance = "heart";
                        break;
                    case 'b':
                        appearance = "boat";
                        break;
                    default:
                        appearance = "avatar";
                }
                avatarPage = false;
                avatarChosen = true;
            }
        }

    }

    private void avatarPage() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FORTY);
        StdDraw.setFont(fontBig);

        StdDraw.text(WIDTH / 2, HEIGHT / 2, "PlEASE ENTER THE KEY TO SELECT APPEARANCE");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "(*╹▽╹*)");
        StdDraw.show(TWO_THOUSAND);

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontBig);

        StdDraw.textLeft(WIDTH / TWO_POINT_SIX, HEIGHT * POINT_NINE, "(Z) FLOWER  ❀");
        StdDraw.textLeft(WIDTH / TWO_POINT_SIX, HEIGHT * POINT_SEVEN, "(X) CLOVER  ☘");
        StdDraw.textLeft(WIDTH / TWO_POINT_SIX, HEIGHT * POINT_FIVE, "(C) SMILEY  ☻");
        StdDraw.textLeft(WIDTH / TWO_POINT_SIX, HEIGHT * POINT_THREE, "(V) HEART  ♥");
        StdDraw.textLeft(WIDTH / TWO_POINT_SIX, HEIGHT * POINT_ONE, "(B) BOAT  ⛴");

        StdDraw.show();
    }

    private void askSeedPage() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FORTY);
        StdDraw.setFont(fontBig);

        StdDraw.text(WIDTH / 2, HEIGHT / 2, "PlEASE ENTER THE SEED(ONLY NUMBERS)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "(*╹▽╹*)");
        StdDraw.show(TWO_THOUSAND);

    }

    public void hud() {
        while (!StdDraw.hasNextKeyTyped()) {
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            if (x < WIDTH && y < HEIGHT) {
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.filledRectangle(1, HEIGHT - 1, 4, 1);
                displayType(getType(x, y));
            }
        }
    }

    public String getType(int x, int y) {
        return tiles[x][y].description();
    }

    public void displayType(String s) {
        Font font = new Font("Monaco", Font.PLAIN, FIFTEEN);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(1, HEIGHT - 1, s);
        StdDraw.show();
    }

    /**
     * Initializes the game after the seed is set.
     */
    private long getSeed() {
        StringBuilder seed = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c == 's') {
                    break;
                }
                if (Character.isDigit(c)) {
                    seed.append(c);
                    drawFrame(seed.toString());
                    StdDraw.pause(THREE_HUNDRED);
                }
            }
        }
        String stringSeed = seed.toString();
        commands.append(stringSeed);
        return Long.parseLong(stringSeed);
    }

    private void instructionPage() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FORTY);
        StdDraw.setFont(fontBig);

        StdDraw.text(WIDTH / 2, HEIGHT / 2, "(づ๑•ᴗ•๑)づ♡ IN YOUR WORLD YOU CAN");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "TURN ON LIGHT(O)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, "TURN OFF LIGHT(F)");
        StdDraw.show(THREE_THOUSAND);
    }

    private void loadPage() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FORTY);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "YOUR WORLD IN 3");
        Font large = new Font("Monaco", Font.BOLD, FIFTY);
        StdDraw.setFont(large);
        StdDraw.textLeft(0, HEIGHT / 2 - 5, "─=≡Σʕっ•ᴥ•ʔっ");
        StdDraw.show(ONE_THOUSAND);

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "2");
        StdDraw.setFont(large);
        StdDraw.textLeft(0, HEIGHT / 2 - 5, "────=======≡≡≡≡≡Σʕっ•ᴥ•ʔっ");
        StdDraw.show(ONE_THOUSAND);

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "1");
        StdDraw.setFont(large);
        StdDraw.textLeft(0, HEIGHT / 2 - 5, "─────────========≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡≡Σʕっ•ᴥ•ʔっ");
        StdDraw.show(ONE_THOUSAND);
    }


    public void drawFrame(String s) {
        menuPage = true;

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, FORTY);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "YOUR SEED: " + s);
        Font fontSmall = new Font("Monaco", Font.BOLD, TWENTY_FIVE);
        StdDraw.setFont(fontSmall);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "END YOUR SEED WITH (S)");
        StdDraw.show();
    }

    private void initializeGame() {
        askSeedPage();
        long seed = getSeed();
        if (!avatarChosen) {
            changeAvatarAfterSeed();
        }
        menuPage = false;
        instructionPage();
        loadPage();
        mpg = new MapGenerator(HEIGHT, WIDTH, seed);

        avatar = new Avatar(mpg, appearance);
        tiles = mpg.getTiles();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(tiles);
    }

    private void quitAndSave() {
        if (!commands.isEmpty()) {
            char c = commands.charAt(commands.length() - 1);
            if (c == ':') {
                out = new Out("commands.txt");
                String stringOfCommands = commands.substring(0, commands.length() - 1);
                out.print(stringOfCommands);
                quit = true;
            } else {
                quit = false;
            }
        }
    }

    private void loadGame() {
        menuPage = false;
        in = new In("commands.txt");
        String savedCommands = in.readLine();
        tiles = interactWithInputString(savedCommands);
        commands.append(savedCommands);
        commands.append(newCommands);
    }

    private void turnOnLight(Room r) {
        if (r != null) {
            r.turnOn = true;
            for (int i = r.x + 1; i < r.x + r.width - 1; i++) {
                for (int j = r.y + 1; j < r.y + r.height - 1; j++) {
                    if (i == avatar.x && j == avatar.y) {
                        avatar.tiles[i][j] = avatar.lightOnAppearance(avatar.appearance);
                        avatar.prevLight = Tileset.LIGHTWITHLIGHT;
                    } else {
                        if (avatar.tiles[i][j] == Tileset.LIGHT || avatar.tiles[i][j] == Tileset.LIGHTWITHLIGHT) {
                            avatar.tiles[i][j] = Tileset.LIGHTWITHLIGHT;
                        } else {
                            avatar.tiles[i][j] = Tileset.LIGHTFLOOR;
                        }
                    }
                }
            }
        }
    }

    private void turnOffLight(Room r) {
        if (r != null && r.turnOn) {
            for (int i = r.x + 1; i < r.x + r.width - 1; i++) {
                for (int j = r.y + 1; j < r.y + r.height - 1; j++) {
                    if (i == avatar.x && j == avatar.y) {
                        avatar.tiles[i][j] = avatar.noLightAppearance(avatar.appearance);
                        avatar.prevLight = Tileset.LIGHT;
                    }
                    if (!(i == avatar.x && j == avatar.y)) {
                        if (avatar.tiles[i][j] == Tileset.LIGHTWITHLIGHT || avatar.tiles[i][j] == Tileset.LIGHT) {
                            avatar.tiles[i][j] = Tileset.LIGHT;
                        } else {
                            avatar.tiles[i][j] = Tileset.FLOOR;
                        }
                    }
                }
            }
            r.turnOn = false;
        }
    }


    public static Room getRoom() {
        for (Room r : mpg.rooms
        ) {
            if ((avatar.x <= r.x + r.width - 2) && (avatar.x > r.x)
                    && (avatar.y <= r.y + r.height - 2) && (avatar.y > r.y)) {
                return r;
            }
        }
        return null;
    }


}


