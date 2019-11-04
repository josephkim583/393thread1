package GO;

import java.io.IOException;
import java.util.ArrayList;

public class Game implements GameInterface {
     Player playerOne;
     Player playerTwo;
    int consecutivePass = 0;
    int numberOfPlayers = 0;
    Player currentPlayer = playerOne;
    boolean registered;
    ArrayList<Board> boardHistory = new ArrayList<>();
    RuleChecker ruleChecker = new RuleChecker();


    @Override
    public String registerPlayer(String name) throws IOException {
        if (numberOfPlayers == 0){
            Stone blackStone = new Stone("B");
            playerOne = new Player(name, blackStone);
            numberOfPlayers++;
            return "B";
        } else if (numberOfPlayers == 1){
            Stone whiteStone = new Stone("W");
            playerOne = new Player(name, whiteStone);
            numberOfPlayers++;
            return "W";
        }
        return "Trying to add more than 2 players";
    }

    @Override
    public ArrayList<Board> pass() {
        ArrayList<Board> returnHistory = makeCopyofCurrentState();

        consecutivePass++;
        if (consecutivePass == 2) {
            endGame();
        }
        alternatePlayer();

        return (returnHistory);
    }

    @Override
    public ArrayList<Board> makeMove(Point point) throws Exception {
        ArrayList<Board> returnHistory = makeCopyofCurrentState();

        consecutivePass = 0;
        Stone currentStone = currentPlayer.getPlayerStone();
        if (!ruleChecker.moveCheck(currentStone, point, boardHistory).getKey()) {
            endGame();
        }
        Board newBoard = new Board(boardHistory.get(0));
        newBoard.place(currentStone, point);
        if (boardHistory.size() == 3) {
            boardHistory.remove(2);
        }
        boardHistory.add(newBoard);
        alternatePlayer();

        return (returnHistory);
    }

    void endGame() {

    }

    void alternatePlayer() {
        if (currentPlayer == playerOne) {
            currentPlayer = playerTwo;
        } else {
            currentPlayer = playerOne;
        }
    }

    private ArrayList<Board> makeCopyofCurrentState(){
        ArrayList<Board> currentHistory = new ArrayList<>();
        for (Board board : boardHistory){
            Board boardCopy = new Board(board);
            currentHistory.add(boardCopy);
        }
        return (currentHistory);
    }
}
