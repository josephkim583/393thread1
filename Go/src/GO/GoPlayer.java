package GO;

import java.util.ArrayList;

public interface GoPlayer {
    public String register(String name);
    public boolean receiveStones(Stone stone);
    public String makeAMove(ArrayList<Board> boards, int distance) throws Exception;
}
