package honeycomb.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

// this is the class to run. it opens the window and connects each new game to Swing.
public class HoneycombApp {
    private final JFrame window = new JFrame("Honeycomb");
    private final Random seeds = new Random();
    private long seed = seeds.nextLong();

    private GardenFactory.Round round;
    private HoneycombPanel panel;

    static void main() {
        // ask Swing to create the app on the thread that handles the UI.
        SwingUtilities.invokeLater(() -> new HoneycombApp().open());
    }

    private void open() {
        // restart keeps the seed so we can try the same garden again.
        JButton restart = new JButton("Restart");
        restart.addActionListener(event -> startRound());

        JButton newGarden = new JButton("New garden");
        newGarden.addActionListener(event -> {
            seed = seeds.nextLong();
            startRound();
        });

        // these buttons belong to the window so we can still use them after a loss.
        JPanel actions = new JPanel();
        actions.add(restart);
        actions.add(newGarden);
        window.add(actions, BorderLayout.SOUTH);

        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                round.game().removeObserver(panel);
            }
        });

        startRound();
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void startRound() {
        // on the first round there isn't an old panel to remove.
        if (panel != null) {
            round.game().removeObserver(panel);
            window.remove(panel);
        }

        round = GardenFactory.create(seed);
        panel = new HoneycombPanel(round);

        // direct registration. in Polymorphia we used SPI to find observers.
        // here we already have the panel we want to register.
        round.game().addObserver(panel);

        window.add(panel, BorderLayout.CENTER);
        window.revalidate();
        window.repaint();
    }
}