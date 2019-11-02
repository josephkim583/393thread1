package GO;

import java.io.IOException;

public class Stone {
    private String stone;

    public String getStone() {
        return stone;
    }

    public Stone(String str) throws IOException {
        if (str.equals("W")|| str.equals("B")) {
            stone = str;
        } else {
            throw new IOException("Wrong stone value");
        }
    }

    public MaybeStone toMaybeStone() throws IOException {
        return new MaybeStone(stone);
    }

    public Stone opponent() throws IOException {
        if (stone.equals("W")) {
            return new Stone("B");
        } else {
            return new Stone ("W");
        }
    }

}
