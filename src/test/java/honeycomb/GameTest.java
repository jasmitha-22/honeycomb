package honeycomb;

import honeycomb.characters.CharacterFactory;
import honeycomb.observer.GameObserver;
import honeycomb.strategy.FrightenedStrategy;
import honeycomb.strategy.HornetStrategy;
import honeycomb.strategy.StrategyFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private final StrategyFactory strategyFactory = new StrategyFactory(new Random(42));
    private final CharacterFactory characterFactory = new CharacterFactory(strategyFactory);

    @Test
    public void collectingAllPollenWinsTheGame() {
        Board board = Board.getNewBuilder().createGrid(1, 2).addPollen(0, 1).build();
        Game game = new Game(board, characterFactory.createBee(new Position(0, 0)), List.of(), strategyFactory);

        game.playTurn(Direction.RIGHT);

        assertEquals(GameStatus.WON, game.getStatus());
        assertFalse(board.getTile(0, 1).hasPollen());
    }

    @Test
    public void beeIsBlockedByWalls() {
        Board board = Board.getNewBuilder().createGrid(1, 2).createWall(0, 1).build();
        Game game = new Game(board, characterFactory.createBee(new Position(0, 0)), List.of(), strategyFactory);

        game.playTurn(Direction.RIGHT);

        assertEquals(new Position(0, 0), game.getBee().getPosition());
    }

    @Test
    public void hornetCatchingTheBeeLosesTheGame() {
        Board board = Board.getNewBuilder().createGrid(1, 4).addPollen(0, 3).build();
        Game game = new Game(board,
                characterFactory.createBee(new Position(0, 0)),
                List.of(characterFactory.createHornet(new Position(0, 2))),
                strategyFactory);

        game.playTurn(Direction.RIGHT);

        assertEquals(GameStatus.LOST, game.getStatus());
    }

    @Test
    public void hornetLandingOnPollenDoesNotCollectIt() {
        Board board = Board.getNewBuilder().createGrid(1, 5).addPollen(0, 3).build();
        Game game = new Game(board,
                characterFactory.createBee(new Position(0, 0)),
                List.of(characterFactory.createHornet(new Position(0, 4))),
                strategyFactory);

        game.playTurn(Direction.LEFT);

        assertEquals(new Position(0, 3), game.getEnemies().getFirst().getPosition());
        assertTrue(board.getTile(0, 3).hasPollen());
    }

    @Test
    public void frightenedEnemiesSwitchStrategyAndThenRecover() {
        Board board = Board.getNewBuilder().createGrid(1, 4).build();
        GameCharacter hornet = characterFactory.createHornet(new Position(0, 3));
        Game game = new Game(board, characterFactory.createBee(new Position(0, 0)), List.of(hornet), strategyFactory);

        game.frightenEnemies();
        assertInstanceOf(FrightenedStrategy.class, hornet.getStrategy());

        game.calmEnemies();
        assertInstanceOf(HornetStrategy.class, hornet.getStrategy());
    }

    @Test
    public void beeStartingOnAWallIsRejected() {
        Board board = Board.getNewBuilder().createGrid(2, 2).createWall(0, 0).build();
        assertThrows(IllegalArgumentException.class,
                () -> new Game(board, characterFactory.createBee(new Position(0, 0)), List.of(), strategyFactory));
    }

    @Test
    public void observerSeesTheWinExactlyOnce() {
        Board board = Board.getNewBuilder()
                .createGrid(1, 2)
                .addPollen(0, 1)
                .build();
        GameCharacter bee = characterFactory.createBee(Position.at(0, 0));
        Game game = new Game(board, bee, List.of(), strategyFactory);

        // Record the status the observer receives after each completed turn.
        List<GameStatus> observedStatuses = new ArrayList<>();
        game.addObserver(updatedGame -> observedStatuses.add(updatedGame.getStatus()));

        game.playTurn(Direction.RIGHT); // Collects the last pollen.
        game.playTurn(Direction.LEFT);  // The game has already ended.

        assertEquals(List.of(GameStatus.WON), observedStatuses);
    }

    @Test
    public void removedObserverStopsReceivingTurnUpdates() {
        Board board = Board.getNewBuilder()
                .createGrid(1, 3)
                .addPollen(0, 2)
                .build();
        GameCharacter bee = characterFactory.createBee(Position.at(0, 0));
        Game game = new Game(board, bee, List.of(), strategyFactory);

        List<GameStatus> updates = new ArrayList<>();
        GameObserver observer = updatedGame -> updates.add(updatedGame.getStatus());

        game.addObserver(observer);
        game.playTurn(Direction.RIGHT);

        game.removeObserver(observer);
        game.playTurn(Direction.RIGHT);

        //checks current status updated, but observer has previous status because it was removed.
        assertEquals(GameStatus.WON, game.getStatus());
        assertEquals(List.of(GameStatus.RUNNING), updates);
    }
}
