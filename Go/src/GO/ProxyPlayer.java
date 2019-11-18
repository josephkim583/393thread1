package GO;

import jdk.internal.util.xml.impl.Input;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ProxyPlayer {

    public ProxyPlayer() throws IOException {
    }

    public String register(PrintWriter pr, BufferedReader bf, JSONArray commandArray) throws IOException {
        pr.println(commandArray);
        pr.flush();
        String str = bf.readLine();
        return str;
    }

    public boolean receiveStones(PrintWriter pr, BufferedReader bf, JSONArray commandArray) throws IOException {
        pr.println(commandArray);
        pr.flush();
        String str = bf.readLine();
        while (str == null) {
            str = bf.readLine();
        }
        return Boolean.valueOf(str);
    }

    public String makeAMove(PrintWriter pr, BufferedReader bf, JSONArray commandArray) throws IOException {
        pr.println(commandArray);
        pr.flush();
        String str = bf.readLine();
        return str;
    }
}
//    public void closeConnections() throws IOException {
//        this.s.shutdownInput();
//        this.s.shutdownOutput();
//        this.s.close();
//        this.in.close();
//        this.bf.close();
//    }
//
//    public void closeAllConnections() throws IOException {
//        openConnections();
//        JSONArray shutdown = new JSONArray();
//        shutdown.add("shutdown");
//        this.pr.println(shutdown);
//        this.pr.flush();
//        closeConnections();
//    }
//
//    public void openConnections() throws IOException {
//        this.s = this.ss.accept();
//        this.in = new InputStreamReader(this.s.getInputStream());
//        this.bf = new BufferedReader(this.in);
//        this.pr = new PrintWriter(this.s.getOutputStream());
//    }
