package GO;

import java.util.ArrayList;

public class Game implements GameInterface {
    // Player playerOne;
    // Player playerTwo;
    int consecutivePass = 0;
    // Player currentPlayer = playerOne;
    boolean registered;
    ArrayList<Board> boardHistory = new ArrayList<>();
    RuleChecker ruleChecker = new RuleChecker();



    @Override
    public void registerPlayer(String name) {

    }

    @Override
    public void pass() {
        consecutivePass++;
        if (consecutivePass == 2) {
            endGame();
        }
        alternatePlayer();
    }

    @Override
    public void makeMove(Point point) throws Exception {
        consecutivePass = 0;
        Stone currentStone = currentPlayer.stone;
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
}
