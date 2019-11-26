package GO;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Player implements GoPlayer{
    private String playerName;
    private Stone playerStone;
    private boolean registered;
    private boolean receivedStone;

    public Player() {
    }

    public Player (String name, Stone stone){
        playerName = name;
        playerStone = stone;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Stone getPlayerStone() {
        return playerStone;
    }

    public String register(String name) {
        if (!registered){
            this.playerName = name;
            this.registered = true;
            return name;
        }
        return "GO has gone crazy!";
    }

    public boolean receiveStones(Stone stone) {
        if (registered && !receivedStone) {
            this.playerStone = stone;
            this.receivedStone = true;
            return true;
        }
        return false;
    }

     public String makeADumbMove (ArrayList<Board> boards) throws Exception {
         RuleChecker ruleChecker = new RuleChecker();
         if (!ruleChecker.historyCheck(getPlayerStone(), boards)) {
             return ("This history makes no sense!");
         }
         return dumbMove(boards);
     }

     public String makeAMove(ArrayList<Board> boards) throws Exception {
        if (registered && receivedStone) {
            RuleChecker ruleChecker = new RuleChecker();
            try{
                if (!ruleChecker.historyCheck(getPlayerStone(), boards)) {
                    return ("This history makes no sense!");
                }
                return smartMove(boards, 1);
            } catch (Exception e){
                return "GO has gone crazy!";
            }

        }
        return "GO has gone crazy!";
     }

     String dumbMove (ArrayList<Board> boards) throws Exception {
         Board b = new Board();
         int boardSize = b.boardSize();

         RuleChecker ruleChecker = new RuleChecker();
         for (int i = 1; i <= boardSize; i++) {
             for (int j = 1; j <= boardSize; j++) {
                 String pointStr = i + "-" + j;
                 Point currPoint = new Point(pointStr);
                 if ((boolean)ruleChecker.moveCheck(getPlayerStone(),currPoint,boards).get(0)) {
                     return (currPoint.pointToString());
                 }
             }
         }
         return "pass";
     }

     String smartMove (ArrayList<Board> boards, int depth) throws Exception {
         Board b = new Board();
         int boardSize = b.boardSize();

         RuleChecker ruleChecker = new RuleChecker();
         Stone opponentStone = playerStone.opponent();
         Board lastBoard = boards.get(0);
         Point pointToPlace = new Point(100, 100);
         JSONArray opponentPoints = lastBoard.getPoints(opponentStone.toMaybeStone());
         for (int i = 1; i <= boardSize; i++) {
             for (int j = 1; j <= boardSize; j++) {
                 String pointStr = i + "-" + j;
                 Point currPoint = new Point(pointStr);
                 if (lastBoard.board[currPoint.getCol()][currPoint.getRow()].equals(opponentStone.getStone())){
                     List<Point> liberties = ruleChecker.getLiberties(lastBoard, currPoint);
                     if (liberties.size() == depth){
                         Point libertyPoint = liberties.get(0);
                         if ((boolean)ruleChecker.moveCheck(playerStone, libertyPoint, boards).get(0)){
                             if (libertyPoint.isPriorityOver(pointToPlace)){
                                 pointToPlace = libertyPoint;
                             }
                         }
                     }
                 }
//                 Board maybeBoard = ruleChecker.makeMaybeBoard(lastBoard, currPoint, playerStone);
//                 JSONArray maybeOpponentPoint = maybeBoard.getPoints(opponentStone.toMaybeStone());
//                 if (opponentPoints.size() > maybeOpponentPoint.size()) {
//                     return (currPoint.pointToString());
//                 }
             }
         }
         if (pointToPlace.getCol() == 100){
             return dumbMove(boards);
         }
         return pointToPlace.pointToString();
     }

}
