package GO;

import jdk.internal.util.xml.impl.Input;
import org.json.simple.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class ProxyPlayer{
    public static void main(String[] args) throws IOException {
        Player player = new Player();
        ServerSocket ss = new ServerSocket(8001);
        Socket s = ss.accept();

        System.out.print("client connected");

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();
        System.out.print("gets here fucker: " + str);
    }
}
