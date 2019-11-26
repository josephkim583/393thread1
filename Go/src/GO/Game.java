package GO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Game implements GameInterface {
    GoPlayer playerOne;
    GoPlayer playerTwo;
    String currentStoneColor = "B";
    int consecutivePass = 0;
    boolean gameEnded = false;
    ArrayList<Board> boardHistory = new ArrayList<>();
    RuleChecker ruleChecker = new RuleChecker();
    JSONArray gameLog = new JSONArray();
    HashMap<String, GoPlayer> gameResult = new HashMap<>();

    public Game(){
        Board emptyBoard = new Board();
        boardHistory.add(emptyBoard);
    }

    public HashMap<String, GoPlayer> getGameResult() {
        return gameResult;
    }

    public JSONArray getGameLog() {
        return gameLog;
    }

    public void registerPlayer(GoPlayer playerOne, GoPlayer playerTwo) throws IOException {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        //register playerOne
        try {
            this.playerOne.register("localPlayer");
        }
        catch (Exception e){
            this.playerOne = new Player();
            playerOne.register("localPlayer");
        }

        //register playerTwo
        try {
            this.playerTwo.register("localPlayer");
        }
        catch (Exception e){
            this.playerTwo = new Player();
            playerTwo.register("localPlayer");
        }

        //receive stone playerOne
        try{
            Stone blackStone = new Stone("B");
            this.playerOne.receiveStones(blackStone);
        }
        catch (Exception e){
            gameEnded = true;
            gameResult.put("winner", playerTwo);
            gameResult.put("loser", playerOne);
        }

        //receive stone playerTwo
        try{
            Stone whiteStone = new Stone("W");
            this.playerTwo.receiveStones(whiteStone);
        }
        catch (Exception e){
            gameEnded = true;
            gameResult.put("winner", playerOne);
            gameResult.put("loser", playerTwo);
        }
        // TODO: GO has gone crazy case
    }

    public void receiveStone(GoPlayer player) {
        if (player.getPlayerName().equals(this.playerOne.getPlayerName())){
            try {
                Stone blackStone = new Stone("B");
                playerOne.receiveStones(blackStone);
            } catch (IOException e) {
                gameEnded = true;
                gameResult.put("winner", playerTwo);
                gameResult.put("loser", playerOne);
            }
        }
        else {
            try {
                Stone blackStone = new Stone("W");
                playerOne.receiveStones(blackStone);
            } catch (IOException e) {
                gameEnded = true;
                gameResult.put("winner", playerOne);
                gameResult.put("loser", playerTwo);
            }
        }

    }

    public void playGame() throws Exception {
        while (!gameEnded) {
            System.out.println(boardHistory.get(0).printBoard());
            try {
                if (currentStoneColor.equals("B")){
                    String playerOneMove = playerOne.makeAMove(boardHistory);
                    System.out.println(playerOneMove);
                    if (playerOneMove.equals("pass")){
                        pass();
                    }
                    else{
                        Point playerOneMovePoint = new Point(playerOneMove);
                        if (!playerOneMovePoint.isValid()){
                            illegalEndGame("B");
                        }
                        makeMove(playerOneMovePoint);
                    }
                }
                if (currentStoneColor.equals("W")){
                    String playerTwoMove = playerTwo.makeAMove(boardHistory);
                    System.out.println(playerTwoMove);
                    if (playerTwoMove.equals("pass")){
                        pass();
                    }
                    else{
                        Point playerTwoMovePoint = new Point(playerTwoMove);
                        if (!playerTwoMovePoint.isValid()){
                            illegalEndGame("W");
                        }
                        makeMove(playerTwoMovePoint);
                    }
                }
            } catch(Exception e) {
                illegalEndGame(currentStoneColor);
            }
        }
    }

    @Override
    public void pass() throws Exception {
        if (!gameEnded){
            JSONArray returnHistory = makeCopyofCurrentState();
            consecutivePass++;
            if (consecutivePass == 2) {
                gameLog.add(returnHistory);
                legalEndGame();
                return;
            }
            alternatePlayer();

            Board newBoard = new Board(boardHistory.get(0));
            if (boardHistory.size() == 3) {
                boardHistory.remove(2);
            }
            boardHistory.add(0, newBoard);

            gameLog.add(returnHistory);
        }
    }

    @Override
    public void makeMove(Point point) throws Exception {
        if (gameEnded == false){
            JSONArray returnHistory = makeCopyofCurrentState();

            consecutivePass = 0;
            Stone currentStone = new Stone(currentStoneColor);
            if (!(boolean)ruleChecker.moveCheck(currentStone, point, boardHistory).get(0)) {
                gameLog.add(returnHistory);
                illegalEndGame(currentStoneColor);
                return;
            }
            Board newBoard = new Board(boardHistory.get(0));
            newBoard.place(currentStone, point);
            if (boardHistory.size() == 3) {
                boardHistory.remove(2);
            }
            boardHistory.add(0, newBoard);
            alternatePlayer();

            gameLog.add(returnHistory);
        }
    }

    void illegalEndGame(String stoneColor) throws Exception {
        System.out.println("comes to illgal end game");
        JSONArray winnerArray = new JSONArray();
        if (stoneColor == "B"){
            winnerArray.add(playerTwo.getPlayerName());
            gameResult.put("winner", playerTwo);
            gameResult.put("loser", playerOne);
            gameResult.put("cheater", playerOne);
        }
        else{
            winnerArray.add(playerOne.getPlayerName());
            gameResult.put("winner", playerOne);
            gameResult.put("loser", playerTwo);
            gameResult.put("cheater", playerTwo);
        }
        gameEnded = true;
        playerOne.endGame();
        playerTwo.endGame();
    }

    void legalEndGame() throws Exception {
        RuleChecker ruleChecker = new RuleChecker();
        JSONObject scores = ruleChecker.getScore(boardHistory.get(0));
        JSONArray jsonArray = new JSONArray();
        int blackScore = (int)scores.get("B");
        int whiteScore = (int)scores.get("W");
        gameResult.put("cheater", null);
        if (blackScore > whiteScore){
            jsonArray.add(playerOne.getPlayerName());
            gameResult.put("winner", playerOne);
            gameResult.put("loser", playerTwo);
        }
        else if (whiteScore > blackScore){
            jsonArray.add(playerTwo.getPlayerName());
            gameResult.put("winner", playerTwo);
            gameResult.put("loser", playerOne);
        }
        else {
            // TODO: Randomize this part
            gameResult.put("winner", playerTwo);
            gameResult.put("loser", playerOne);
            if (playerOne.getPlayerName().compareTo(playerTwo.getPlayerName()) < 0){
                jsonArray.add(playerOne.getPlayerName());
                jsonArray.add(playerTwo.getPlayerName());
            }
            else {
                jsonArray.add(playerTwo.getPlayerName());
                jsonArray.add(playerOne.getPlayerName());
            }
        }
        gameEnded = true;
        playerOne.endGame();
        playerTwo.endGame();
    }

    void alternatePlayer() {
        if (currentStoneColor.equals("B")) {
            currentStoneColor = "W";
        } else {
            currentStoneColor = "B";
        }
    }

    private JSONArray makeCopyofCurrentState(){
        JSONArray currentHistory = new JSONArray();
        for (Board board : boardHistory){
            Board boardCopy = new Board(board);
            currentHistory.add(boardCopy.printBoard());
        }
        return (currentHistory);
    }
}
