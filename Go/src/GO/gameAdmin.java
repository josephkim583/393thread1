package GO;

import org.json.simple.JSONArray;

import java.io.IOException;

public class gameAdmin {
    public static void main(String[] args) throws Exception {
        Game referee = new Game();
        ProxyPlayer proxyPlayer = new ProxyPlayer(9876);
        Player localPlayer = new Player();

        //register local player (will have black stone)
        referee.registerPlayer("localPlayer");
        Stone localPlayerStone = new Stone("B");
        localPlayer.receiveStones(localPlayerStone);

        //register the Remote Player after connecting
        //TODO: check for connnection
        JSONArray registerArray = new JSONArray();
        JSONArray receiveStoneArray = new JSONArray();
        receiveStoneArray.add("W");
        registerArray.add("register");
        String remotePlayerName = proxyPlayer.register(registerArray);
        proxyPlayer.receiveStones(receiveStoneArray);
        referee.registerPlayer(remotePlayerName);

        while(true){
            if (referee.getCurrentStoneColor() == "B"){
                String pointString = localPlayer.makeAMove(referee.boardHistory, 1);
            }
        }

    }
}
