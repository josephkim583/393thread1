package GO;

public interface GameInterface {
    public void registerPlayer(String name);
    public void makeMove(Point point) throws Exception;
    public void pass();
}
