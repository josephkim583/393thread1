package GO;

import org.json.simple.JSONArray;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class tournamentAdmin {
    public static void main(String[] args) throws Exception {
        if (args.length == 0){
            System.out.println("not enough arguments");
            System.exit(0);
        }
        ConfigReader configReader = new ConfigReader();
        InetAddress addr = InetAddress.getByName(configReader.ipAddress());
        ServerSocket ss = new ServerSocket(configReader.port(), 50, addr);
        int playerNum = Integer.parseInt(args[0]);
        ArrayList<GoPlayer> listOfPlayers = new ArrayList<>();
        for (int i = 0; i < playerNum; i++) {
            Socket s = ss.accept();
            ProxyPlayer proxyPlayer = new ProxyPlayer(s);
            listOfPlayers.add(proxyPlayer);
        }

        tournamentAdmin admin = new tournamentAdmin();
        String mode = args[1];
        int closestPowerOfTwo = admin.closestPowerOfTwo(playerNum);
        int numNewPlayers = closestPowerOfTwo - playerNum;

        for (int i = 0; i < numNewPlayers ; i++) {
            Player defaultPlayer = new Player();
            listOfPlayers.add(defaultPlayer);
        }

        if (mode.equals("-league")) {
            System.out.println(admin.league(listOfPlayers));
        } else if (mode.equals("-cup")) {
            System.out.println(admin.cup(listOfPlayers));
        }
        ss.close();
    }

     int closestPowerOfTwo(int n){
        if (n == 0){
            return 2;
        }
        int closestPower = 2;
        while (closestPower < n){
            closestPower = closestPower*2;
        }
        return closestPower;
    }

    String league(ArrayList<GoPlayer> playerList) throws Exception {
        JSONArray ranking = new JSONArray();
        ArrayList<String> cheaters = new ArrayList<>();
        HashMap<GoPlayer, HashMap<GoPlayer, Integer>> currentStanding = new HashMap<>();
        //initialize currentStanding
        for (int i = 0; i < playerList.size(); i++) {
            HashMap<GoPlayer, Integer> initialWinLoss = new HashMap<>();
            for (int j = 0; j < playerList.size(); j++) {
                if (i != j){
                    initialWinLoss.put(playerList.get(j), 0);
                }
            }
            currentStanding.put(playerList.get(i), initialWinLoss);
        }

        //play the league
        for (int i = 0; i < playerList.size(); i++) {
            for (int j = i+1; j < playerList.size(); j++) {
                GoPlayer playerOne = playerList.get(i);
                GoPlayer playerTwo = playerList.get(j);
                HashMap<String, GoPlayer> gameResult = playOneGame(playerOne,playerTwo);

                //In case there was a cheater
                if (gameResult.get("cheater") != null) {
                    GoPlayer cheater = gameResult.get("cheater");
                    cheaters.add(cheater.getPlayerName());
                    Player newPlayer = new Player();
                    //TODO: check this indexOF thing works by sysout later
                    int indexToReplace = playerList.indexOf(cheater);
                    playerList.set(indexToReplace, newPlayer);
                    //initialize currentStanding for new Player
                    HashMap<GoPlayer, Integer> newPlayerWinLoss = new HashMap<>();
                    for (int z = 0; z < playerList.size(); z++){
                        if (z != indexToReplace){
                            newPlayerWinLoss.put(playerList.get(z), 0);
                        }
                    }
                    currentStanding.put(newPlayer, newPlayerWinLoss);

                    //add point to everyone that lost to cheater
                    for (GoPlayer player : currentStanding.get(cheater).keySet()){
                        if (currentStanding.get(cheater).get(player) == 1){
                            currentStanding.get(player).put(cheater, 1);
                        }
                    }

                    //delete cheater from currentStanding
                    currentStanding.remove(cheater);
                }
                //NO cheater
                else{
                    GoPlayer winner = gameResult.get("winner");
                    GoPlayer loser = gameResult.get("loser");

                    currentStanding.get(winner).put(loser, 1);
                    currentStanding.get(loser).put(winner, -1);
                }
            }
        }

        HashMap<Integer, ArrayList<String>> rankingBoard = new HashMap<>();
        HashMap<Integer, ArrayList<String>> finalRanking = new HashMap<>();
        for(GoPlayer player : currentStanding.keySet()) {
            int total = 0;
            for(GoPlayer opponent : currentStanding.get(player).keySet()) {
                total += currentStanding.get(player).get(opponent);
            }
            if (rankingBoard.get(total) == null) {
                rankingBoard.put(total, new ArrayList<String>());
            }
            rankingBoard.get(total).add(player.getPlayerName());
        }
        SortedSet<Integer> keysOfRankingBoard = new TreeSet<>(rankingBoard.keySet());
        int counter = 1;
        for (Integer key : keysOfRankingBoard){
            finalRanking.put(counter, rankingBoard.get(key));
            counter += 1;
        }
        finalRanking.put(0, cheaters);
        return finalRanking.toString();
    };

    String cup(ArrayList<GoPlayer> playerList) throws Exception {
        JSONArray ranking = new JSONArray();

        HashMap<Integer, ArrayList<String>> rankings = new HashMap<>();
        int round = 0;

        while (playerList.size() > 1){
            ArrayList<GoPlayer> tempNewPlayerList = new ArrayList<>();
            ArrayList<String> loserList = new ArrayList<>();

            for (int i =0; i<playerList.size(); i+=2){
                GoPlayer playerOne = playerList.get(i);
                GoPlayer playerTwo = playerList.get(i+1);
                HashMap<String, GoPlayer> gameResult = playOneGame(playerOne, playerTwo);
                tempNewPlayerList.add(gameResult.get("winner"));
                loserList.add(gameResult.get("loser").getPlayerName());
            }
            rankings.put(round, loserList);
            playerList = tempNewPlayerList;
            round ++;
        }
        ArrayList<String> finalWinner = new ArrayList<>();
        finalWinner.add(playerList.get(0).getPlayerName());
        rankings.put(round, finalWinner);
        //loop through the list of remote players and try to register
        //keep track of how many fail register. Add this number to the number of

        //TODO: make rankings json form to print then return it
        return rankings.toString();
    };

    HashMap<String, GoPlayer> playOneGame(GoPlayer playerOne, GoPlayer playerTwo) throws Exception {
        Game referee = new Game();
        referee.registerPlayer(playerOne, playerTwo);
        referee.playGame();
        HashMap<String, GoPlayer> gameResult = referee.getGameResult();
        return gameResult;
    };
}
