package GO;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class tournamentAdmin {
    public static void main(String[] args) throws Exception {
        tournamentAdmin admin = new tournamentAdmin();
        int playerNum = Integer.parseInt(args[0]);
        String mode = args[1];

        ConfigReader configReader = new ConfigReader();
        int closestPowerOfTwo = admin.closestPowerOfTwo(playerNum);
        int numNewPlayers = closestPowerOfTwo - playerNum;
        ArrayList<GoPlayer> listOfPlayers = new ArrayList<>();
        InetAddress addr = InetAddress.getByName(configReader.ipAddress());
        ServerSocket ss = new ServerSocket(configReader.port(), 50, addr);
        for (int i = 0; i < playerNum; i++) {
            Socket s = ss.accept();
            ProxyPlayer proxyPlayer = new ProxyPlayer(s);
            listOfPlayers.add(proxyPlayer);
        }
        for (int i = 0; i < numNewPlayers ; i++) {
            Player defaultPlayer = new Player();
            listOfPlayers.add(defaultPlayer);
        }

        if (mode.equals("-league")) {
            admin.league(listOfPlayers);
        } else if (mode.equals("-cup")) {
            admin.cup(listOfPlayers);
        }
    }

     int closestPowerOfTwo(int n){
        int closestPower = 2;
        while (closestPower < n){
            closestPower = closestPower*2;
        }
        return closestPower;
    }

    JSONArray league(ArrayList<GoPlayer> playerList) throws ParseException, IOException, URISyntaxException {
        JSONArray ranking = new JSONArray();

        return ranking;
    };

    JSONArray cup(ArrayList<GoPlayer> playerList) throws Exception {
        JSONArray ranking = new JSONArray();

        HashMap<Integer, ArrayList<String>> rankings = new HashMap<>();
        int round = 0;

        while (playerList.size() > 1){
            ArrayList<GoPlayer> tempNewPlayerList = new ArrayList<>();
            ArrayList<String> loserList = new ArrayList<>();

            for (int i =0; i<playerList.size(); i+=2){
                GoPlayer playerOne = playerList.get(i);
                GoPlayer playerTwo = playerList.get(i+1);
                String winner = playOneGame(playerOne, playerTwo);
                if (winner.equals(playerOne.getPlayerName())) {
                    tempNewPlayerList.add(playerOne);
                    loserList.add(playerTwo.getPlayerName());
                }
                else {
                    tempNewPlayerList.add(playerTwo);
                    loserList.add(playerOne.getPlayerName());
                }
            }
            rankings.put(round, loserList);
            playerList = tempNewPlayerList;
            round ++;
        }

        //loop through the list of remote players and try to register
        //keep track of how many fail register. Add this number to the number of

        //TODO: make rankings json form to print then return it
        return ranking;
    };

    GoPlayer playOneGame(GoPlayer playerOne, GoPlayer playerTwo) throws Exception {
        Game referee = new Game();
        referee.registerPlayer(playerOne);
        referee.registerPlayer(playerTwo);
        JSONArray winnerArray = referee.playGame();
        if (winnerArray.size() == 2) {
            double rand = Math.random();
            if (rand < 0.5) {
                winnerArray.remove(0);
            } else {
                winnerArray.remove(1);
            }
        }
        String winner = winnerArray.get(0).toString();

        return winner;
    };
}
