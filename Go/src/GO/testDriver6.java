package GO;

import org.json.simple.JSONArray;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class testDriver6 {
    public static void main(String[] args) throws Exception {
        String[][] board = new String[19][19];
        InputParser inputParser = new InputParser();
        ArrayList<Object> parsed = inputParser.parser();
        JSONArray outputArray = new JSONArray();
        Game game = new Game();

        String firstName = parsed.get(0).toString();
        String secondName = parsed.get(1).toString();

        outputArray.add(game.registerPlayer(firstName));
        outputArray.add(game.registerPlayer(secondName));

        for (int i = 2; i < parsed.size(); i++){
            String command = parsed.get(i).toString();
            if (command.equals("pass")){
                outputArray.add(game.pass());
            }
            else{
                outputArray.add(game.makeMove(new Point(command)));
            }
        }
        System.out.println(Arrays.deepToString(outputArray.toArray()));
    }
}
