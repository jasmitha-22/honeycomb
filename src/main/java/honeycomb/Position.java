package honeycomb;

public class Position {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position other = (Position) o;
        return row == other.row && column== other.column;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column+ ")";
    }
}
