package GO;

import org.junit.jupiter.api.Test;

import static GO.Board.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    public Board makeBoard() {
        String[][] emptyBoard = new String[19][19];
        for (int i = 0; i < 19; i++) {
            for(int j = 0; j < 19; j++) {
                emptyBoard[i][j] = " ";
            }
        }
        Board testBoard = new Board(emptyBoard);
        return testBoard;
    }

    @Test
    void occupied() throws Exception {
        Board testBoard = makeBoard();
        Point testPoint = new Point("1-1");
        assertFalse(testBoard.occupied(testPoint));
        testBoard.place(new Stone("B"), new Point("5-5"));
        assertTrue(testBoard.occupied(new Point("5-5")));
        testBoard.remove(new Stone("B"),new Point("5-5"));
        assertFalse(testBoard.occupied(new Point("5-5")));

        String [][] test =
                {{"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
                {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", "W", "W", "W", "W", "W", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", " ", " ", " ", " ", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", " ", " ", " ", " ", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", " ", " ", " ", " ", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", " ", " ", " ", " ", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", " ", " ", " ", " ", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", " ", " ", " ", " ", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", " ", " ", " ", " ", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", " ", " ", " ", " ", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", " ", " ", " ", " ", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                {"W", " ", " ", " ", " ", " ", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "}};
        Board testBoard2 = new Board(test);
        assertFalse(testBoard2.occupied(new Point("2-8")));
    }

    @Test
    void occupies() throws Exception {
        Board testBoard = makeBoard();
        Point testPoint = new Point("5-5");
        testBoard.place(new Stone("B"), new Point("5-5"));
        assertTrue(testBoard.occupies(new Stone("B"), testPoint));
        assertFalse(testBoard.occupies(new Stone("W"), testPoint));
        testBoard.remove(new Stone("B"), testPoint);
        testBoard.place(new Stone("W"), new Point("5-5"));
        assertFalse(testBoard.occupies(new Stone("B"), testPoint));
        assertTrue(testBoard.occupies(new Stone("W"), testPoint));

    }

    @Test
    void reachable() throws Exception {
        Board testBoard = makeBoard();
        Board testBoard2 = new Board(testBoard);
        testBoard2.place(new Stone("W"), new Point("1-1"));
        assertFalse(testBoard.getPointValue(new Point("1-1")).equals("W"));
    }

    @Test
    void place() {
    }

    @Test
    void remove() {
    }

    @Test
    void getPoints() {
    }
}