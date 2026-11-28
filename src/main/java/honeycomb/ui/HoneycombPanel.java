package honeycomb.ui;

import honeycomb.Board;
import honeycomb.Direction;
import honeycomb.Game;
import honeycomb.GameStatus;
import honeycomb.Position;
import honeycomb.Tile;
import honeycomb.observer.GameObserver;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.URL;
import java.util.Objects;


// the panel has two jobs: show the garden and listen for finished turns.
public class HoneycombPanel extends JPanel implements GameObserver {
    private final ImageIcon bee = loadIcon("bee");
    private final ImageIcon hornet = loadIcon("hornet");
    private final ImageIcon wasp = loadIcon("wasp");
    private final ImageIcon flower = loadIcon("flower");

    private final JLabel status = new JLabel();
    private final JPanel tiles = new JPanel();
    private final GardenFactory.Round round;

    HoneycombPanel(GardenFactory.Round round) {
        super(new BorderLayout(8, 8));
        this.round = round;
        setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));

        add(status, BorderLayout.NORTH);
        add(tiles, BorderLayout.CENTER);

        // make a button connected to the arrow keys for each direction. clicking this plays a turn
        JPanel controls = new JPanel();
        for (Direction direction : Direction.values()) {
            Action move = new MoveCommand(round.game(), direction);
            String key = direction.name();
            controls.add(new JButton(move));
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), key);
            getActionMap().put(key, move);
        }

        JPanel footer = new JPanel(new BorderLayout());
        footer.add(new JLabel("You are the bee, and your job is to collect all the pollen! Beware of your enemies! The Red hornet will chase you while you try to complete this task. The yellow wasp is also dangerous, but it wanders around."),
                BorderLayout.NORTH);
        footer.add(controls, BorderLayout.SOUTH);
        add(footer, BorderLayout.SOUTH);

        // draw the starting positions before the player has taken a turn.
        refresh(round.game());
    }

    private static ImageIcon loadIcon(String name) {
        String path = "/resources/garden/" + name + ".png";
        URL image = HoneycombPanel.class.getResource(path);
        return new ImageIcon(Objects.requireNonNull(image, "Missing image: " + path));
    }

    // our Observer callback. Game calls this after it finishes the turn.
    @Override
    public void onTurnFinished(Game game) {
        refresh(game);
    }

    private void refresh(Game game) {
        Board board = game.getBoard();
        tiles.removeAll();
        tiles.setLayout(new GridLayout(board.getRowCount(), board.getColumnCount(0), 1, 1));

        // each label represents one tile. the background shows path or wall,
        // and the image shows what's standing or growing there.
        for (int row = 0; row < board.getRowCount(); row++) {
            for (int col = 0; col < board.getColumnCount(row); col++) {
                Position spot = Position.at(row, col);
                Tile tile = board.getTile(row, col);

                JLabel cell = new JLabel();
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setPreferredSize(new Dimension(64, 64));
                cell.setOpaque(true);
                if (tile.isWalkable()) {
                    cell.setBackground(new Color(74, 103, 73));
                } else {
                    cell.setBackground(Color.BLACK);
                }

                cell.setIcon(iconAt(spot, tile));
                tiles.add(cell);
            }
        }

        String message = switch (game.getStatus()) {
            case RUNNING -> "Pollen remaining: " + board.countPollen();
            case WON -> "All pollen collected!";
            case LOST -> round.hornet().isOnSameTileAs(game.getBee())
                    ? "Caught by the hornet!"
                    : "Caught by the wasp!";
        };
        status.setText(message);

        // this turns off both its button and its arrow key if the move is disabled
        boolean canMove = game.getStatus() == GameStatus.RUNNING;
        for (Direction direction : Direction.values()) {
            Action move = getActionMap().get(direction.name());
            move.setEnabled(canMove);
        }

        tiles.revalidate();
        tiles.repaint();
    }

    private Icon iconAt(Position spot, Tile tile) {
        // check enemies first so we can see what caught the bee on a losing turn.
        if (round.hornet().getPosition().equals(spot)) {
            return hornet;
        }
        if (round.wasp().getPosition().equals(spot)) {
            return wasp;
        }
        if (round.game().getBee().getPosition().equals(spot)) {
            return bee;
        }

        if(tile.hasPollen()){
            return flower;
        }
        return null;
    }
}