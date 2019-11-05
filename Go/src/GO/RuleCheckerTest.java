package GO;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RuleCheckerTest {
    String [][] testBig = new String[][]
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

    String [][] testLiberty = new String[][]
            {{"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
                    {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                    {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                    {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                    {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                    {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                    {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
                    {"W", "B", "B", "B", "B", "B", "W", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
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

    @Test
    void TestLibertyTest() throws Exception {
        RuleChecker rc = new RuleChecker();
        Point point = new Point(3,3);
        Board test = new Board(testLiberty);
        assertTrue(rc.getLiberties(test, point).size() == 9);
    }

    @Test
    void makeMaybeBoard() throws Exception {
        RuleChecker rc = new RuleChecker();
        Board test = new Board();
        Stone whiteStone = new Stone("W");
        Stone blackStone = new Stone("B");
        test.place(whiteStone, new Point("1-1"));
        test.place(whiteStone, new Point("1-2"));
        test.place(whiteStone, new Point("1-3"));
        test.place(whiteStone, new Point("2-1"));
        test.place(whiteStone, new Point("2-3"));
        test.place(whiteStone, new Point("3-1"));
        test.place(whiteStone, new Point("3-2"));
        test.place(whiteStone, new Point("3-3"));

        Board maybeBoard = rc.makeMaybeBoard(test, new Point("2-2"), blackStone);
        assertFalse(maybeBoard.occupied(new Point("2-2")));
        assertTrue(maybeBoard.occupied(new Point("1-1")));

        Board testBigBoard = new Board(testBig);
        Board maybeBoard2 = rc.makeMaybeBoard(testBigBoard, new Point("2-8"), new Stone("B"));
    }

    @Test
    void moveCheckTest() throws Exception {
        Board testBoard = new Board(testBig);
        RuleChecker rc = new RuleChecker();
        ArrayList<Board> testBoards= new ArrayList<>();
        testBoards.add(testBoard);
        assertFalse((boolean)rc.moveCheck(new Stone("B"), new Point("2-8"), testBoards).get(0));
    }
}
