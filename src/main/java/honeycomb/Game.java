package honeycomb;

import honeycomb.strategy.StrategyFactory;

import java.util.List;

public class Game {
    private final Board board;
    private final GameCharacter bee;
    private final List<GameCharacter> enemies;
    private final int totalStartingPollen;
    private final StrategyFactory strategyFactory;
    private GameStatus status = GameStatus.RUNNING;

    public Game(Board board, GameCharacter bee, List<GameCharacter> enemies, StrategyFactory strategyFactory) {
        if(!board.canMoveTo(bee.getPosition())){
            throw new IllegalArgumentException("The bee can't start on a wall or off the board");
        }
        for(GameCharacter enemy : enemies){
            if(!board.canMoveTo(enemy.getPosition())){
                throw new IllegalArgumentException(enemy.getName() + " can't start on a wall or off the board");

            }
        }

        this.board = board;
        this.bee = bee;
        this.enemies = enemies;
        this.totalStartingPollen = board.countPollen();
        this.strategyFactory = strategyFactory;
    }

    public void playTurn(Direction beeMove) {
        if (isGameOver()) {
            return;
        }

        moveBee(beeMove);
        winOrLoss();

        if (isGameOver()) {
            return;
        }

        moveEnemies();
        winOrLoss();
    }

    private void moveBee(Direction beeMove) {
        Position tileThatPlayerWants = beeMove.getNextPosition(bee.getPosition());
        bee.takeTurn(board, tileThatPlayerWants);
    }

    private void moveEnemies(){
        for (GameCharacter enemy : enemies) {
            enemy.takeTurn(board, bee.getPosition());
        }
    }

    private boolean isGameOver() {
        return status != GameStatus.RUNNING;
    }

    private void winOrLoss() {
        if (isBeeCaught()) {
            status = GameStatus.LOST;
        } else if (hasCollectedAllPollen()) {
            status = GameStatus.WON;
        }
    }


    private boolean hasCollectedAllPollen(){
        return totalStartingPollen > 0 && board.countPollen() == 0;
    }

    private boolean isBeeCaught() {
        for (GameCharacter enemy : enemies) {
            if (enemy.isOnSameTileAs(bee)) {
                return true;
            }
        }
        return false;
    }

    public void frightenEnemies(){
        for(GameCharacter enemy : enemies){
            enemy.setStrategy(strategyFactory.newFrightenedStrategy());
        }
    }

    public void calmEnemies(){
        for(GameCharacter enemy : enemies){
            enemy.resetStrategy();
        }
    }

    public GameStatus getStatus(){ return status; }

    public Board getBoard() { return board; }

    public GameCharacter getBee() { return bee; }

    public List<GameCharacter> getEnemies(){return enemies;}

    public int getTotalStartingPollen() { return totalStartingPollen; }

}
