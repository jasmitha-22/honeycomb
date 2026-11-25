package honeycomb.strategy;

import honeycomb.Board;
import honeycomb.Direction;
import honeycomb.Position;

public class FrightenedStrategy implements MovementStrategy{
    @Override
    public Direction chooseMove(Position current, Position target, Board board) {
        Direction bestMovetoBeFurthestFromBee = null;
        int bestDistance = -1;
        for(Direction direction : Direction.getAllowedMoves(current, board)){
            int distance = MovementStrategy.distance(direction.getNextPosition(current), target);
            if(distance > bestDistance){
                bestMovetoBeFurthestFromBee = direction;
                bestDistance = distance;
            }
        }
        return bestMovetoBeFurthestFromBee;
    }
}
