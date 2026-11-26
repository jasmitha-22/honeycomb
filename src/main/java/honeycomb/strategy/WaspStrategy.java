package honeycomb.strategy;

import honeycomb.Board;
import honeycomb.Direction;
import honeycomb.Position;

import java.util.List;
import java.util.Random;

public class WaspStrategy implements MovementStrategy{
    private final Random random;

    public WaspStrategy(Random random) { this.random = random; }

    @Override
    public Direction chooseMove(Position current, Position target, Board board) {
        List<Direction> allowedMoves = Direction.getAllowedMoves(current, board);
        if(allowedMoves.isEmpty()){
            return null;
        }
        return allowedMoves.get(random.nextInt(allowedMoves.size()));
    }
}
