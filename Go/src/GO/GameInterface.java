package GO;

import org.json.simple.JSONArray;

import java.io.IOException;

public interface GameInterface {
    void makeMove(Point point) throws Exception;
    void pass() throws Exception;
}
