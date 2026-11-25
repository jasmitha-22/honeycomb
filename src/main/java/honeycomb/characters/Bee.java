package honeycomb.characters;
import honeycomb.GameCharacter;
import honeycomb.Tile;
import honeycomb.strategy.MovementStrategy;


public class Bee extends GameCharacter {
    private int pollenCollected;

    public Bee(String name, MovementStrategy strategy) {
        super(name, strategy);
        this.pollenCollected = 0;
    }

    public int getPollenCollected() {
        return pollenCollected;
    }

    @Override
    protected void onArrive(Tile tile) {
        if (tile.collectPollen()) {
            pollenCollected++;
        }
    }


}