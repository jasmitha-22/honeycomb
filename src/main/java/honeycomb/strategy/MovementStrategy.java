package honeycomb.strategy;

import honeycomb.Board;
import honeycomb.Direction;
import honeycomb.Position;

public interface MovementStrategy {

    /** Returns the direction to move, or null if there is no legal move. */
    Direction chooseMove(Position current, Position target, Board board);

    static int distance(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getColumn() - b.getColumn());
    }
}