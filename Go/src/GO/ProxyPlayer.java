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
import java.util.Arrays;

public class ProxyPlayer implements GoPlayer{
    private Player proxyPlayer = new Player();
    private Socket s;
    private InputStreamReader in;
    private BufferedReader bf;
    private PrintWriter outputWriter;

    public ProxyPlayer(Socket s) throws IOException {
        System.out.println("Proxy player made - proxyplayer.java");
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
//        if (proxyPlayer.isRegistered()){
//            return proxyPlayer.getPlayerName();
//        }
        JSONArray commandArray = new JSONArray();
        commandArray.add("register");
        System.out.println(commandArray.getClass());
        System.out.println(commandArray.get(0).getClass());

        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        System.out.println("flushed");
//        int c = bf.read();
//        System.out.println((char) c);
//        return proxyPlayer.register(Integer.toString(c));
        StringBuilder sb = new StringBuilder(512);
        int c = 0;
        int counter = 0;
        while ((c = bf.read()) != -1 && counter <27) {
            System.out.println(c);
            sb.append((char) c);
            counter += 1;
        }
        String str = sb.toString();
        System.out.println("register Name read");
        System.out.println(str);
        return proxyPlayer.register(str);
    }

    //TODO: does it need to return?
    public boolean receiveStones(Stone stone) throws IOException {
        JSONArray commandArray = new JSONArray();
        commandArray.add("receive-stones");
        commandArray.add(stone.getStone());
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        return proxyPlayer.receiveStones(stone);
    }

    public String makeAMove(ArrayList<Board> boards) throws IOException {
//        if (!(proxyPlayer.isRegistered() && proxyPlayer.isReceivedStone())) {
//            return "Go has gone crazy!";
//        }
        JSONArray commandArray = new JSONArray();
        commandArray.add("make-a-move");
        JSONArray boardArray = new JSONArray();
        for (Board b : boards) {
            JSONArray printBoard = b.printBoard();
            boardArray.add(printBoard);
//            String[][] boardCopy = b.getStringBoard();
//            boardArray.add(Arrays.deepToString(boardCopy));
//            System.out.println(Arrays.deepToString(boardCopy).getClass());
        }
        commandArray.add(boardArray);
//        System.out.println(commandArray.size());
//        System.out.println(commandArray.getClass());
//        System.out.println(commandArray.get(0).getClass());
//        System.out.println(commandArray.get(1).getClass());

        System.out.println(commandArray);
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
//        String str = listenForMessage();
//        String str = bf.readLine();
//        int c = bf.read();


        StringBuilder sb = new StringBuilder(512);
        int counter = 0;
        int c = 0;
        while ((c = bf.read()) != -1 && counter <5) {
            System.out.println(c);
            sb.append((char) c);
            counter += 1;
        }
        String str = sb.toString();
        System.out.println("From make a move: " + str);
        return str;
    }

    public String endGame() throws IOException {
        JSONArray commandArray = new JSONArray();
        commandArray.add("end-game");
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
//        int c = bf.read();
//        System.out.println((char) c);
//        return (Integer.toString(c));
        StringBuilder sb = new StringBuilder(512);
        int c = 0;
        int counter = 0;
        while ((c = bf.read()) != -1 && counter <4) {
            System.out.println(c);
            sb.append((char) c);
            counter += 1;
        }
        String str = sb.toString();
        System.out.println("at endgame");
        System.out.println(str);
        System.out.println("From end game: " + str);
        return str;

//        int c = bf.read();
//        System.out.println(c);
//        return Integer.toString(c);
//        s.close();
//        return str;
    }
}