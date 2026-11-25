package honeycomb.strategy;

import honeycomb.Board;
import honeycomb.Direction;
import honeycomb.Position;

public class BeeStrategy implements MovementStrategy{

    @Override
    public Direction chooseMove(Position current, Position target, Board board) {
        for(Direction direction : Direction.getAllowedMoves(current, board)){
            if(direction.getNextPosition(current).equals(target)){
                return direction;
            }
        }
        return null;
    }
}
