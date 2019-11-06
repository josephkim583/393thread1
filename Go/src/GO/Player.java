package GO;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private Stone playerStone;

    public Player() {
        playerName = "no name";
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
        this.playerName = name;
        return name;
    }

    public void receiveStones(Stone stone) {
        this.playerStone = stone;
    }

     public String makeADumbMove (ArrayList<Board> boards) throws Exception {
         RuleChecker ruleChecker = new RuleChecker();
         if (!ruleChecker.historyCheck(getPlayerStone(), boards)) {
             return ("This history makes no sense!");
         }
         return dumbMove(boards);
     }

     public String makeAMove(ArrayList<Board> boards, int distance) throws Exception {
         RuleChecker ruleChecker = new RuleChecker();
         if (!ruleChecker.historyCheck(getPlayerStone(), boards)) {
             return ("This history makes no sense!");
         }
         return smartMove(boards);
     }

     String dumbMove (ArrayList<Board> boards) throws Exception {
         RuleChecker ruleChecker = new RuleChecker();
         for (int i = 1; i <= 19; i++) {
             for (int j = 1; j <= 19; j++) {
                 String pointStr = i + "-" + j;
                 Point currPoint = new Point(pointStr);
                 if ((boolean)ruleChecker.moveCheck(getPlayerStone(),currPoint,boards).get(0)) {
                     return (currPoint.pointToString());
                 }
             }
         }
         return "pass";
     }

     String smartMove (ArrayList<Board> boards) throws Exception {
         RuleChecker ruleChecker = new RuleChecker();
         Stone opponentStone = playerStone.opponent();
         Board lastBoard = boards.get(0);
         Point pointToPlace = new Point(100, 100);
         JSONArray opponentPoints = lastBoard.getPoints(opponentStone.toMaybeStone());
         for (int i = 1; i <= 19; i++) {
             for (int j = 1; j <= 19; j++) {
                 String pointStr = i + "-" + j;
                 Point currPoint = new Point(pointStr);
                 List<Point> liberties = ruleChecker.getLiberties(lastBoard, currPoint);
                 if (liberties.size() == 1){
                     Point libertyPoint = liberties.get(0);
                     if ((boolean)ruleChecker.moveCheck(playerStone, libertyPoint, boards).get(0)){
                        if (libertyPoint.isPriorityOver(pointToPlace)){
                            pointToPlace = libertyPoint;
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
