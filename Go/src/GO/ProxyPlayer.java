package GO;

import org.json.simple.JSONArray;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ProxyPlayer implements GoPlayer{
    private Socket s;
    private InputStreamReader in;
    private BufferedReader bf;
    private PrintWriter outputWriter;
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

        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        StringBuilder sb = new StringBuilder(512);
        int c = 0;
        int counter = 0;
        while ((c = bf.read()) != -1 && counter < 26) {
            System.out.println(c);
            sb.append((char) c);
            counter += 1;
        }
        String str = sb.toString();
        return proxyPlayer.register(str.substring(1));
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
        while ((c = bf.read()) != -1 && counter < 4) {
            char temp = (char) c;
            System.out.println(c);
            sb.append((char) c);
            counter += 1;
        }
        String str = sb.toString();
        if (str.charAt(1) == 'p'){
            return "pass";
        }
        return str.substring(1);
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
        while ((c = bf.read()) != -1 && counter < 3) {
            System.out.println(c);
            sb.append((char) c);
            counter += 1;
        }
        String str = sb.toString();
        return str.substring(1);
    }

    void openConnection() throws IOException {
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
    }
}