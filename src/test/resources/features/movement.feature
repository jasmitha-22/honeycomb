
Feature: Exploring the garden maze

  As a player
  I want to move the bee through the garden
  So that I can explore paths and find resources

  Scenario: Moving onto an open path
    Given the bee is at position (2, 2)
    And the tile to the right is an open path
    When the player moves right
    Then the bee moves to position (2, 3)

  Scenario: Being blocked by a wall
    Given the bee is at position (2, 2)
    And the tile to the right is a wall
    When the player moves right
    Then the bee remains at position (2, 2)

  Scenario: Staying within the board
    Given the bee is at the right edge of the board
    When the player moves right
    Then the bee remains within the board

  Scenario: Collecting pollen
    Given the bee is next to a tile containing pollen
    When the bee moves onto that tile
    Then the bee collects the pollen
    And the pollen is no longer available at that location
