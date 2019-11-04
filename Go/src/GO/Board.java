package GO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.Collections;

public class Board implements Statement {
    // Creates a new 2D array Board
    String[][] board = new String[19][19];

    String dirN = "North";
    String dirE = "East";
    String dirS = "South";
    String dirW = "West";

    public Board(String[][] newBoard) {
        super();
        if (newBoard.length == 19 && newBoard[0].length == 19) {
            this.board = newBoard;
        }
    }

    public Board(Board copyBoard) {
        this.board = new String[19][19];
        String[][] ref = copyBoard.getBoard();
        for (int i = 0 ; i < 19 ; i++) {
            for (int j = 0; j < 19 ; j++) {
                board[i][j] = ref[i][j];
            }
        }
    }

    public Board() {
        this.board = new String[19][19];
        for (int i = 0 ; i < 19 ; i++) {
            for (int j = 0; j < 19 ; j++) {
                board[i][j] = " ";
            }
        }
    }

    public String[][] getBoard() {
        return board;
    }


    public String getPointValue(Point point) {
        return board[point.getCol()][point.getRow()];
    }

    // Occupied
    // Return "true" if the Board has a Stone at the given Point or "false" otherwise
    @Override
    public boolean occupied(Point point) throws Exception {
        if (point.isValid()) {
            int col = point.getCol();
            int row = point.getRow();
            if (board[col][row].equals("B") || board[col][row].equals("W")) {
                return true;
            }
            return false;
        } else {
            throw new Exception("Not a valid point! - from Occupied");
        }
    }

    // Occupies
    // Return "true" if the given Stone appears on the Board at the given Point or "false" otherwise
    @Override
    public boolean occupies(Stone stone, Point point) throws Exception {
        if (point.isValid()) {
            int col = point.getCol();
            int row = point.getRow();
            if (this.board[col][row].equals(stone.getStone())) {
                return true;
            }
            return false;
        } else {
            throw new Exception("Not a valid point! - from Occupies");
        }
    }

    @Override
    public boolean reachable(Point point, MaybeStone stone) throws Exception {
        boolean[][] trackBoard = new boolean[19][19];
        for (boolean[] row : trackBoard) {
            for (boolean cell : row) {
                cell = false;
            }
        }

        if (point.isValid()) {
            String currStone = getPointValue(point);

            if (currStone.equals(stone.getMaybeStone())) {
                return true;
            } else {
                boolean isFound = false;

               return (reachable_recur(point.North(), stone, isFound, dirN, point, trackBoard) ||
                        reachable_recur(point.East(), stone, isFound, dirE, point, trackBoard) ||
                        reachable_recur(point.South(), stone, isFound, dirS, point, trackBoard) ||
                        reachable_recur(point.West(), stone, isFound, dirW, point, trackBoard));
            }
        } else {
            throw new Exception("Not a valid point! - from reachable");
        }
    }

    private boolean reachable_recur(Point nextPoint/*targetpoint*/, MaybeStone targetStone, boolean isFound, String dir, Point currPoint /*actualOrigPoint*/, boolean[][] trackBoard) {
        trackBoard[currPoint.getCol()][currPoint.getRow()] = true;

        if (!nextPoint.isValid()) {
            return false;
        }

        // not visited
        if (!trackBoard[nextPoint.getCol()][nextPoint.getRow()]) {
            // check if next is target
            if (getPointValue(nextPoint).equals(targetStone.getMaybeStone())) {
                return true;
            }
            // is it worth recursion? - only when the nextValue is same as currValue
            if ((getPointValue(nextPoint).equals(getPointValue(currPoint)))) {
                return (reachable_recur(nextPoint.North(), targetStone, isFound, dirN, nextPoint, trackBoard) ||
                        reachable_recur(nextPoint.East(), targetStone, isFound, dirE, nextPoint, trackBoard) ||
                        reachable_recur(nextPoint.South(), targetStone, isFound, dirS, nextPoint, trackBoard) ||
                        reachable_recur(nextPoint.West(), targetStone, isFound, dirW, nextPoint, trackBoard));
            }
        }
        return false;
    }

    // Place
    // Return a Board that reflects placing the given Stone on the given Point if it is possible to do so,
    // Else return the message "This seat is taken!"
    @Override
    public Object place(Stone stone, Point point) throws Exception {
        int col = point.getCol();
        int row = point.getRow();
        if (occupied(point)) {
            return "This seat is taken!";
        } else {
            this.board[col][row] = stone.getStone();
            JSONArray output = printBoard();
            return output;
        }
    }

    // Remove
    // Return a Board that reflects removing Stone from the given Point on the Board if it is possible to do so,
    // Else it should return the message "I am just a board! I cannot remove what is not there!"
    @Override
    public Object remove(Stone stone, Point point) throws Exception {
        int col = point.getCol();
        int row = point.getRow();
        if (!occupies(stone, point)){
            return "I am just a board! I cannot remove what is not there!";
        } else {
            this.board[col][row] = " ";
            JSONArray output = printBoard();
            return output;
        }
    }

     // Get-points
    //  return a JSON array of Points that collects all the Points on the Board that have the appropriate stone
    //  or are empty depending on the given MaybeStone
    @Override
    public JSONArray getPoints(MaybeStone stone){
        List<String> points = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            for(int j = 0; j < 19; j++) {
                try {
                    Point temp = new Point(i+1,j+1);
                    if (this.board[i][j].equals(stone.getMaybeStone())) {
                        String iString = Integer.toString(i+1);
                        String jString = Integer.toString(j+1);
                        String pointObj = (jString+"-"+iString);
                        points.add(pointObj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Collections.sort(points);
        JSONArray output = new JSONArray();
        for (Object o : points) {
            output.add(o);
        }
        return output;
    }

    public JSONArray printBoard() {
        JSONArray output = new JSONArray();
        for (int i = 0; i < 19; i++) {
            JSONArray temp = new JSONArray();
            for (int j = 0; j < 19; j++){
                temp.add(this.board[i][j]);
            }
            output.add(temp);
        }
        return output;
    }

    public boolean isBoardEqual(Board board2) {
        for (int i = 1; i <= 19; i++) {
            for (int j = 1; j <= 19; j++) {
                String pointStr = i + "-" + j;
                Point currPoint = new Point(pointStr);
                if (!this.getPointValue(currPoint).equals(board2.getPointValue(currPoint))) {
                    return false;
                }
            }
        }
        return true;
    }


}
