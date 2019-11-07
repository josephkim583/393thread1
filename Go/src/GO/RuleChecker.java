package GO;

import java.util.*;
import java.lang.*;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RuleChecker {

    public ArrayList<Object> moveCheck(Stone stone, Point point, ArrayList<Board> boards) throws Exception {
        Board newestBoard = boards.get(0);
        Board oldestBoard = boards.get(boards.size() - 1);
        Board maybeBoard = makeMaybeBoard(newestBoard, point, stone);

        // Can't place if point occupied
        if (newestBoard.occupied(point)) {
            ArrayList moveCheck = new ArrayList();
            moveCheck.add(false);
            moveCheck.add(maybeBoard);
            return moveCheck;
        }

        // Can't place if suicide
        if (!maybeBoard.occupies(stone, point)) {
            ArrayList moveCheck = new ArrayList();
            moveCheck.add(false);
            moveCheck.add(maybeBoard);
            return moveCheck;
        }

        int boardSize = boards.size();

        if (boardSize == 3) {
            Board board2 = boards.get(1);
            // Can't place if super-ko
            if (board2.isBoardEqual(maybeBoard)) {
                ArrayList moveCheck = new ArrayList();
                moveCheck.add(false);
                moveCheck.add(maybeBoard);
                return moveCheck;
            }
        }
        ArrayList moveCheck = new ArrayList();
        moveCheck.add(true);
        moveCheck.add(maybeBoard);
        return moveCheck;
    }

    public boolean historyCheck(Stone stone, ArrayList<Board> boards) throws Exception {
        int boardSize = boards.size();

        // Checking the new Move
        Board newestBoard = boards.get(0);
        Board oldestBoard = boards.get(boardSize - 1);

        // All captured stones are removed
        for (Board b : boards) {
            if (!allPointsHaveLiberties(b)) {
                return false;
            }
        }
        // Check the history
        switch (boardSize) {
            case 1: {
                if (stone.getStone().equals("W")) {
                    return false;
                }
                if (!isEmptyBoard(oldestBoard)) {
                    return false;
                }
                break;
            }
            case 2: {
                if (stone.getStone().equals("B")) {
                    return false;
                }
                ArrayList<Point> added = addedPoints(oldestBoard, newestBoard);
                MaybeStone addedStone = getTurn(added, oldestBoard, newestBoard);
                // More than one point added
                if (added.size() > 1) {
                    return false;
                }
                if (!isEmptyBoard(oldestBoard)) {
                    return false;
                }
                if (addedStone.getMaybeStone().equals("W")) {
                    return false;
                }
                break;
            }
            case 3: {
                Board board1 = boards.get(0); // Most recent board
                Board board2 = boards.get(1);
                Board board3 = boards.get(2); // Oldest board

                ArrayList<Point> addedNew = addedPoints(board2, board1);
                ArrayList<Point> addedOld = addedPoints(board3, board2);

                // More than one point added
                if (addedOld.size() > 1 || addedNew.size() > 1) {
                    return false;
                } else {
                    String addedStoneNew = getTurn(addedNew, board2, board1).getMaybeStone();
                    String addedStoneOld = getTurn(addedOld, board3, board2).getMaybeStone();

                    if (isEmptyBoard(board3) && isEmptyBoard(board2) && addedStoneNew.equals("B")) {
                        return false;
                    }

                    if (!addedStoneNew.equals(" ") && !addedStoneOld.equals(" ")) {
                        // Check if alternating
                        if (addedStoneNew.equals(addedStoneOld)) {
                            return false;
                        }
                        if (addedStoneNew.equals(stone.getStone())) {
                            return false;
                        }
                        Board MaybeBoard2 = makeMaybeBoard(board3, addedOld.get(0), new Stone(addedStoneOld));
                        Board MaybeBoard1 = makeMaybeBoard(board2, addedNew.get(0), new Stone(addedStoneNew));
                        if (!(MaybeBoard1.isBoardEqual(board1) && MaybeBoard2.isBoardEqual(board2))) {
                            return false;
                        }
                    } else {
                        // Passed from board 2 to board 1
                        if (addedStoneNew.equals(" ") && !addedStoneOld.equals(" ")) {
                            if (!board2.isBoardEqual(board1)) {
                                return false;
                            }
                            if (!addedStoneOld.equals(stone.getStone())) {
                                return false;
                            }
                            // Passed from board 3 to board 2
                        } else if (addedStoneOld.equals(" ") && !addedStoneNew.equals(" ")) {
                            if (!board3.isBoardEqual(board2)) {
                                return false;
                            }
                            if (addedStoneNew.equals(stone.getStone())) {
                                return false;
                            }
                        } else {
                            if (!(board3.isBoardEqual(board2) && board2.isBoardEqual(board1))) {
                                return false;
                            }
                        }
                    }
                }

                if (board1.isBoardEqual(board3)) {
                    return false;
                }
                break;
            }
            default: {
                System.out.print("Boards size is weird");
            }
        }
        return true;
    }

    public boolean pass() {
        return true;
    }

    public JSONObject getScore(Board board) throws Exception {
        int blackScore = 0;
        int whiteScore = 0;
        for (int i = 1; i <= 19; i++) {
            for (int j = 1; j <= 19; j++) {
                String pointStr = i + "-" + j;
                Point currPoint = new Point(pointStr);
                if (board.getPointValue(currPoint).equals(" ")) {
                    boolean blackReachable = board.reachable(currPoint, new MaybeStone("B"));
                    boolean whiteReachable = board.reachable(currPoint, new MaybeStone("W"));
                    if (blackReachable && whiteReachable) continue;
                    if (blackReachable) {
                        blackScore += 1;
                    }
                    if (whiteReachable) {
                        whiteScore += 1;
                    }
                } else if (board.getPointValue(currPoint).equals("B")) {
                    blackScore += 1;
                } else {
                    whiteScore += 1;
                }
            }
        }
        JSONObject score = new JSONObject();
        score.put("W", whiteScore);
        score.put("B", blackScore);

        return score;
    }

    boolean allPointsHaveLiberties(Board board) throws Exception {
        for (int i = 1; i <= 19; i++) {
            for (int j = 1; j <= 19; j++) {
                String pointStr = i + "-" + j;
                Point currPoint = new Point(pointStr);
                if (!hasLiberty(board, currPoint)) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean isEmptyBoard(Board board) {
        for (int i = 1; i <= 19; i++) {
            for (int j = 1; j <= 19; j++) {
                String pointStr = i + "-" + j;
                Point currPoint = new Point(pointStr);
                if (!board.getPointValue(currPoint).equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean isNotEmptyBoard(Board board) {
        return !isEmptyBoard(board);
    }

    boolean hasLiberty(Board board, Point point) throws Exception {
        return board.reachable(point, new MaybeStone(" "));
    }

    List<Point> getLiberties(Board board, Point point) {
        int col = point.getCol();
        int row = point.getRow();
        String originalColor = board.board[col][row];
        HashMap<String, Boolean> visited = new HashMap<String, Boolean>();
        ArrayList<Point> queue = new ArrayList<>();
        List<Point> liberties = new ArrayList<>();

        //If at the point is an empty space on the board
        if (board.board[col][row].equals(" ")){
            return liberties;
        }

        queue.add(point);
        while (queue.size() > 0){
            Point pop = queue.get(0);
            queue.remove(0);
            visited.put(pop.pointToString(), true);
            ArrayList<Point> neighbors = pop.getNeighbors();

            for (Point neighborPoint : neighbors){
                if (!visited.containsKey(neighborPoint.pointToString())){
                    String stoneAtPoint = board.board[neighborPoint.getCol()][neighborPoint.getRow()];
                    if (stoneAtPoint.equals(" ")){
                        if (!containsPoint(liberties, neighborPoint)){
                            liberties.add(neighborPoint);
                        }
                    } else if (stoneAtPoint.equals(originalColor)){
                        if (!queue.contains(neighborPoint)){
                            queue.add(neighborPoint);
                        }
                    }
                }
            }
        }
        return liberties;
    }

    boolean containsPoint(List<Point> list, Point point){
        return list.stream().filter(o -> o.pointToString().equals(point.pointToString())).findFirst().isPresent();
    }

    // Returns ArrayList of newly added points (i.e. not in the old board but in the new board)
    // Can
    // 1. Check if there has been only one newly added point
    // 2. Used to check whose turn
    ArrayList<Point> addedPoints(Board oldBoard, Board newBoard) {
        ArrayList<Point> added = new ArrayList<Point>();
        for (int i = 1; i <= 19; i++) {
            for (int j = 1; j <= 19; j++) {
                String pointStr = i + "-" + j;
                Point currPoint = new Point(pointStr);
                if (oldBoard.getPointValue(currPoint).equals(" ") && !newBoard.getPointValue(currPoint).equals(" ")) {
                    added.add(currPoint);
                }
            }
        }
        return added;
    }

    MaybeStone getTurn(ArrayList<Point> addedPoints, Board oldBoard, Board newBoard) throws IOException {
        if (addedPoints.size() == 0) {
            return new MaybeStone(" ");
        } else {
            String newStone = newBoard.getPointValue(addedPoints.get(0));
            return new MaybeStone(newStone);
        }
    }

    Board makeMaybeBoard(Board board, Point point, Stone stone) throws Exception {
        Board maybeBoard = new Board(board);
        maybeBoard.place(stone, point);
        Board maybeBoardRemoveEnemy = new Board(maybeBoard);

        Board maybeBoardAfterRemoving = new Board(maybeBoardRemoveEnemy);
        for (int i = 1; i <= 19; i++) {
            for (int j = 1; j <= 19; j++) {
                String pointStr = i + "-" + j;
                Point currPoint = new Point(pointStr);
                if (!hasLiberty(maybeBoardRemoveEnemy, currPoint)) {
                    Stone currStone = new Stone(maybeBoard.getPointValue(currPoint));
                    maybeBoardAfterRemoving.remove(currStone, currPoint);
                }
            }
        }
        return maybeBoardAfterRemoving;
    }

}
