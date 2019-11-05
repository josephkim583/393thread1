package GO;

import org.json.simple.JSONArray;

import java.io.IOException;

public interface GameInterface {
    public void registerPlayer(String name) throws IOException;
    public void makeMove(Point point) throws Exception;
    public void pass() throws Exception;
}
