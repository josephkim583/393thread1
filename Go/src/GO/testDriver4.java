//package GO;
//
//import java.util.*;
//import java.lang.*;
//import java.io.*;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//public class testDriver4 {
//    public static void main(String[] args) throws Exception {
//        InputParser input = new InputParser();
//        RuleChecker ruleChecker = new RuleChecker();
//        ArrayList<Object> parsedInput = input.parser();
//        JSONArray outputArray = new JSONArray();
//        for (Object parsed : parsedInput) {
//            if (((JSONArray) parsed).size() == 19) {
//                JSONArray jsonBoard = ((JSONArray) parsed);
//                String[][] inputBoard = input.parseJSONboard(jsonBoard);
//                Board board = new Board(inputBoard);
//                outputArray.add(ruleChecker.getScore(board));
//            }
//            else {
//                JSONArray parsedJA = (JSONArray) parsed;
//                Stone stone = new Stone((String) parsedJA.get(0));
//                ArrayList<Board> boards = new ArrayList<Board>();
//                if (parsedJA.get(1).equals("pass")) {
//                    boolean checked = ruleChecker.pass();
//                    outputArray.add(checked);
//                } else {
//                    JSONArray move = (JSONArray) parsedJA.get(1);
//                    Point point = new Point((String) (move.get(0)));
//                    JSONArray boardJSONArray = ((JSONArray) move.get(1));
//                    for (int i = 0; i < boardJSONArray.size(); i++) {
//                        Board temp = new Board(input.parseJSONboard((JSONArray) boardJSONArray.get(i)));
//                        boards.add(temp);
//                    }
//                   outputArray.add(ruleChecker.moveCheck(stone, point, boards) && ruleChecker.historyCheck(stone, boards));
//                }
//            }
//        }
//        System.out.println(outputArray);
//    }
//}
