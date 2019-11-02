package GO;

import org.json.simple.JSONArray;

import java.util.ArrayList;

public class Play {
    private String playerName;
    private Stone playerStone;
    RuleChecker ruleChecker = new RuleChecker();

    public String getPlayerName() {
        return playerName;
    }

    public Stone getPlayerStone() {
        return playerStone;
    }

    public String register() {
        this.playerName = "no name";
        return this.playerName;
    }

    public void receiveStones(Stone stone) {
        this.playerStone = stone;
    }

    public String makeADumbMove (ArrayList<Board> boards) throws Exception {
        if (!ruleChecker.historyCheck(getPlayerStone(), boards)) {
            return ("This history makes no sense!");
        }
        return dumbMove(boards);
    }

    public String makeAMove(ArrayList<Board> boards, int distance) throws Exception {
        if (!ruleChecker.historyCheck(getPlayerStone(), boards)) {
            return ("This history makes no sense!");
        }
        return smartMove(boards);
    }

    String dumbMove (ArrayList<Board> boards) throws Exception {
        for (int i = 1; i <= 19; i++) {
            for (int j = 1; j <= 19; j++) {
                String pointStr = i + "-" + j;
                Point currPoint = new Point(pointStr);
                if (ruleChecker.moveCheck(getPlayerStone(),currPoint,boards).getKey()) {
                    return (currPoint.pointToString());
                }
            }
        }
        return "pass";
    }

    String smartMove (ArrayList<Board> boards) throws Exception {
        Stone opponentStone = playerStone.opponent();
        Board lastBoard = boards.get(0);
        JSONArray opponentPoints = lastBoard.getPoints(opponentStone.toMaybeStone());
        for (int i = 1; i <= 19; i++) {
            for (int j = 1; j <= 19; j++) {
                String pointStr = i + "-" + j;
                Point currPoint = new Point(pointStr);
                Board maybeBoard = ruleChecker.makeMaybeBoard(lastBoard, currPoint, playerStone);
                JSONArray maybeOpponentPoint = maybeBoard.getPoints(opponentStone.toMaybeStone());
                if (opponentPoints.size() > maybeOpponentPoint.size()) {
                    return (currPoint.pointToString());
                }
            }
        }
        return dumbMove(boards);
    }

}
