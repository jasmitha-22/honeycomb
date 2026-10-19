package honeycomb;

public class Board {
    private Tile[][] tiles;

    public Board(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < tiles.length
                && col >= 0 && col < tiles[row].length;
    }

    public boolean isWalkable(int row, int col) {
        return isWithinBounds(row, col)
                && tiles[row][col].isWalkable();
    }

    public Tile getTile(int row, int col) {
        if (!isWithinBounds(row, col)) {
            throw new IndexOutOfBoundsException("Position is outside the board");
        }

        return tiles[row][col];
    }
}
