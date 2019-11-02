package GO;

import java.io.IOException;

public class MaybeStone {
    private String maybeStone;

    public String getMaybeStone() {
        return maybeStone;
    }

    public MaybeStone(String str) throws IOException {
        if (str.equals("W")|| str.equals("B") || str.equals(" ")) {
            maybeStone = str;
        } else {
            throw new IOException("Wrong maybeStone value");
        }
    }

}
