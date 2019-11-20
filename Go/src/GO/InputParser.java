package GO;

import java.util.*;
import java.lang.*;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class InputParser {
    public ArrayList<Object> parser() {
        Scanner scanner = new Scanner(System.in);
        JSONParser parser = new JSONParser();
        ArrayList<Object> parsedInput = new ArrayList<Object>();
        boolean finish = true;
        Object temp = new Object();
        String line = "";

        while (scanner.hasNextLine())
        {
            if (finish)
            {
                line = scanner.nextLine();
            }
            else
            {
                line += scanner.nextLine();
            }
            try {
                temp = parser.parse(line);
                parsedInput.add(temp);
                finish = true;
            } catch (ParseException e) {
                finish = false;
                continue;
            }
        }

        return parsedInput;
    }


    public String[][] parseJSONboard(JSONArray board) {
        Board b = new Board();
        int boardSize = b.boardSize();

        String[][] strBoard = new String[boardSize][boardSize];
        if (board.size() > boardSize){
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < board.size(); i++) {
            JSONArray inner = (JSONArray) board.get(i);
            if (inner.size() > boardSize){
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < inner.size(); j++) {
                strBoard[i][j] = inner.get(j).toString();
            }
        }
        return strBoard;
    }
}
