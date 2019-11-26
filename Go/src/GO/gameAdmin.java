//package GO;
//
//import org.json.simple.JSONArray;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//
//public class gameAdmin {
//    public static void main(String[] args) throws Exception {
//        Game referee = new Game();
//        ConfigReader config = new ConfigReader();
//        JSONArray winners = new JSONArray();
//        ArrayList<ProxyPlayer> listOfProxyFuckers = new ArrayList<>();
//        //open sockets
//        ServerSocket ss = new ServerSocket(config.port());
//
//        for (int i =0; i<2; i++) {
//            Socket s = ss.accept();
//            System.out.println("client accepted" + i);
//            ProxyPlayer proxyPlayer = new ProxyPlayer(s);
//            listOfProxyFuckers.add(proxyPlayer);
//        }
//
//        //register player 1
//        referee.registerPlayer(listOfProxyFuckers.get(0));
//        referee.registerPlayer(listOfProxyFuckers.get(1));
//        //register player 2
//
//        System.out.println(referee.playerOne.getPlayerName());
//        System.out.println(referee.playerTwo.getPlayerName());
//
//
//
//        //play game and get winners
//        winners = referee.playGame();
//
//        //close sockets
//        ss.close();
//        //Print out winner
//        System.out.println(winners);
//    }
//}
