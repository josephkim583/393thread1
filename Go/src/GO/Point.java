package GO;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Point {
    private int col;
    private int row;
    private boolean valid;
    private int boardSize = (new Board()).boardSize();

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
        valid = 0 < r && r < boardSize + 1 && 0 < c && c < boardSize + 1;
    }

    public Point(int c, int r) {
        this.col = c;
        this.row = r;
        valid = 0 <= r && r <= boardSize - 1 && 0 <= c && c <= boardSize - 1;
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

    public ArrayList<Point> getNeighbors() {
        ArrayList<Point> neighbors = new ArrayList<>();
        if (this.North().isValid()){
            neighbors.add(this.North());
        }
        if (this.East().isValid()){
            neighbors.add(this.East());
        }
        if (this.South().isValid()){
            neighbors.add(this.South());
        }
        if (this.West().isValid()){
            neighbors.add(this.West());
        }
        return neighbors;
    }

    public String pointToString() {
        String pointString = (row+1)+"-"+(col+1);
        return  pointString;
    }

    public Boolean isPriorityOver(Point anotherpoint){
        if (this.row < anotherpoint.row){
            return true;
        } else if (anotherpoint.row < this.row){
            return false;
        } else {
            if (this.col < anotherpoint.col){
                return true;
            } else return anotherpoint.col >= this.col;
        }
    }
}
