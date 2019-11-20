package GO;

import jdk.internal.util.xml.impl.Input;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class gameAdmin {
    public static void main(String[] args) throws Exception {
        Game referee = new Game();
        ConfigReader config = new ConfigReader();
        JSONArray winners = new JSONArray();

        ServerSocket ss = new ServerSocket(config.port());

        Player localPlayer = new Player();

        //register local player (will have black stone)
        localPlayer.register("localPlayer");
        Stone localPlayerStone = new Stone("B");
        localPlayer.receiveStones(localPlayerStone);
        referee.registerPlayerOne(localPlayer);

        //register the Remote Player after connecting
        ProxyPlayer proxyPlayer = new ProxyPlayer(ss);
        JSONArray registerArray = new JSONArray();
        JSONArray receiveStoneArray = new JSONArray();
        receiveStoneArray.add("receive-stones");
        receiveStoneArray.add("W");
        registerArray.add("register");
        proxyPlayer.register(registerArray);
        proxyPlayer.receiveStones(receiveStoneArray);
        referee.registerPlayerTwo(proxyPlayer);

        winners = referee.playGame();
        ss.close();
        System.out.println(winners);

//        while(true){
//            referee.playgame();
//            s = ss.accept();
//            in = new InputStreamReader(s.getInputStream());
//            bf = new BufferedReader(in);
//            if (bf.read() == -1){
//                winners.add(localPlayerName);
//                ss.close();
//                break;
//            }
//
//            if (referee.getCurrentStoneColor() == "B"){
//                String pointString = localPlayer.makeAMove(referee.boardHistory);
//                if (pointString == "pass"){
//                    referee.pass();
//                }
//                else{
//                    Point localPlayerMove = new Point(pointString);
//                    referee.makeMove(localPlayerMove);
//                }
//            }
//            else {
//                JSONArray makeAMoveArray = new JSONArray();
//                makeAMoveArray.add("make-a-move");
//                makeAMoveArray.add(referee.boardHistory);
//                String pointString = proxyPlayer.makeAMove(outputWriter, bf, makeAMoveArray);
//                if (pointString == "pass"){
//                    referee.pass();
//                }
//                else{
//                    Point remotePlayerMove = new Point(pointString);
//                    referee.makeMove(remotePlayerMove);
//                }
//            }
//            if (referee.gameEnded) {
//                winners = referee.winner;
//            }
//            System.out.println(referee.boardHistory.get(0));
//        }
    }
}
