package GO;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ProxyPlayer implements GoPlayer{
    private Player proxyPlayer = new Player();
    private Socket s;
    private InputStreamReader in;
    private BufferedReader bf;
    private PrintWriter outputWriter;

    public ProxyPlayer(Socket s) throws IOException {
        this.s = s;
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
    }

    public Player getProxyPlayer() {
        return proxyPlayer;
    }

    @Override
    public String getPlayerName() {
       return proxyPlayer.getPlayerName();
    }

    //TODO: does it need to return the name?
    public String register(String string) throws IOException {
        if (proxyPlayer.isRegistered()){
            return proxyPlayer.getPlayerName();
        }
        JSONArray commandArray = new JSONArray();
        commandArray.add("register");
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        String str = this.bf.readLine();
        return proxyPlayer.register(str);
    }

    //TODO: does it need to return?
    public boolean receiveStones(Stone stone) throws IOException {
        if (proxyPlayer.isReceivedStone()){
            return true;
        }
        JSONArray commandArray = new JSONArray();
        commandArray.add("receive-stones");
        commandArray.add(stone.getStone());
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        return proxyPlayer.receiveStones(stone);
    }

    public String makeAMove(ArrayList<Board> boards) throws IOException {
        if (!(proxyPlayer.isRegistered() && proxyPlayer.isReceivedStone())) {
            return "Go has gone crazy!";
        }
        JSONArray commandArray = new JSONArray();
        commandArray.add("make-a-move");
        JSONArray boardArray = new JSONArray();
        for (Board b : boards) {
            boardArray.add(b.printBoard());
        }
        commandArray.add(boardArray);
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        String str = bf.readLine();
        return str;
    }

    public String endGame() throws IOException {
        JSONArray commandArray = new JSONArray();
        commandArray.add("end-game");
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        String str = bf.readLine();
//        s.close();
        return str;
    }

}