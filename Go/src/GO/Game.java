package GO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class Game implements GameInterface {
    Player playerOne;
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
            Stone localPlayerStone = new Stone("B");
            playerOne.receiveStones(localPlayerStone);
            numberOfPlayers++;
            gameLog.add("B");
        } else if (numberOfPlayers == 1){
            JSONArray registerArray = new JSONArray();
            JSONArray receiveStoneArray = new JSONArray();
            receiveStoneArray.add("receive-stones");
            receiveStoneArray.add("W");
            registerArray.add("register");

            //Check if connection is still open.
            // If connection was closed then add local player as winner then end game.
            try{
                playerTwo.register(registerArray);
                playerTwo.receiveStones(receiveStoneArray);
            } catch (Exception e){
                gameEnded = true;
                winner.add(playerOne.getPlayerName());
            }
            numberOfPlayers++;
            gameLog.add("W");
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
    public JSONArray playGame() throws Exception {
        while (!gameEnded) {
            System.out.println(boardHistory.get(0).printBoard());
            if (currentStoneColor.equals("B")){
                String playerOneMove = playerOne.makeADumbMove(boardHistory);
                if (playerOneMove.equals("pass")){
                    pass();
                }
                else{
                    Point playerOneMovePoint = new Point(playerOneMove);
                    makeMove(playerOneMovePoint);
                }
            }
            else {
                JSONArray makeAMoveArray = new JSONArray();
                makeAMoveArray.add("make-a-move");
                makeAMoveArray.add(makeCopyofCurrentState());
                try {
                    String playerTwoMove = playerTwo.makeAMove(makeAMoveArray);

                    if (playerTwoMove.equals("pass")){
                        pass();
                    }
                    else {
                        Point playerTwoMovePoint = new Point(playerTwoMove);
                        makeMove(playerTwoMovePoint);
                    }
                } catch(Exception e) {
                    gameEnded = true;
                    JSONArray playerWins = new JSONArray();
                    playerWins.add(playerOne.getPlayerName());
                    return (playerWins);
                }
            }
        }
        return(this.winner);
    }

    void illegalEndGame(String stoneColor) throws Exception {

        JSONArray winnerArray = new JSONArray();
        if (stoneColor == "B"){
            winnerArray.add(playerTwo.getProxyPlayerName());
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
            jsonArray.add(playerTwo.getProxyPlayerName());
        }
        else {

            if (playerOne.getPlayerName().compareTo(playerTwo.getProxyPlayerName()) < 0){
                jsonArray.add(playerOne.getPlayerName());
                jsonArray.add(playerTwo.getProxyPlayerName());
            }
            else {
                jsonArray.add(playerTwo.getProxyPlayerName());
                jsonArray.add(playerOne.getPlayerName());
            }
        }
        winner = jsonArray;
        gameEnded = true;
    }

    void alternatePlayer() {
        if (currentStoneColor == "B") {
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
