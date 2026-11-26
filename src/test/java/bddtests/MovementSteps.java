package bddtests;

import honeycomb.Board;
import honeycomb.Direction;
import honeycomb.Game;
import honeycomb.GameCharacter;
import honeycomb.Position;
import honeycomb.characters.Bee;
import honeycomb.characters.CharacterFactory;
import honeycomb.strategy.StrategyFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovementSteps {
    private static final int BOARD_SIZE = 5;

    private final StrategyFactory strategyFactory = new StrategyFactory();
    private final CharacterFactory characterFactory = new CharacterFactory(strategyFactory);

    private Board.Builder boardBuilder;
    private Board board;
    private GameCharacter bee;
    private Game game;
    private Position pollenSpot;

    @Given("the bee is at position \\({int}, {int})")
    public void theBeeIsAtPosition(Integer row, Integer col) {
        boardBuilder = Board.getNewBuilder().createGrid(BOARD_SIZE, BOARD_SIZE);
        bee = characterFactory.createBee(Position.at(row, col));
    }

    @Given("the tile to the right is an open path")
    public void theTileToTheRightIsAnOpenPath() {
    }

    @Given("the tile to the right is a wall")
    public void theTileToTheRightIsAWall() {
        Position beePosition = bee.getPosition();
        boardBuilder.createWall(beePosition.getRow(), beePosition.getColumn() + 1);
    }

    @Given("the bee is at the right edge of the board")
    public void theBeeIsAtTheRightEdgeOfTheBoard() {
        boardBuilder = Board.getNewBuilder().createGrid(BOARD_SIZE, BOARD_SIZE);
        bee = characterFactory.createBee(Position.at(2, BOARD_SIZE - 1));
    }

    @Given("the bee is next to a tile containing pollen")
    public void theBeeIsNextToATileContainingPollen() {
        pollenSpot = Position.at(2, 3);
        boardBuilder = Board.getNewBuilder()
                .createGrid(BOARD_SIZE, BOARD_SIZE)
                .addPollen(pollenSpot.getRow(), pollenSpot.getColumn());
        bee = characterFactory.createBee(Position.at(2, 2));
    }

    @When("the player moves right")
    public void thePlayerMovesRight() {
        startGame();
        game.playTurn(Direction.RIGHT);
    }

    @When("the bee moves onto that tile")
    public void theBeeMovesOntoThatTile() {
        startGame();
        game.playTurn(Direction.RIGHT);
    }

    @Then("the bee moves to position \\({int}, {int})")
    public void theBeeMovesToPosition(Integer row, Integer col) {
        assertEquals(Position.at(row, col), bee.getPosition());
    }

    @Then("the bee remains at position \\({int}, {int})")
    public void theBeeRemainsAtPosition(Integer row, Integer col) {
        assertEquals(Position.at(row, col), bee.getPosition());
    }

    @Then("the bee remains within the board")
    public void theBeeRemainsWithinTheBoard() {
        Position beePosition = bee.getPosition();
        assertTrue(board.isWithinBounds(beePosition.getRow(), beePosition.getColumn()));
    }

    @Then("the bee collects the pollen")
    public void theBeeCollectsThePollen() {
        assertEquals(1, assertInstanceOf(Bee.class, bee).getPollenCollected());
    }

    @Then("the pollen is no longer available at that location")
    public void thePollenIsNoLongerAvailableAtThatLocation() {
        assertFalse(board.getTile(pollenSpot.getRow(), pollenSpot.getColumn()).hasPollen());
    }

    private void startGame() {
        board = boardBuilder.build();
        game = new Game(board, bee, List.of(), strategyFactory);
    }
}