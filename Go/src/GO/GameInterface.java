package GO;

import java.io.IOException;
import java.util.ArrayList;

public interface GameInterface {
    public String registerPlayer(String name) throws IOException;
    public ArrayList<Board> makeMove(Point point) throws Exception;
    public ArrayList<Board> pass();
}
