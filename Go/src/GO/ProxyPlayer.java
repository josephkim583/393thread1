package GO;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ProxyPlayer{
    public static void main(String[] args) throws Exception {
        Player player = new Player();
        ServerSocket ss = new ServerSocket(8015);
        Socket s = ss.accept();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);


        String str = bf.readLine();
        JSONParser parser = new JSONParser();
        JSONArray parsedInput = (JSONArray) parser.parse(str);
        JSONArray outputArray = new JSONArray();
        InputParser inputParser = new InputParser();


        for (Object parse : parsedInput) {
            JSONArray commandArray = ((JSONArray) parse);
            String command = commandArray.get(0).toString();
            switch (command) {
                case ("register"): {
                    String registered = player.register("no name");
                    outputArray.add(registered);
                    break;
                }
                case ("receive-stones"): {
                    Stone playerStone = new Stone(((JSONArray) parse).get(1).toString());
                    player.receiveStones(playerStone);
                    break;
                }
                case ("make-a-move"): {
                    ArrayList<Board> boards = new ArrayList<Board>();
                    JSONArray boardJSONArray = (JSONArray) ((JSONArray) parse).get(1);
                    for (int i = 0; i < boardJSONArray.size(); i++) {
                        Board temp = new Board(inputParser.parseJSONboard((JSONArray) boardJSONArray.get(i)));
                        boards.add(temp);
                    }
                    String move = player.makeAMove(boards, 1);
                    outputArray.add(move);
                }
            }
        }

        PrintWriter outputWriter = new PrintWriter(s.getOutputStream());
        outputWriter.println(outputArray);
        outputWriter.flush();
    }
}
