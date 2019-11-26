package GO;

import org.json.simple.JSONArray;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class gameAdmin {
    public static void main(String[] args) throws Exception {
        Game referee = new Game();
        System.out.println("it's reading config");

        ConfigReader config = new ConfigReader();
        JSONArray winners = new JSONArray();
        System.out.println("it's starting to connect port: " + config.port());

        //open sockets
        InetAddress addr = InetAddress.getByName(config.ipAddress());
        ServerSocket ss = new ServerSocket(config.port(), 50, addr);
        Socket s = ss.accept();
        System.out.println("Connected to port: " + ss.getLocalPort());


        //register player 1
        Player localPlayer = new Player();
        referee.playerOne = localPlayer;
        referee.registerPlayer();
        System.out.println("It is registering player one at least");


        //register player 2
        ProxyPlayer proxyPlayer = new ProxyPlayer(s);
        referee.playerTwo = proxyPlayer;
        referee.registerPlayer();

        System.out.println("It is registering players at least");

        //play game and get winners
        winners = referee.playGame();

        //close sockets
        ss.close();
        s.close();

        //Print out winner
        System.out.println(winners);
    }
}
