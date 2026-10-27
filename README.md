# Honeycomb


## About the project

Honeycomb is a maze exploration game we're building in Java using Swing.

The idea starts with a bee whose hive has lost a lot of its honey after a bear raids it. She heads out into a magical garden to find resources and help her hive recover. Along the way, she has to explore unfamiliar paths, deal with creatures, and figure out how to make the most of what she finds.

We want the game to feel whimsical and cozy, but still give the player challenges to think through. Rather than just moving through a maze and collecting items, the goal is to make exploration feel strategic. The choices you make about where to go, which resources to collect, and when to use your abilities should matter.

## What we're building first

For our initial version, we want to focus on the core experience:

- Exploring a maze and deciding which paths to take.
- Finding and collecting resources along the way.
- Using abilities strategically to get around obstacles or outsmart creatures.
- Interacting with creatures and discovering what they can offer.
- Working toward the goal of making it back to the hive with what you've gathered.

We are still figuring out exactly how resources, abilities, and interactions will work together. We want to start with a manageable version of the game and build on it as we go.

## Ideas for the future

As the game develops, we'd like to explore more ways to make the garden feel alive. This could include trading resources with other characters, meeting friends who can help you along the way, solving riddles, discovering hidden paths, and learning new abilities from magical flowers.

We also like the idea of community being part of the gameplay. The bee starts out trying to help her own hive, but she might discover that helping others and making connections can make the journey easier, too.

## Why we're making it

One of the things we find interesting about bees is how much their lives depend on their community. Honeycomb is an opportunity to explore that idea through a game, while also creating something that is fun to play and has room for strategy and discovery.

We're also using the project to get more experience designing an object-oriented program, writing tests, and building a graphical application in Java. We want the different parts of the game to work together in a way that makes sense, rather than adding features just for the sake of adding them.

## Technologies

- Java
- Java Swing
- Maven for building and managing dependencies
- JUnit for testing
- Cucumber and Gherkin for behavior-driven (BDD) tests

## Where we are now

More details about how to build and run the game will be added as development progresses.
Our first playable core focuses on movement, collecting, and creatures. The bee moves through a maze of paths and walls, collecting **pollen** as she goes. Two kinds of creatures share the garden: **hornets**, which hunt the bee, and **wasps**, which wander randomly. Collecting all the pollen wins the level, and getting caught by a creature ends it. Creatures can also be switched into a frightened mode where they run from the bee, which will power our royal jelly ability.

The game logic and tests are in place. The Swing interface is next.

## Running the project

You'll need JDK 17 or newer. Maven is bundled with IntelliJ, so there's nothing extra to install.

- **In IntelliJ:** open the Maven tool window and run **Lifecycle → test**.
- **From a terminal (if Maven is installed):** `mvn clean test`

This runs all the JUnit tests and Cucumber scenarios. The Cucumber HTML report is written to `bdd/report.html`.

## Project structure
- **`honeycomb`**: the game loop, the maze, and shared building blocks (`Game`, `Board`, `Tile`, `TileType`, `Position`, `Direction`, `GameStatus`, `GameCharacter`)
- **`honeycomb.characters`**: the characters and how they're created (`Bee`, `Enemy`, `CharacterFactory`)
- **`honeycomb.strategy`**: how characters decide where to move (`MovementStrategy`, `BeeStrategy`, `HornetStrategy`, `WaspStrategy`, `FrightenedStrategy`, `StrategyFactory`)

## Design patterns

### 1. Builder: `Board.Builder`

**Where:** Building the garden maze. This is the same idea as the Maze Builder we used in Polymorphia: `Board` plays the role of the maze, and each `Tile` is like a room.

**How:** `Board`'s constructor is private, so the only way to make a board is through `Board.getNewBuilder()`, which returns a nested `Builder`. The builder lays out the maze step by step and returns a finished `Board` from `build()`:

```java
Board board = Board.getNewBuilder()
        .createGrid(7, 7)
        .createBorderWalls()
        .createWall(3, 3)
        .addPollen(1, 2)
        .addPollen(5, 4)
        .build();
```

The builder also enforces the rules of a valid maze. It throws an exception if something is placed off the board, if pollen is put on a wall, or if `build()` is called before a grid exists.

**Why:** A maze has many parts that have to be set up in the right order. The builder keeps level creation readable, keeps the validation in one place, and guarantees nobody can end up with a half-built board. As we add new resources and hidden paths, they become new builder methods rather than changes scattered across the code.

### 2. Strategy: `MovementStrategy`

**Where:** Deciding how each character moves.

**How:** `MovementStrategy` is an interface with one method, `chooseMove(current, target, board)`, which returns the `Direction` to move. Every `GameCharacter` holds a strategy and hands the decision off to it in `takeTurn()`:
- **`BeeStrategy`** (the bee): moves to the tile the player chose, unless it's a wall.
- **`HornetStrategy`** (hornets): moves to the open tile closest to the bee.
- **`WaspStrategy`** (wasps): picks a random open tile.
- **`FrightenedStrategy`** (all creatures while frightened, for our planned royal jelly ability): moves to the open tile farthest from the bee.

Strategies can be swapped while the game is running. `Game.frightenEnemies()` gives every creature a `FrightenedStrategy`, and `calmEnemies()` puts each one back to its original behavior.

**Why:** Without this, `GameCharacter` would need an `if/else` for every kind of creature and every mode. With it, a new behavior means writing one new strategy class and one `StrategyFactory` method, and nothing else has to change (the Open/Closed Principle). This matters for us because we expect to add friendly characters and new creature types later.

### 3. Factory: `CharacterFactory`

**Where:** Creating characters and the strategies they use. This follows the `CharacterFactory` structure from Polymorphia.

**How:** `CharacterFactory` has creation methods for each character type: `createBee(...)`, several `createHornet(...)` and `createWasp(...)` overloads, and `createHornets(n)` / `createWasps(n)` for groups. Creatures that aren't given a name get a random one from that type's name list. Every method returns a `GameCharacter`, so the rest of the game works with the abstraction instead of the specific class. The factory gets each character's strategy from a `StrategyFactory`, which is passed into its constructor.

**Why:** The rest of the code just asks for "a hornet" without needing to know how one is put together. The factories are the only place that knows a hornet chases and a wasp wanders, so changing a character's behavior means changing one line.

### How the patterns work together

The **Builder** creates the maze. The **Character Factory** creates the characters and, through the `StrategyFactory`, gives each one the right **Strategy**. `Game` receives the finished board and characters and runs the turns.

## OO principles

### Coding to abstractions

- `Game` stores the bee and the creatures as `GameCharacter`, never as `Bee` or `Enemy`.
- `CharacterFactory` returns `GameCharacter`, and `StrategyFactory` returns `MovementStrategy`.
- `GameCharacter` depends only on the `MovementStrategy` interface, not on any specific strategy.

### Polymorphism

We have two examples:

1. **`Strategies`.** `Game` calls `takeTurn()` the same way on every character, but each one moves differently depending on which strategy it holds. The same creature also behaves differently before and after `frightenEnemies()`.
2. **`onArrive()`.** `GameCharacter.moveTo()` always calls the abstract method `onArrive(tile)` after a move. A `Bee` collects pollen there, while an `Enemy` does nothing. `GameCharacter` never checks which kind it is.

### Dependency injection

- Each `GameCharacter` receives its `MovementStrategy` through its constructor.
- `CharacterFactory` receives its `StrategyFactory` through its constructor.
- `StrategyFactory` can receive a `java.util.Random`, so tests can pass in a seeded `Random` and get the same results every run.
- `Game` receives its `Board`, bee, creatures, and `StrategyFactory` instead of creating them itself.

## How a turn works

`Game.playTurn(direction)` runs one turn:

1. If the game is already won or lost, do nothing.
2. Move the bee toward the tile the player chose.
3. Check for a win or loss. Getting caught is checked before winning, so being caught beats grabbing the last pollen.
4. Move every creature, each using its own strategy.
5. Check again, in case a creature moved onto the bee.

The player wins when no pollen is left on the board.

## Tests

### JUnit (`src/test/java/honeycomb/`)

- **`BoardTest`** (8 tests): the Builder, including open grids, border walls, single walls, pollen counting, off-board checks, and rejecting invalid mazes.
- **`BeeTest`** (5 tests): collecting pollen, landing on empty tiles, walls, board edges, and not jumping more than one tile.
- **`GameTest`** (6 tests): winning, losing to a hornet, walls blocking the bee, creatures not collecting pollen, frightening and calming creatures, and rejecting invalid starting positions.
- **`CharacterFactoryTest`** (2 tests): each character gets the right class and strategy, and group creation returns the right count and names.

### Cucumber (`src/test/resources/features/movement.feature`)

Four scenarios describe the bee's movement from the player's point of view: moving onto an open path, being blocked by a wall, staying inside the board, and collecting pollen. The step definitions are in `src/test/java/bddtests/MovementSteps.java`.

## Next steps

- A Swing interface that draws the garden and reads keyboard input
- Royal jelly tiles that frighten creatures automatically, with a timer that calms them again
- Returning to the hive as the goal, instead of only collecting all the pollen
- More resources and abilities, building toward the ideas above
