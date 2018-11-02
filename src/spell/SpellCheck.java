package spell;

import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;

public class SpellCheck {

    static String host = "https://api.cognitive.microsoft.com";
    static String path = "/bing/v7.0/spellcheck";

    // NOTE: Replace this example key with a valid subscription key.
    static String key = "0d703a2d476442039e918b2ac2a13b89";

    static String mkt = "en-US";
    static String mode = "proof";

    public static String check (String text) throws Exception {
        String params = "?mkt=" + mkt + "&mode=" + mode;
        URL url = new URL(host + path + params);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", "" + text.length() + 5);
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", key);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes("text=" + text);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream()));
        String completeLine="";
        String line;
        while ((line = in.readLine()) != null) {
            completeLine=completeLine+line;
        }
        in.close();
        
        return completeLine;
    }

}
