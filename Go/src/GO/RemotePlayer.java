package GO;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class RemotePlayer implements GoPlayer {
    public static void main(String[] args) throws Exception {
//        ConfigReader config = new ConfigReader();
        RemotePlayer rp = new RemotePlayer();
        int counter = 0;
        Socket s = new Socket("localhost", 9000);
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        PrintWriter outputWrtier = new PrintWriter(s.getOutputStream());

        loop: while(true){
            try {
//                counter += 1;
//                if (counter == 2){
//                    break loop;
//                }

                String str = bf.readLine();
                if (str != null) {
//                    System.out.println(str);
                    JSONParser parser = new JSONParser();
                    JSONArray commandArray = (JSONArray) parser.parse(str);
                    String command = commandArray.get(0).toString();

                    switch (command) {
                        case ("register"): {
                            if (commandArray.size() != 1) {
                                outputWrtier.println("GO has gone crazy!");
                                outputWrtier.flush();
                                break;
                            }
                            String registered = rp.register("no name");
                            outputWrtier.println(registered);
                            outputWrtier.flush();
                            break;
                        }
                        case ("receive-stones"): {
                            if (commandArray.size() != 2) {
                                outputWrtier.println("GO has gone crazy!");
                                outputWrtier.flush();
                                break;
                            }
                            try {
                                Stone playerStone = new Stone(commandArray.get(1).toString());
                                boolean receiveStoneSuccess = rp.receiveStones(playerStone);
                                outputWrtier.println(receiveStoneSuccess);
                                outputWrtier.flush();
                                break;
                            } catch (Exception e) {
                                outputWrtier.println(false);
                                outputWrtier.flush();
                                break;
                            }
                        }
                        case ("make-a-move"): {
                            if (commandArray.size() != 2) {
                                outputWrtier.println("GO has gone crazy!");
                                outputWrtier.flush();
                                break;
                            }
                            InputParser input = new InputParser();
                            ArrayList<Board> boards = new ArrayList<Board>();
                            JSONArray boardJSONArray = (JSONArray) commandArray.get(1);
                            for (int i = 0; i < boardJSONArray.size(); i++) {
                                try {
                                    Board temp = new Board(input.parseJSONboard((JSONArray) boardJSONArray.get(i)));
                                    boards.add(temp);
                                } catch (Exception e) {
                                    outputWrtier.println("GO has gone crazy!");
                                    outputWrtier.flush();
                                    break;
                                }
                            }
                            String move = rp.makeAMove(boards);
                            outputWrtier.println(move);
                            outputWrtier.flush();
                            break;
                        }
                        case ("shutdown"): {
                            break loop;
                        }
                    }
                }
//                s.close();
//                in.close();
//                bf.close();
//                outputWrtier.close();
            } catch (Exception e){
                continue ;
            }
        }
        s.close();
        in.close();
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
    public String makeAMove(ArrayList<Board> boards) throws Exception {
        String makeAMove = p.makeAMove(boards);
        return makeAMove;
    }
}
