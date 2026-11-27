package honeycomb.ui;

import honeycomb.Direction;
import honeycomb.Game;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

// this is one movement request.
public final class MoveCommand extends AbstractAction {
    private final Game game;
    private final Direction direction;

    public MoveCommand(Game game, Direction direction) {
        super(direction.name()); // swing uses this name as the button label.
        this.game = game;
        this.direction = direction;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        game.playTurn(direction);
    }
}