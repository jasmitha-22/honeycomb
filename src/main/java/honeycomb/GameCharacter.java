package honeycomb;

import honeycomb.strategy.MovementStrategy;

public abstract class GameCharacter {
    private final String name;
    private Position position;
    private final MovementStrategy defaultStrategy;
    private MovementStrategy strategy;

    protected GameCharacter(String name, MovementStrategy strategy) {
        this.name = name;
        this.defaultStrategy = strategy;
        this.strategy = strategy;
    }

    public boolean isOnSameTileAs(GameCharacter other) {
        return position.equals(other.getPosition());
    }

    public void takeTurn(Board board, Position target){
        Direction direction = strategy.chooseMove(position, target, board);
        if(direction != null){
            moveTo(direction.getNextPosition(position), board);
        }
    }

    public void setStrategy(MovementStrategy strategy){ this.strategy = strategy;}

    public MovementStrategy getStrategy() {
        return strategy;
    }

    public void resetStrategy() {this.strategy = defaultStrategy;}

    protected boolean moveTo(Position newPosition, Board board) {
        if (!board.canMoveTo(newPosition)) {
            return false;
        }
        position = newPosition;
        onArrive(board.getTile(position.getRow(), position.getColumn()));
        return true;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    protected abstract void onArrive(Tile tile);

    @Override
    public String toString() {
        return name;
    }
}