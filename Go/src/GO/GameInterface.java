package GO;

import org.json.simple.JSONArray;

import java.io.IOException;

public interface GameInterface {
    public String registerPlayer(String name) throws IOException;
    public JSONArray makeMove(Point point) throws Exception;
    public JSONArray pass() throws Exception;
}
