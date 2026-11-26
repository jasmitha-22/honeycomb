package honeycomb.characters;

import honeycomb.GameCharacter;
import honeycomb.Position;
import honeycomb.strategy.StrategyFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class CharacterFactory {
    static final Random random = new Random();

    public static String[] HORNET_NAMES = new String[]{"Stinger", "Buzzkill", "Buzz LightSting", "Ha-Ha Hornet"};
    public static String[] WASP_NAMES = new String[]{"Waspinator", "Lord of the Sting", "StingZilla", "Waspy McWaspface"};
    public static String[] BEE_NAMES = new String[]{"Beeyonce", "Cardi Bee", "Bumbledore", "Barry"};
    public static Map<String, String[]> NAMES = new HashMap<>();

    private final StrategyFactory strategyFactory;

    static {
        NAMES.put("Hornet", HORNET_NAMES);
        NAMES.put("Wasp", WASP_NAMES);
        NAMES.put("Bee", BEE_NAMES);
    }

    public CharacterFactory(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public GameCharacter createBee(Position startingPosition) {
        GameCharacter bee =  new Bee("Bee", strategyFactory.newBeeStrategy());
        bee.setPosition(startingPosition);
        return bee;
    }

    public GameCharacter createHornet() {
        return createHornet(getRandomName("Hornet"));
    }

    public GameCharacter createHornet(String name) {
        return new Enemy(name, strategyFactory.newHornetStrategy());
    }

    public GameCharacter createHornet(Position startingPosition) {
        GameCharacter hornet = createHornet();
        hornet.setPosition(startingPosition);
        return hornet;
    }

    public GameCharacter createHornet(String name, Position startingPosition) {
        GameCharacter hornet = createHornet(name);
        hornet.setPosition(startingPosition);
        return hornet;
    }

    public GameCharacter createWasp() {
        return createWasp(getRandomName("Wasp"));
    }

    public GameCharacter createWasp(String name) {
        return new Enemy(name, strategyFactory.newWaspStrategy());
    }

    public GameCharacter createWasp(Position startingPosition) {
        GameCharacter wasp = createWasp();
        wasp.setPosition(startingPosition);
        return wasp;
    }

    public GameCharacter createWasp(String name, Position startingPosition) {
        GameCharacter wasp = createWasp(name);
        wasp.setPosition(startingPosition);
        return wasp;
    }

    public List<GameCharacter> createHornets(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
                .mapToObj(_ -> createHornet())
                .toList();
    }

    public List<GameCharacter> createWasps(int numberOfCharacters) {
        return IntStream.range(0, numberOfCharacters)
                .mapToObj(_ -> createWasp())
                .toList();
    }

    private String getRandomName(String characterType) {
        String[] names = NAMES.get(characterType);
        return names[random.nextInt(names.length)];
    }


}
