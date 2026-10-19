package honeycomb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BeeTest {


    @Test
    public void beeCollectsPollenWhenMovingOntoTile() {
        Tile[][] tiles = {
                {new Tile(TileType.PATH), new Tile(TileType.PATH)}
        };

        Board board = new Board(tiles);
        Position start = new Position(0, 0);
        Bee bee = new Bee(start);

        tiles[0][1].addPollen();

        boolean moved = bee.move(0, 1, board);

        assertTrue(moved);
        assertEquals(new Position(0, 1), bee.getPosition());
        assertEquals(1, bee.getPollenCollected());
        assertFalse(tiles[0][1].hasPollen());
    }
}
