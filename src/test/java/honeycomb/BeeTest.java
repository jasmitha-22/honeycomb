package honeycomb;

import honeycomb.characters.Bee;
import honeycomb.characters.CharacterFactory;
import honeycomb.strategy.StrategyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BeeTest {
    private final CharacterFactory characterFactory = new CharacterFactory(new StrategyFactory());


    @Test
    public void beeCollectsPollenWhenMovingOntoTile() {
        Board board = Board.getNewBuilder()
                .createGrid(1, 2)
                .addPollen(0, 1)
                .build();
        GameCharacter bee = characterFactory.createBee(new Position(0, 0));

        bee.takeTurn(board, new Position(0, 1));

        assertEquals(new Position(0, 1), bee.getPosition());
        assertEquals(1, assertInstanceOf(Bee.class, bee).getPollenCollected());
        assertFalse(board.getTile(0, 1).hasPollen());
    }

    @Test
    public void beeDoesNotCollectAnythingFromAnEmptyTile() {
        Board board = Board.getNewBuilder()
                .createGrid(1, 2)
                .build();
        GameCharacter bee = characterFactory.createBee(Position.at(0, 0));

        bee.takeTurn(board,  Position.at(0, 1));

        assertEquals(0, assertInstanceOf(Bee.class, bee).getPollenCollected());
    }

    @Test
    public void beeCannotMoveIntoAWall() {
        Board board = Board.getNewBuilder()
                .createGrid(1, 2)
                .createWall(0, 1)
                .build();
        GameCharacter bee = characterFactory.createBee(Position.at(0, 0));

        bee.takeTurn(board, Position.at(0, 1));

        assertEquals(Position.at(0, 0), bee.getPosition());
    }

    @Test
    public void beeCannotMoveOffTheBoard() {
        Board board = Board.getNewBuilder()
                .createGrid(1, 2)
                .build();
        GameCharacter bee = characterFactory.createBee(Position.at(0, 1));

        bee.takeTurn(board, Position.at(0, 2));

        assertEquals(Position.at(0, 1), bee.getPosition());
    }

    @Test
    public void beeCannotJumpMoreThanOneTile() {
        Board board = Board.getNewBuilder()
                .createGrid(1, 3)
                .build();
        GameCharacter bee = characterFactory.createBee(Position.at(0, 0));

        bee.takeTurn(board, Position.at(0, 2));

        assertEquals(Position.at(0, 0), bee.getPosition());
    }

}
