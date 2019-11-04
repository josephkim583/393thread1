package GO;

import java.io.IOException;

public interface GameInterface {
    public String registerPlayer(String name) throws IOException;
    public Object[] makeMove(Point point) throws Exception;
    public Object[] pass() throws Exception;
}
