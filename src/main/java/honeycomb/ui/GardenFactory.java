package honeycomb.ui;

import honeycomb.Board;
import honeycomb.Game;
import honeycomb.GameCharacter;
import honeycomb.Position;
import honeycomb.characters.CharacterFactory;
import honeycomb.strategy.StrategyFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// sets up one round. Game still handles what happens after the player makes a move.
final class GardenFactory {

    // both enemies use the same Enemy class. keeping their references lets the
    // panel draw the right image for each one, even if its movement strategy changes.
    record Round(Game game, GameCharacter hornet, GameCharacter wasp) {}

    static Round create(long seed) {
        // starting with the same seed repeats the same random choices on restart.
        Random random = new Random(seed);
        int rows = 7;
        int columns = 9;

        Position beeStart = Position.at(0, 0);
        Position hornetStart = Position.at(rows - 1, columns - 1);
        Position waspStart = Position.at(0, columns - 1);

        Board.Builder builder = Board.getNewBuilder().createGrid(rows, columns);
        List<Position> flowerSpots = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Position spot = Position.at(row, col);

                // walls can only go where both coordinates are odd. this leaves
                // connected paths along every even row and column.
                if (row % 2 == 1 && col % 2 == 1 && random.nextBoolean()) {
                    builder.createWall(row, col);
                } else if (!spot.equals(beeStart)
                        && !spot.equals(hornetStart)
                        && !spot.equals(waspStart)) {
                    flowerSpots.add(spot);
                }
            }
        }

        // shuffle the open spots and take the first four. this keeps us from
        // putting two flowers on the same tile or one under a starting character.
        Collections.shuffle(flowerSpots, random);
        for (Position flower : flowerSpots.subList(0, 4)) {
            builder.addPollen(flower.getRow(), flower.getColumn());
        }

        // constructor injection.
        StrategyFactory strategies = new StrategyFactory(random);
        CharacterFactory characters = new CharacterFactory(strategies);

        GameCharacter bee = characters.createBee(beeStart);
        GameCharacter hornet = characters.createHornet("Stinger", hornetStart);
        GameCharacter wasp = characters.createWasp("Zippy", waspStart);

        Game game = new Game(builder.build(), bee, List.of(hornet, wasp), strategies);
        return new Round(game, hornet, wasp);
    }
}