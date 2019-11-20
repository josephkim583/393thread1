package GO;

import com.sun.security.ntlm.Server;
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
    private String proxyPlayerName;
    private Socket s;
    private InputStreamReader in;
    private BufferedReader bf;
    private PrintWriter outputWriter;

    public ProxyPlayer(Socket s) throws IOException {
        this.s = s;
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
    }

    public String getProxyPlayerName() {
        return proxyPlayerName;
    }

    //TODO: does it need to return the name?
    public String register(JSONArray commandArray) throws IOException {
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        String str = this.bf.readLine();
        proxyPlayerName = str;
        return str;
    }

    //TODO: does it need to return?
    public boolean receiveStones(JSONArray commandArray) throws IOException {
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        String str = bf.readLine();
        while (str == null) {
            str = bf.readLine();
        }
        return Boolean.valueOf(str);
    }

    public String makeAMove(JSONArray commandArray) throws IOException {
        this.outputWriter.println(commandArray);
        this.outputWriter.flush();
        String str = bf.readLine();
        return str;
    }

    public void closeConnections() throws IOException {
        this.s.close();
        this.in.close();
        this.bf.close();
        this.outputWriter.close();
    }


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
    public void openConnections() throws IOException {
        this.in = new InputStreamReader(this.s.getInputStream());
        this.bf = new BufferedReader(this.in);
        this.outputWriter = new PrintWriter(this.s.getOutputStream());
    }
}