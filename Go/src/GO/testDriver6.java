package GO;

import org.json.simple.JSONArray;

import java.util.ArrayList;

public class testDriver6 {
    public static void main(String[] args) throws Exception {
        InputParser inputParser = new InputParser();
        ArrayList<Object> parsed = inputParser.parser();
        Game game = new Game();

        String firstName = parsed.get(0).toString();
        String secondName = parsed.get(1).toString();

        game.registerPlayer(firstName);
        game.registerPlayer(secondName);

        for (int i = 2; i < parsed.size(); i++){
            String command = parsed.get(i).toString();
            if (command.equals("pass")){
                game.pass();
            }
            else{
                game.makeMove(new Point(command));
            }
        }
        System.out.println(game.getGameLog());
    }
}
