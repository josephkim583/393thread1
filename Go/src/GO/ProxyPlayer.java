package GO;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ProxyPlayer implements GoPlayer{
    private Socket s;
    private InputStreamReader in;
    private BufferedReader bf;
    private PrintWriter outputWriter;
    private String proxyPlayerName;
    private String stoneColor;
    private Player proxyPlayer = new Player();

    public ProxyPlayer(Socket s) throws IOException {
        System.out.println("Proxy player made - proxyplayer.java");
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
        if (proxyPlayer.isRegistered()){
            return proxyPlayer.getPlayerName();
        }
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());

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
        while ((c = bf.read()) != -1 && counter < 26) {
            System.out.println(c);
            sb.append((char) c);
            counter += 1;
        }
        String str = sb.toString();
        System.out.println("register Name read");
        System.out.println(str);
        return proxyPlayer.register(str.substring(1));
    }

    //TODO: does it need to return?
    public boolean receiveStones(Stone stone) throws IOException {
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
        JSONArray commandArray = new JSONArray();
        commandArray.add("receive-stones");
        commandArray.add(stone.getStone());
        System.out.println(commandArray.toJSONString());
        this.outputWriter.println(commandArray.toJSONString());
        this.outputWriter.flush();
        this.stoneColor = stone.getStone();
        return true;
    }

    public String makeAMove(ArrayList<Board> boards) throws IOException {
//        if (!(proxyPlayer.isRegistered() && proxyPlayer.isReceivedStone())) {
//            return "Go has gone crazy!";
//        }
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
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
        System.out.println(commandArray);
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
//        String str = listenForMessage();
//        String str = bf.readLine();
//        int c = bf.read();


        StringBuilder sb = new StringBuilder(512);
        int c = 0;
        int counter = 0;
        while ((c = bf.read()) != -1 && counter < 4) {
            char temp = (char) c;
            System.out.println(c);
            sb.append((char) c);
            counter += 1;
        }
        String str = sb.toString();
        System.out.println("From make a move: " + str.substring(1));
        if (str.charAt(1) == 'p'){
            return "pass";
        }
        return str.substring(1);
    }

    public String endGame() throws IOException {
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
        JSONArray commandArray = new JSONArray();
        commandArray.add("end-game");
        this.outputWriter.println(commandArray.toJSONString());
        this.outputWriter.flush();
//        int c = bf.read();
//        System.out.println((char) c);
//        return (Integer.toString(c));
        StringBuilder sb = new StringBuilder(512);
        int c = 0;
        int counter = 0;
        while ((c = bf.read()) != -1 && counter < 3) {
            System.out.println(c);
            sb.append((char) c);
            counter += 1;
        }
        String str = sb.toString();
        System.out.println("at endgame");
        System.out.println(str);
        System.out.println("From end game: " + str);
        return str.substring(1);

//        int c = bf.read();
//        System.out.println(c);
//        return Integer.toString(c);
//        s.close();
//        return str;
    }
}