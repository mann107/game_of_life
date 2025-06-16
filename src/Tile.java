
public class Tile {
    // Tile type constants
    public static final String EMPTY = "EMPTY";
    public static final String PAYDAY = "PAYDAY";
    public static final String MARRIAGE = "MARRIAGE";
    public static final String BABY = "BABY";
    public static final String HOUSE = "HOUSE";
    public static final String ACTION = "ACTION";
    public static final String RETIREMENT = "RETIREMENT";
    public static final String CAREER_CHOICE = "CAREER_CHOICE";
    public static final String COLLEGE_CHOICE = "COLLEGE_CHOICE";

    private String type;
    private String description;
    private int position;

    public Tile(String type, String description, int position) {
        this.type = type;
        this.description = description;
        this.position = position;
    }

    // Getters
    public String getType() { return type; }
    public String getDescription() { return description; }
    public int getPosition() { return position; }

    public String toString() {
        return "Tile " + position + ": " + description;
    }
}