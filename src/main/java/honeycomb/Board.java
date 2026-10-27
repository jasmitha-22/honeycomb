package honeycomb;

import java.lang.module.ModuleDescriptor;

public class Board {
    private final Tile[][] tiles;

    public Board(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public static Builder getNewBuilder() {
        return new Builder();
    }

    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < tiles.length
                && col >= 0 && col < tiles[row].length;
    }

    public boolean isWalkable(int row, int col) {
        return isWithinBounds(row, col)
                && tiles[row][col].isWalkable();
    }

    public Tile getTile(int row, int col) {
        if (!isWithinBounds(row, col)) {
            throw new IndexOutOfBoundsException("Position is outside the board");
        }

        return tiles[row][col];
    }

    public boolean canMoveTo(Position position) {
        return isWalkable(position.getRow(), position.getColumn());
    }

    public int countPollen(){
        int count = 0;
        for(Tile[] row : tiles){
            for(Tile tile : row){
                if(tile.hasPollen()){
                    count++;
                }
            }
        }
        return count;
    }

    public static class Builder {
        private Tile[][] tiles;

        private Builder(){
        }

        public Builder createGrid(int rows, int cols){
            if(rows <= 0 || cols <= 0){
                throw new IllegalArgumentException("Grid must be at least 1x1");
            }
            tiles = new Tile[rows][cols];
            for(int row = 0; row < rows; row++){
                for(int col = 0; col < cols; col++){
                    tiles[row][col] = new Tile(TileType.PATH);
                }
            }
            return this;
        }


        public Builder createBorderWalls(){
            requireGrid();
            int lastRow = tiles.length - 1;
            int lastCol = tiles[0].length - 1;
            for(int row = 0; row <= lastRow; row++){
                createWall(row, 0);
                createWall(row, lastCol);
            }
            for(int col = 0; col <= lastCol; col++){
                createWall(0, col);
                createWall(lastRow, col);
            }
            return this;
        }

        public Builder createWall(int row, int col){
            requireInBounds(row, col);
            tiles[row][col] = new Tile(TileType.WALL);
            return this;
        }

        public Builder addPollen(int row, int col){
            requireInBounds(row, col);
            if(!tiles[row][col].isWalkable()){
                throw new IllegalArgumentException("Can't put pollen on wall");
            }
            tiles[row][col].addPollen();
            return this;
        }

        public Board build(){
            requireGrid();
            return new Board(tiles);
        }

        private void requireGrid(){
            if(tiles == null){
                throw new IllegalStateException("Call createGrid(...) first");
            }
        }

        private void requireInBounds(int row, int col){
            requireGrid();
            if(row <0 || row >= tiles.length || col < 0 || col >= tiles[0].length){
                throw new IllegalArgumentException("(" + row + ", " + col + ") is off the board");
            }
        }
    }
}
