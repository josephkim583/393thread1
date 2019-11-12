package GO;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class socketTestDriver {
//    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
//        InputParser input = new InputParser();
//        ArrayList<Object> parsedInput = input.parser();
//
//        Thread.sleep(5000);
//
//        InetAddress addr = InetAddress.getByName("127.0.0.1");
//        Socket s = new Socket(addr, 8154);
//        PrintWriter pr = new PrintWriter(s.getOutputStream());
//
//        pr.println(parsedInput);
//        pr.flush();
//
//        InputStreamReader in = new InputStreamReader(s.getInputStream());
//        BufferedReader bf = new BufferedReader(in);
//        String str = bf.readLine();
//        JSONParser parser = new JSONParser();
//        JSONArray playerOutput = (JSONArray) parser.parse(str);
//
//        s.close();
//        pr.close();
//        in.close();
//        bf.close();
//        System.out.println(playerOutput);
//    }
    public static void main(String[] args) throws Exception {
        InputParser input = new InputParser();
        ArrayList<Object> parsedInput = input.parser();
        JSONArray outputArray = new JSONArray();
        ProxyPlayer proxyPlayer = new ProxyPlayer(8152);

        loop: for (Object parse : parsedInput) {
            JSONArray commandArray = ((JSONArray) parse);
            String command = commandArray.get(0).toString();
            switch (command) {
                case ("register"): {
                    String registered = proxyPlayer.register(commandArray);
                    if (registered.equals("GO has gone crazy!")){
                        outputArray.add(registered);
                        break loop;
                    }
                    outputArray.add(registered);
                    break;
                }
                case ("receive-stones"): {
                    boolean receivedStone = proxyPlayer.receiveStones(commandArray);
                    if (receivedStone == false){
                        outputArray.add("GO has gone crazy!");
                        break loop;
                    }
                    break;
                }
                case ("make-a-move"): {
                    String move = proxyPlayer.makeAMove(commandArray);
                    if (move.equals("GO has gone crazy!")){
                        outputArray.add(move);
                        break loop;
                    }
                    outputArray.add(move);
                    break ;
                }
                default: {
                    outputArray.add("GO has gone crazy!");
                    break loop;
                }
            }
//            proxyPlayer.closeConnections();
        }
        System.out.println(outputArray);
    }
}
