package honeycomb;
import honeycomb.Position;
import honeycomb.Board;
import honeycomb.Tile;


public class Bee {
    private Position position;
    private int pollenCollected;

    public Bee(Position position) {
        this.position = position;
        this.pollenCollected = 0;
    }

    public Position getPosition() {
        return position;
    }

    public int getPollenCollected() {
        return pollenCollected;
    }

    public boolean move(int rowChange, int colChange, Board board) {
        Position newPosition = new Position(
                position.getRow() + rowChange,
                position.getColumn() + colChange
        );

        if (!board.isWalkable(newPosition.getRow(), newPosition.getColumn())) {
            return false;
        }

        position = newPosition;

        Tile tile = board.getTile(
                position.getRow(), position.getColumn());

        if (tile.collectPollen()) {
            pollenCollected++;
        }

        return true;
    }
}