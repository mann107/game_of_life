import java.util.Arrays;

public class GameBoard {
    private Tile[] tiles;
    private static final int TOTAL_TILES = 200;
    private static final int COLLEGE_PATH_LENGTH = 20;
    private static final int MERGE_POINT = 25;

    public GameBoard() {
        this.tiles = new Tile[TOTAL_TILES];
        createBoard();
    }

    private void createBoard() {
        tiles[0] = new Tile("CAREER_CHOICE", "Choose Career Path or College Path", 0);

        for (int i = 1; i <= 4; i++) {
            tiles[i] = new Tile("EMPTY", "Career Path", i);
        }

        int currentPos = MERGE_POINT;

        tiles[currentPos++] = new Tile("MARRIAGE", "Get Married!", currentPos - 1);

        while (currentPos < TOTAL_TILES - 1) {
            if (currentPos % 15 == 0) {
                tiles[currentPos] = new Tile("PAYDAY", "PayDay - Collect Salary", currentPos);
            } else if (currentPos % 23 == 0) {
                tiles[currentPos] = new Tile("BABY", "Baby! Add a Child", currentPos);
            } else if (currentPos % 35 == 0) {
                tiles[currentPos] = new Tile("HOUSE", "House for Sale", currentPos);
            } else if (currentPos % 47 == 0) {
                tiles[currentPos] = new Tile("BABY", "Twins! Add 2 Children", currentPos);
            } else {
                tiles[currentPos] = new Tile("ACTION", "Draw Action Card", currentPos);
            }
            currentPos++;
        }

        tiles[TOTAL_TILES - 1] = new Tile("RETIREMENT", "Retirement - Game Over!", TOTAL_TILES - 1);
    }

    public Tile getTile(int position) {
        if (position >= 0 && position < tiles.length) {
            return tiles[position];
        }
        return null;
    }

    public int getTotalTiles() {
        return TOTAL_TILES;
    }

    public int getCollegePathLength() {
        return COLLEGE_PATH_LENGTH;
    }

    public int getMergePoint() {
        return MERGE_POINT;
    }

    public double getProgressPercentage(int position) {
        return ((double) position / TOTAL_TILES) * 100.0;
    }

    public Tile[] getAllTiles() {
        Tile[] copy = new Tile[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            copy[i] = tiles[i];
        }
        return copy;
    }
}
