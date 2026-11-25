package honeycomb;

import honeycomb.characters.Bee;
import honeycomb.characters.CharacterFactory;
import honeycomb.characters.Enemy;
import honeycomb.strategy.BeeStrategy;
import honeycomb.strategy.HornetStrategy;
import honeycomb.strategy.StrategyFactory;
import honeycomb.strategy.WaspStrategy;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterFactoryTest {
    private final CharacterFactory characterFactory = new CharacterFactory(new StrategyFactory(new Random(1)));

    @Test
    public void eachCharacterGetsTheRightTypeAndStrategy() {
        GameCharacter bee = characterFactory.createBee(new Position(0, 0));
        GameCharacter hornet = characterFactory.createHornet("Stinger");
        GameCharacter wasp = characterFactory.createWasp("Zippy");

        assertInstanceOf(Bee.class, bee);
        assertInstanceOf(BeeStrategy.class, bee.getStrategy());
        assertInstanceOf(Enemy.class, hornet);
        assertInstanceOf(HornetStrategy.class, hornet.getStrategy());
        assertInstanceOf(WaspStrategy.class, wasp.getStrategy());
        assertEquals("Stinger", hornet.getName());
    }

    @Test
    public void createWaspsMakesTheRequestedNumberWithWaspNames() {
        List<GameCharacter> wasps = characterFactory.createWasps(3);

        assertEquals(3, wasps.size());
        for (GameCharacter wasp : wasps) {
            assertInstanceOf(WaspStrategy.class, wasp.getStrategy());
            assertTrue(Arrays.asList(CharacterFactory.WASP_NAMES).contains(wasp.getName()));
        }
    }
}
