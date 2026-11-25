package honeycomb.characters;

import honeycomb.GameCharacter;
import honeycomb.Tile;
import honeycomb.strategy.MovementStrategy;

public class Enemy extends GameCharacter {

    public Enemy(String name, MovementStrategy strategy){
        super(name, strategy);
    }

    @Override
    protected void onArrive(Tile tile){

    }
}
