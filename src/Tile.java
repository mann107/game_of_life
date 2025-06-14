/**
 * Represents a tile on the game board
 * Different tile types trigger different events
 */
public class Tile {
    public enum TileType {
        EMPTY, PAYDAY, MARRIAGE, BABY, HOUSE, ACTION, RETIREMENT, CAREER_CHOICE, COLLEGE_CHOICE
    }
    
    private TileType type;
    private String description;
    private int position;
    
    public Tile(TileType type, String description, int position) {
        this.type = type;
        this.description = description;
        this.position = position;
    }
    
    // Getters
    public TileType getType() { return type; }
    public String getDescription() { return description; }
    public int getPosition() { return position; }
    
    @Override
    public String toString() {
        return "Tile " + position + ": " + description;
    }
}