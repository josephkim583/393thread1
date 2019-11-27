package GO;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ProxyPlayer implements GoPlayer{
    private Socket s;
    private InputStreamReader in;
    private BufferedReader bf;
    private PrintWriter outputWriter;
    private String proxyPlayerName;
    private String stoneColor;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public ProxyPlayer(Socket s) throws IOException {
        this.s = s;
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
        this.dataOutputStream = new DataOutputStream(this.s.getOutputStream());
        this.dataInputStream = new DataInputStream(this.s.getInputStream());
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
        this.dataOutputStream.write(commandArray.toString().getBytes());
//        this.outputWriter.println(commandArray);
//        this.outputWriter.flush();
        int length = this.dataInputStream.readInt();
        byte[] message = new byte[length];
        dataInputStream.readFully(message, 0, message.length);
        String str = message.toString();

        System.out.println(str);

        this.proxyPlayerName = str;
        return this.proxyPlayerName;
    }

    //TODO: does it need to return?
    public boolean receiveStones(Stone stone) throws IOException {
        JSONArray commandArray = new JSONArray();
        commandArray.add("receive-stones");
        commandArray.add(stone.getStone());
        System.out.println(commandArray);
        this.dataOutputStream.write(commandArray.toString().getBytes());
//        this.outputWriter.println(commandArray);
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
        this.dataOutputStream.write(commandArray.toString().getBytes());

//        this.outputWriter.println(commandArray);
//        this.outputWriter.flush();
//        String str = bf.readLine();
        int length = this.dataInputStream.readInt();
        byte[] message = new byte[length];
        dataInputStream.readFully(message, 0, message.length);
        String str = message.toString();

        System.out.println(str);
        return str;
    }

    public String endGame() throws IOException {
        JSONArray commandArray = new JSONArray();
        commandArray.add("end-game");
        this.dataOutputStream.write(commandArray.toString().getBytes());
//        this.outputWriter.println(commandArray);
//        this.outputWriter.flush();
//        String str = bf.readLine();
        int length = this.dataInputStream.readInt();
        byte[] message = new byte[length];
        dataInputStream.readFully(message, 0, message.length);
        String str = message.toString();

        System.out.println(str);
        return str;
    }
}