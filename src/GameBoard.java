import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board with all tiles
 * Manages board creation and tile placement
 */
public class GameBoard {
    private List<Tile> tiles;
    private static final int TOTAL_TILES = 200;
    private static final int COLLEGE_PATH_LENGTH = 20;
    private static final int MERGE_POINT = 25;
    
    public GameBoard() {
        this.tiles = new ArrayList<>();
        createBoard();
    }
    
    private void createBoard() {
        // Starting tiles
        tiles.add(new Tile(Tile.TileType.CAREER_CHOICE, "Choose Career Path or College Path", 0));
        
        // Career path tiles (positions 1-4)
        for (int i = 1; i <= 4; i++) {
            tiles.add(new Tile(Tile.TileType.EMPTY, "Career Path", i));
        }
        
        // College path tiles (positions 1-20, but separate from career path)
        // These are handled separately in game logic
        
        // Main board starts at position 25 (merge point)
        int currentPos = MERGE_POINT;
        
        // Add marriage tile early
        tiles.add(new Tile(Tile.TileType.MARRIAGE, "Get Married!", currentPos++));
        
        // Create main board with mostly action tiles as requested
        while (currentPos < TOTAL_TILES - 1) {
            if (currentPos % 15 == 0) {
                tiles.add(new Tile(Tile.TileType.PAYDAY, "PayDay - Collect Salary", currentPos));
            } else if (currentPos % 23 == 0) {
                tiles.add(new Tile(Tile.TileType.BABY, "Baby! Add a Child", currentPos));
            } else if (currentPos % 35 == 0) {
                tiles.add(new Tile(Tile.TileType.HOUSE, "House for Sale", currentPos));
            } else if (currentPos % 47 == 0) {
                tiles.add(new Tile(Tile.TileType.BABY, "Twins! Add 2 Children", currentPos));
            } else {
                // Most tiles are action tiles as requested
                tiles.add(new Tile(Tile.TileType.ACTION, "Draw Action Card", currentPos));
            }
            currentPos++;
        }
        
        // Final tile - retirement
        tiles.add(new Tile(Tile.TileType.RETIREMENT, "Retirement - Game Over!", TOTAL_TILES - 1));
    }
    
    public Tile getTile(int position) {
        if (position >= 0 && position < tiles.size()) {
            return tiles.get(position);
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
    
    public List<Tile> getAllTiles() {
        return new ArrayList<>(tiles);
    }
}