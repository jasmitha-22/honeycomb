package honeycomb.strategy;


import java.util.Random;

public class StrategyFactory {
    private final Random random;

    public StrategyFactory(){
        this(new Random());
    }

    public StrategyFactory(Random random) {
        this.random = random;
    }

    public MovementStrategy newBeeStrategy() {return new BeeStrategy(); }

    public MovementStrategy newWaspStrategy() { return new WaspStrategy(random); }

    public MovementStrategy newHornetStrategy(){ return new HornetStrategy(); }

    public MovementStrategy newFrightenedStrategy() { return new FrightenedStrategy(); }

}
