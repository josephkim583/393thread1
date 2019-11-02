package GO;

import java.util.*;
import java.lang.*;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class testDriver3 {

    public static void main(String[] args) throws Exception {
        testDriver3 testDriver = new testDriver3();
        InputParser Input = new InputParser();
        ArrayList<Object> parsedInput = Input.parser();
        JSONArray outputArray = new JSONArray();
        for (Object parsed : parsedInput) {
            outputArray.add(testDriver.callBoardMethod((JSONArray) parsed));
        }
        System.out.println(outputArray);
    }

    private Object callBoardMethod(JSONArray msg) throws Exception {
        InputParser input = new InputParser();
        String[][] inputBoard = input.parseJSONboard((JSONArray) msg.get(0));
        Board board = new Board(inputBoard);
        JSONArray statement = (JSONArray) msg.get(1);
        String call = statement.get(0).toString();
        switch (call) {
            case "occupied?": {
                Point target = new Point(statement.get(1).toString());
                return board.occupied(target);
            }
            case "occupies?": {
                Stone stone = new Stone(statement.get(1).toString());
                Point target = new Point(statement.get(2).toString());
                return board.occupies(stone, target);
            }
            case "reachable?": {
                Point target = new Point(statement.get(1).toString());
                MaybeStone maybeStone = new MaybeStone(statement.get(2).toString());
                return board.reachable(target, maybeStone);
            }
            case "place": {
                Stone stone = new Stone(statement.get(1).toString());
                Point target = new Point(statement.get(2).toString());
                return board.place(stone, target);
            }
            case "remove": {
                Stone stone = new Stone(statement.get(1).toString());
                Point target = new Point(statement.get(2).toString());
                return board.remove(stone, target);
            }
            case "get-points": {
                MaybeStone maybeStone = new MaybeStone(statement.get(1).toString());
                return board.getPoints(maybeStone);
            }
        }
        return false;
    }

}
