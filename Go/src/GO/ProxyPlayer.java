package GO;

import jdk.internal.util.xml.impl.Input;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ProxyPlayer{
    Socket s;
    InputStreamReader in;
    BufferedReader bf;
    PrintWriter pr;
    int portNumber;

    public ProxyPlayer(int portNumber) throws IOException {
        this.portNumber = portNumber;
    }

    public String register(JSONArray commandArray) throws IOException {
        openConnections();
        this.pr.println(commandArray);
        this.pr.flush();
        String str = this.bf.readLine();
        closeConnections();
        return str;
    }

    public boolean receiveStones(JSONArray commandArray) throws IOException {
        openConnections();
        this.pr.println(commandArray);
        this.pr.flush();
        String str = this.bf.readLine();
        while (str == null){
            str = this.bf.readLine();
        }
        closeConnections();
        return Boolean.valueOf(str);
    }

    public String makeAMove(JSONArray commandArray) throws IOException {
        openConnections();
        this.pr.println(commandArray);
        this.pr.flush();
        String str = this.bf.readLine();
        closeConnections();
        return str;
    }

    public void closeConnections() throws IOException {
        this.s.shutdownInput();
        this.s.shutdownOutput();
        this.s.close();
        this.in.close();
        this.bf.close();
    }

    public void closeAllConnections() throws IOException {
        openConnections();
        JSONArray shutdown = new JSONArray();
        shutdown.add("shutdown");
        this.pr.println(shutdown);
        this.pr.flush();
        closeConnections();
    }

    public void openConnections() throws IOException {
        this.s = new Socket("localhost", this.portNumber);
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.pr = new PrintWriter(this.s.getOutputStream());
    }

//    public static void main(String[] args) throws Exception {
//        Player player = new Player();
//        InetAddress addr = InetAddress.getByName("127.0.0.1");
//        ServerSocket ss = new ServerSocket(8154, 50, addr);
//        Socket s = ss.accept();
//
//        InputStreamReader in = new InputStreamReader(s.getInputStream());
//        BufferedReader bf = new BufferedReader(in);
//
//
//        String str = bf.readLine();
//        JSONParser parser = new JSONParser();
//        JSONArray parsedInput = (JSONArray) parser.parse(str);
//        JSONArray outputArray = new JSONArray();
//        InputParser inputParser = new InputParser();
//
//        int counter = 0;
//        loop: for (Object parse : parsedInput) {
//            JSONArray commandArray = ((JSONArray) parse);
//            String command = commandArray.get(0).toString();
//            switch (command) {
//                case ("register"): {
//                    if (counter != 0) {
//                        outputArray.add("GO has gone crazy!");
//                        break loop;
//                    }
//                    String registered = player.register("no name");
//                    outputArray.add(registered);
//                    break;
//                }
//                case ("receive-stones"): {
//                    if (counter != 1) {
//                        outputArray.add("GO has gone crazy!");
//                        break loop;
//                    }
//                    Stone playerStone = new Stone(((JSONArray) parse).get(1).toString());
//                    player.receiveStones(playerStone);
//                    break;
//                }
//                case ("make-a-move"): {
//                    if (counter < 2) {
//                        outputArray.add("GO has gone crazy!");
//                        break loop;
//                    }
//                    ArrayList<Board> boards = new ArrayList<Board>();
//                    JSONArray boardJSONArray = (JSONArray) ((JSONArray) parse).get(1);
//                    for (int i = 0; i < boardJSONArray.size(); i++) {
//                        Board temp = new Board(inputParser.parseJSONboard((JSONArray) boardJSONArray.get(i)));
//                        boards.add(temp);
//                    }
//                    String move = player.makeAMove(boards, 1);
//                    outputArray.add(move);
//                }
//            }
//            counter ++;
//        }
//
//        PrintWriter outputWriter = new PrintWriter(s.getOutputStream());
//        outputWriter.println(outputArray);
//        outputWriter.flush();
//
//        in.close();
//        bf.close();
//        outputWriter.close();
//        s.close();
//        ss.close();
//    }
}
