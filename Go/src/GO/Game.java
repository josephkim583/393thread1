package GO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Game implements GameInterface {
    GoPlayer playerOne;
    GoPlayer playerTwo;
    String currentStoneColor = "B";
    int consecutivePass = 0;
    int numberOfPlayers = 0;
    boolean gameEnded = false;
    ArrayList<Board> boardHistory = new ArrayList<>();
    RuleChecker ruleChecker = new RuleChecker();
    JSONArray gameLog = new JSONArray();
    JSONArray winner = new JSONArray();

    HashMap<String, ArrayList<GoPlayer>> gameResult = new HashMap<>();

    public Game(){
        Board emptyBoard = new Board();
        boardHistory.add(emptyBoard);
    }

    public JSONArray getGameLog() {
        return gameLog;
    }

    public void registerPlayer(GoPlayer player) throws IOException {
        if (numberOfPlayers == 0){
            //Check if connection is still open.
            // If connection was closed then add local player as winner then end game.
            try{
                this.playerOne = player;
                playerOne.register("localPlayer");
            } catch (Exception e){
                gameEnded = true;
            }
            numberOfPlayers++;

        } else if (numberOfPlayers == 1){
            //Check if connection is still open.
            // If connection was closed then add local player as winner then end game.
            try{
                this.playerTwo = player;
                playerTwo.register("localPlayer");
            } catch (Exception e){

            }
            numberOfPlayers++;
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
                winner.add(playerTwo.getPlayerName());
                gameResult.get("winner").add(playerTwo);
                gameResult.get("loser").add(playerOne);
            }
        }
        else {
            try {
                Stone blackStone = new Stone("W");
                playerOne.receiveStones(blackStone);
            } catch (IOException e) {
                gameEnded = true;
                winner.add(playerOne.getPlayerName());
                gameResult.get("winner").add(playerOne);
                gameResult.get("loser").add(playerTwo);
            }
        }

    }

    public JSONArray playGame() throws Exception {
        while (!gameEnded) {
            System.out.println(boardHistory.get(0).printBoard());
            try {
                if (currentStoneColor.equals("B")){
                    String playerOneMove = playerOne.makeAMove(boardHistory);
                    if (playerOneMove.equals("pass")){
                        pass();
                    }
                    else{
                        Point playerOneMovePoint = new Point(playerOneMove);
                        makeMove(playerOneMovePoint);
                    }
                }
                if (currentStoneColor.equals("W")){
                    String playerTwoMove = playerTwo.makeAMove(boardHistory);
                    if (playerTwoMove.equals("pass")){
                        pass();
                    }
                    else{
                        Point playerTwoMovePoint = new Point(playerTwoMove);
                        makeMove(playerTwoMovePoint);
                    }
                }
            } catch(Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
                gameEnded = true;
                GoPlayer opponent;
                if (currentStoneColor.equals("B")) {
                    opponent = playerTwo;
                } else {
                    opponent = playerOne;
                }

                winner.add(opponent.getPlayerName());
            }
        }
        return(this.winner);
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

        JSONArray winnerArray = new JSONArray();
        if (stoneColor == "B"){
            winnerArray.add(playerTwo.getPlayerName());
        }
        else{
            winnerArray.add(playerOne.getPlayerName());
        }
        winner = winnerArray;
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
        if (blackScore > whiteScore){
            jsonArray.add(playerOne.getPlayerName());
        }
        else if (whiteScore > blackScore){
            jsonArray.add(playerTwo.getPlayerName());
        }
        else {
            // TODO: Randomize this part
            if (playerOne.getPlayerName().compareTo(playerTwo.getPlayerName()) < 0){
                jsonArray.add(playerOne.getPlayerName());
                jsonArray.add(playerTwo.getPlayerName());
            }
            else {
                jsonArray.add(playerTwo.getPlayerName());
                jsonArray.add(playerOne.getPlayerName());
            }
        }
        winner = jsonArray;
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
