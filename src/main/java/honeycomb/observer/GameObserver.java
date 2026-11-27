package honeycomb.observer;

import honeycomb.Game;

@FunctionalInterface
//functional interface means we can only have one abstract method
public interface GameObserver {
    void onTurnFinished(Game game);
}
