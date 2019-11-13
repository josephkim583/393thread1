package GO;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class ConfigReader {
    private String ipAddress;
    private int port;
    public ConfigReader() throws IOException, ParseException {
        InputStream in = getClass().getResourceAsStream("go.config");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String currentLine = reader.readLine();
        reader.close();

        JSONParser parser = new JSONParser();
        JSONObject configData = (JSONObject) parser.parse(currentLine);
        ipAddress = configData.get("IP").toString();
        port = ((Long) configData.get("port")).intValue();
    }

}
