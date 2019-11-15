package GO;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class ConfigReader {
    private String ipAddress;
    private int port;
    private int depth;
    public ConfigReader() throws IOException, ParseException {

        String goConfigData = getConfigData("go.config");
        String goPlayerData = getConfigData("go-player.config");

        JSONParser parser = new JSONParser();
        JSONObject configData = (JSONObject) parser.parse(goConfigData);
        JSONObject playerData = (JSONObject) parser.parse(goPlayerData);
        ipAddress = configData.get("IP").toString();
        port = ((Long) configData.get("port")).intValue();
        depth = ((Long) playerData.get("depth")).intValue();
    }

    private String getConfigData(String fileName) throws IOException {
        InputStream in = getClass().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String currentLine = reader.readLine();
        reader.close();
        return currentLine;
    }

    public int port() {
        return port;
    }

    public String ipAddress() {
        return ipAddress;
    }

    public int depth() {
        return depth;
    }
}
