package GO;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Point {
    private int col;
    private int row;
    private boolean valid;

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public boolean isValid() {
        return valid;
    }

    // Constructor
    // Takes string type of "N-N" and constructs a point
    // 1- 19 valid
    public Point(@NotNull String str) {
        String[] rcArray = str.split("-");
        int r = Integer.parseInt(rcArray[0]);
        int c = Integer.parseInt(rcArray[1]);
        row = r - 1;
        col = c - 1;
        valid = 0 < r && r < 20 && 0 < c && c < 20;
    }

    public Point(int c, int r) {
        this.col = c;
        this.row = r;
        valid = 0 <= r && r <= 18 && 0 <= c && c <= 18;
    }

    public Point North() {
        Point north = new Point(col, row-1);
        return north;
    }

    public Point East() {
        Point east = new Point(col + 1, row);
        return east;
    }

    public Point South() {
        Point south = new Point(col, row+1);
        return south;
    }

    public Point West() {
        Point west = new Point(col-1, row);
        return west;
    }

    public String pointToString() {
        String pointString = (row+1)+"-"+(col+1);
        return  pointString;
    }
}
