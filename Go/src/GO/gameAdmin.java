package GO;

import org.json.simple.JSONArray;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class gameAdmin {
    public static void main(String[] args) throws Exception {
        Game referee = new Game();
        ConfigReader config = new ConfigReader();
        JSONArray winners = new JSONArray();

        //open sockets
        InetAddress addr = InetAddress.getByName(config.ipAddress());
        ServerSocket ss = new ServerSocket(config.port(), 50, addr);
        Socket s = ss.accept();

        //register player 1
        Player localPlayer = new Player();
        referee.playerOne = localPlayer;
        referee.registerPlayer();

        //register player 2
        ProxyPlayer proxyPlayer = new ProxyPlayer(s);
        referee.playerTwo = proxyPlayer;
        referee.registerPlayer();

        //play game and get winners
        winners = referee.playGame();

        //close sockets
        ss.close();
        s.close();

        //Print out winner
        System.out.println(winners);
    }
}
