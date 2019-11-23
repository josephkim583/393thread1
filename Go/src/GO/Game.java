package GO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class Game implements GameInterface {
    GoPlayer playerOne;
    ProxyPlayer playerTwo;
    String currentStoneColor = "B";
    int consecutivePass = 0;
    int numberOfPlayers = 0;
    boolean gameEnded = false;
    ArrayList<Board> boardHistory = new ArrayList<>();
    RuleChecker ruleChecker = new RuleChecker();
    JSONArray gameLog = new JSONArray();
    JSONArray winner = new JSONArray();

    public Game(){
        Board emptyBoard = new Board();
        boardHistory.add(emptyBoard);
    }

    public JSONArray getGameLog() {
        return gameLog;
    }

    public void registerPlayer() throws IOException {
        if (numberOfPlayers == 0){
            playerOne.register("localPlayer");
            Stone blackStone = new Stone("B");
            playerOne.receiveStones(blackStone);
            numberOfPlayers++;
            gameLog.add("B");
        } else if (numberOfPlayers == 1){
            //Check if connection is still open.
            // If connection was closed then add local player as winner then end game.
            try{
                playerTwo.register("localPlayer");
                Stone whiteStone = new Stone("W");
                playerTwo.receiveStones(whiteStone);
            } catch (Exception e){
                gameEnded = true;
                winner.add(playerOne.getPlayerName());
            }
            numberOfPlayers++;
            gameLog.add("W");
        }
        // TODO: GO has gone crazy case
    }

    public JSONArray playGame() throws Exception {
        while (!gameEnded) {
//            System.out.println(boardHistory.get(0).printBoard());
//            if (currentStoneColor.equals("B")){
//                String playerOneMove = playerOne.makeAMove(boardHistory);
//                if (playerOneMove.equals("pass")){
//                    pass();
//                }
//                else{
//                    Point playerOneMovePoint = new Point(playerOneMove);
//                    makeMove(playerOneMovePoint);
//                }
//            }
//            else {
//                JSONArray makeAMoveArray = new JSONArray();
//                makeAMoveArray.add("make-a-move");
//                makeAMoveArray.add(makeCopyofCurrentState());
            try {
                    GoPlayer currentPlayer;
                    if (currentStoneColor.equals("B")) {
                        currentPlayer = playerOne;
                    } else {
                        currentPlayer = playerTwo;
                    }
                    String playerMove = currentPlayer.makeAMove(boardHistory);

                    if (playerMove.equals("pass")){
                        pass();
                    }
                    else {
                        Point playerTwoMovePoint = new Point(playerMove);
                        makeMove(playerTwoMovePoint);
                    }
                } catch(Exception e) {
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
