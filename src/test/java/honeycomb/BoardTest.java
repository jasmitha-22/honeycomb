package honeycomb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardTest {
    @Test
    public void newGridIsAllWalkable(){
        Board board = Board.getNewBuilder()
                .createGrid(3, 3)
                .build();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertTrue(board.isWalkable(row, col));
            }
        }
    }

    @Test
    public void borderWallsSurroundTheBoardButLeaveTheMiddleOpen() {
        Board board = Board.getNewBuilder()
                .createGrid(3, 3)
                .createBorderWalls()
                .build();

        assertFalse(board.isWalkable(0, 0));
        assertFalse(board.isWalkable(0, 1));
        assertFalse(board.isWalkable(2, 2));
        assertTrue(board.isWalkable(1, 1));
    }

    @Test
    public void createWallBlocksThatTile() {
        Board board = Board.getNewBuilder()
                .createGrid(3, 3)
                .createWall(1, 1)
                .build();

        assertFalse(board.canMoveTo(new Position(1, 1)));
        assertTrue(board.canMoveTo(new Position(1, 2)));
    }

    @Test
    public void countPollenCountsEveryPollenTile() {
        Board board = Board.getNewBuilder()
                .createGrid(3, 3)
                .addPollen(0, 0)
                .addPollen(1, 2)
                .addPollen(2, 1)
                .build();

        assertEquals(3, board.countPollen());
    }

    @Test
    public void canMoveToIsFalseOffTheBoard() {
        Board board = Board.getNewBuilder()
                .createGrid(2, 2)
                .build();

        assertFalse(board.canMoveTo(new Position(-1, 0)));
        assertFalse(board.canMoveTo(new Position(0, 2)));
    }

    @Test
    public void pollenCannotBePlacedOnAWall() {
        Board.Builder builder = Board.getNewBuilder()
                .createGrid(2, 2)
                .createWall(0, 0);

        assertThrows(IllegalArgumentException.class, () -> builder.addPollen(0, 0));
    }

    @Test
    public void placingOffTheBoardIsRejected() {
        Board.Builder builder = Board.getNewBuilder().createGrid(2, 2);

        assertThrows(IllegalArgumentException.class, () -> builder.createWall(5, 5));
    }

    @Test
    public void buildingWithoutAGridIsRejected() {
        assertThrows(IllegalStateException.class, () -> Board.getNewBuilder().build());
    }
}
