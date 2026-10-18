package honeycomb;
import main.java.honeycomb.TileType;

public class Tile {
    private final TileType type;
    private boolean hasPollen;

    public Tile(TileType type) {
        this.type = type;
        this.hasPollen = false;
    }

    public TileType getType() {
        return type;
    }

    public boolean isWalkable() {
        return type == TileType.PATH;
    }

    public boolean hasPollen() {
        return hasPollen;
    }

    public void addPollen() {
        hasPollen = true;
    }

    public boolean collectPollen() {
        if (hasPollen) {
            hasPollen = false;
            return true;
        }

        return false;
    }
}
