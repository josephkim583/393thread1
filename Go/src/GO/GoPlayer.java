package GO;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;

public interface GoPlayer {
    String register(@Nullable String name) throws IOException;
    boolean receiveStones(Stone stone) throws IOException;
    String makeAMove(ArrayList<Board> boards) throws Exception;
    String endGame() throws IOException;
    String getPlayerName();
}
