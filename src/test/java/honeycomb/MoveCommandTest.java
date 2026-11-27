package honeycomb;

import honeycomb.characters.CharacterFactory;
import honeycomb.strategy.StrategyFactory;
import honeycomb.ui.MoveCommand;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MoveCommandTest {
    @Test
    void downCommandMovesTheBeeDownOneTile() {
        StrategyFactory strategies = new StrategyFactory();
        CharacterFactory characters = new CharacterFactory(strategies);
        Board board = Board.getNewBuilder().createGrid(3, 3).build();
        GameCharacter bee = characters.createBee(Position.at(1, 1));
        Game game = new Game(board, bee, List.of(), strategies);

        JButton down = new JButton(new MoveCommand(game, Direction.DOWN));
        down.doClick(0);

        assertEquals(Position.at(2, 1), bee.getPosition());
    }
}
