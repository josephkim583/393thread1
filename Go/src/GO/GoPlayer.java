package GO;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;

public interface GoPlayer {
    public String register(@Nullable String name) throws IOException;
    public boolean receiveStones(Stone stone) throws IOException;
    public String makeAMove(ArrayList<Board> boards) throws Exception;
    public String getPlayerName();
}
