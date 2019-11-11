package GO;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class socketTestDriver {
    public static void main(String[] args) throws IOException, ParseException {
        Socket s = new Socket("localhost", 8154);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        InputParser input = new InputParser();
        ArrayList<Object> parsedInput = input.parser();

        pr.println(parsedInput);
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        JSONParser parser = new JSONParser();
        JSONArray playerOutput = (JSONArray) parser.parse(str);
        s.close();
        System.out.println(playerOutput);
    }
}
