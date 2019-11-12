package GO;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class RemotePlayer implements GoPlayer {
    public static void main(String[] args) throws IOException, ParseException {
        ServerSocket ss = new ServerSocket(8152);

        while(true){
            Socket s = ss.accept();
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader bf = new BufferedReader(in);
            RemotePlayer rp = new RemotePlayer();
            PrintWriter outputWrtier = new PrintWriter(s.getOutputStream());
            String str = bf.readLine();
            if (str != null){
                System.out.print(str);
                JSONParser parser = new JSONParser();
                JSONArray commandArray = (JSONArray) parser.parse(str);
                String command = commandArray.get(0).toString();

                switch (command) {
                    case ("register"): {
                        String registered = rp.register("no name");
                        outputWrtier.println(registered);
                        outputWrtier.flush();
                        break;
                    }
                    case ("receive-stones"): {
                        Stone playerStone = new Stone(commandArray.get(1).toString());
                        boolean receiveStoneSuccess = rp.receiveStones(playerStone);
                        outputWrtier.println(receiveStoneSuccess);
                        outputWrtier.flush();
                        break;
                    }
                }
            }
        }
    }


    Player p = new Player();

    public RemotePlayer(){

    }

    @Override
    public String register(String name) {
        String register = p.register("no name");
        return register;
    }

    @Override
    public boolean receiveStones(Stone stone) {
        boolean receiveStoneSuccess = p.receiveStones(stone);
        return receiveStoneSuccess;
    }

    @Override
    public String makeAMove(ArrayList<Board> boards, int distance) throws Exception {
        String makeAMove = p.makeAMove(boards, distance);
        return makeAMove;
    }
}
