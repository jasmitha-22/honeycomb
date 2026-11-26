package honeycomb.strategy;

import honeycomb.Board;
import honeycomb.Direction;
import honeycomb.Position;

public class HornetStrategy implements MovementStrategy{

    @Override
    public Direction chooseMove(Position current, Position target, Board board) {
        Direction bestMoveToGetCloserToBee = null;
        int bestDistance = Integer.MAX_VALUE;
        for(Direction direction : Direction.getAllowedMoves(current, board)){
            int distance = MovementStrategy.distance(direction.getNextPosition(current), target);
            if(distance < bestDistance){
                bestMoveToGetCloserToBee = direction;
                bestDistance = distance;
            }
        }
        return bestMoveToGetCloserToBee;
    }
}
