package GO;

import org.json.simple.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class socketTestDriver {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 8001);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("PENISSSS");
        pr.println(jsonArray);
        pr.flush();
//        ProxyPlayer proxyPlayer = new ProxyPlayer();
//        proxyPlayer.readFromSocket();
        System.out.println("I CAME");
    }
}
