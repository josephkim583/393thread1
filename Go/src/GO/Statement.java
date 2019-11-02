package GO;

import org.json.simple.JSONArray;

public interface Statement {
    // Query Statements
    boolean occupied(Point point) throws Exception;
    boolean occupies(Stone stone, Point point) throws Exception;
    boolean reachable(Point point, MaybeStone stone) throws Exception;

    // Command Statements
    Object place(Stone stone, Point point) throws Exception;
    Object remove(Stone stone, Point point) throws Exception;
    JSONArray getPoints(MaybeStone stone);
}
