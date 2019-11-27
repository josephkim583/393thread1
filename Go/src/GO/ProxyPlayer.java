package GO;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ProxyPlayer implements GoPlayer{
    private Socket s;
    private InputStreamReader in;
    private BufferedReader bf;
    private PrintWriter outputWriter;
    private String proxyPlayerName;
    private String stoneColor;

    public ProxyPlayer(Socket s) throws IOException {
        this.s = s;
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
    }

    @Override
    public String getPlayerName() {
       return this.proxyPlayerName;
    }

    //TODO: does it need to return the name?
    public String register(String string) throws IOException {
        if (proxyPlayerName != null){
            return this.proxyPlayerName;
        }
        JSONArray commandArray = new JSONArray();
        commandArray.add("register");
//        this.outputWriter.println(commandArray.toJSONString());
//        this.outputWriter.flush();
        OutputStreamWriter out = new OutputStreamWriter(
                s.getOutputStream(), StandardCharsets.UTF_8);
        out.write(commandArray.toJSONString());
        String str = this.bf.readLine();
        this.proxyPlayerName = str;
        return this.proxyPlayerName;
    }

    //TODO: does it need to return?
    public boolean receiveStones(Stone stone) throws IOException {
        JSONArray commandArray = new JSONArray();
        commandArray.add("receive-stones");
        commandArray.add(stone.getStone());
        System.out.println(commandArray.toJSONString());
        OutputStreamWriter out = new OutputStreamWriter(
                s.getOutputStream(), StandardCharsets.UTF_8);
        out.write(commandArray.toJSONString());
//        this.outputWriter.println(commandArray.toJSONString());
//        this.outputWriter.flush();
        this.stoneColor = stone.getStone();
        return true;
    }

    public String makeAMove(ArrayList<Board> boards) throws IOException {
        if (!(this.proxyPlayerName != null && this.stoneColor != null)) {
            return "Go has gone crazy!";
        }
        JSONArray commandArray = new JSONArray();
        commandArray.add("make-a-move");
        JSONArray boardArray = new JSONArray();
        for (Board b : boards) {
            Board boardCopy = new Board(b);
            boardArray.add(boardCopy.printBoard());
        }
        commandArray.add(boardArray);
        System.out.println(commandArray.toJSONString());
        OutputStreamWriter out = new OutputStreamWriter(
                s.getOutputStream(), StandardCharsets.UTF_8);
        out.write(commandArray.toJSONString());
//        this.outputWriter.println(commandArray.toJSONString());
//        this.outputWriter.flush();
        String str = bf.readLine();
        return str;
    }

    public String endGame() throws IOException {
        JSONArray commandArray = new JSONArray();
        commandArray.add("end-game");
//        this.outputWriter.println(commandArray.toJSONString());
//        this.outputWriter.flush();
        OutputStreamWriter out = new OutputStreamWriter(
                s.getOutputStream(), StandardCharsets.UTF_8);
        out.write(commandArray.toString());
        String str = bf.readLine();
        return str;
    }
}