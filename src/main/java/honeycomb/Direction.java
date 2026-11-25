package honeycomb;

import java.util.ArrayList;
import java.util.List;

public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int rowChange;
    private final int colChange;

    Direction(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    public int getRowChange() {
        return rowChange;
    }

    public int getColChange() {
        return colChange;
    }

    public Position getNextPosition(Position start){
        int newRow = start.getRow() + rowChange;
        int newCol = start.getColumn() + colChange;
        return new Position(newRow, newCol);
    }

    public static List<Direction> getAllowedMoves(Position start, Board board){
        List<Direction> allowedMoves = new ArrayList<>();
        for(Direction direction : Direction.values()){
            Position nextPosition = direction.getNextPosition(start);
            if(board.canMoveTo(nextPosition)){
                allowedMoves.add(direction);
            }
        }
        return allowedMoves;
    }
}


