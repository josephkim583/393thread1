package GO;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ProxyPlayer implements GoPlayer{
    private Socket s;
    private InputStreamReader in;
    private BufferedReader bf;
    private PrintWriter outputWriter;
    private Player proxyPlayer = new Player();
    private InputParser parser = new InputParser();

    public ProxyPlayer(Socket s) throws IOException {
        System.out.println("Proxy player made - proxyplayer.java");
        this.s = s;
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
    }

    @Override
    public String getPlayerName() {
       return this.proxyPlayer.getPlayerName();
    }

    //TODO: does it need to return the name?
    public String register(String string) throws IOException {
        if (proxyPlayer.isRegistered()){
            return proxyPlayer.getPlayerName();
        }
        openConnection();

        JSONArray commandArray = new JSONArray();
        commandArray.add("register");

        System.out.println("register proxy function");

        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        System.out.println("register proxy flushed");

        StringBuilder sb = new StringBuilder(512);
        int c = 0;
        String registeredName = null;
        while ((c = bf.read()) != -1) {
            sb.append((char) c);
            String s = sb.toString();
            System.out.println("print s: "+s);
            try{
                System.out.println("valid json?: "+this.parser.parseJSON(s).toString());
                registeredName = this.parser.parseJSON(s).toString();
                break;
            } catch (ParseException e) {
                continue;
            }
        }
        System.out.println("register proxy received: "+registeredName);
        return proxyPlayer.register(registeredName);
    }

    //TODO: does it need to return?
    public String receiveStones(Stone stone) throws IOException {
        openConnection();
        JSONArray commandArray = new JSONArray();
        commandArray.add("receive-stones");
        commandArray.add(stone.getStone());
        this.outputWriter.println(commandArray.toJSONString());
        this.outputWriter.flush();
        return this.proxyPlayer.receiveStones(stone);
    }

    public String makeAMove(ArrayList<Board> boards) throws IOException {
//        if (!(proxyPlayer.isRegistered() && proxyPlayer.isReceivedStone())) {
//            return "Go has gone crazy!";
//        }
        openConnection();
        JSONArray commandArray = new JSONArray();
        commandArray.add("make-a-move");
        JSONArray boardArray = new JSONArray();
        for (Board b : boards) {
            JSONArray printBoard = b.printBoard();
            boardArray.add(printBoard);
        }

        commandArray.add(boardArray);
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();



        StringBuilder sb = new StringBuilder(512);
        int c = 0;
        int counter = 0;
        String move = null;
        while ((c = bf.read()) != -1) {
            sb.append((char) c);
            String s = sb.toString();
            System.out.println("print s: "+s);
            System.out.println("valid json?: "+this.parser.isValidJson(s));
            try{
                System.out.println("valid json?: "+this.parser.parseJSON(s).toString());
                move = this.parser.parseJSON(s).toString();
                break;
            } catch (ParseException e) {
                continue;
            }
        }
        return move;
    }

    public String endGame() throws IOException {
        openConnection();
        JSONArray commandArray = new JSONArray();
        commandArray.add("end-game");
        this.outputWriter.println(commandArray.toJSONString());
        this.outputWriter.flush();

        StringBuilder sb = new StringBuilder(512);
        int c = 0;
        int counter = 0;
        String endGame = null;
        while ((c = bf.read()) != -1) {
            sb.append((char) c);
            String s = sb.toString();
            System.out.println("print s: "+s);
            System.out.println("valid json?: "+this.parser.isValidJson(s));
            try{
                System.out.println("valid json?: "+this.parser.parseJSON(s).toString());
                endGame = this.parser.parseJSON(s).toString();
                break;
            } catch (ParseException e) {
                continue;
            }
        }
        return endGame;
    }

    void openConnection() throws IOException {
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
    }
}