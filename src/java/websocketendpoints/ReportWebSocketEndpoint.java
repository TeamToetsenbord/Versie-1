/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocketendpoints;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Elize
 */
@ServerEndpoint("/reportEndpoint")
public class ReportWebSocketEndpoint {

    @OnMessage
    public String onMessage(String message) {
        
        try {
            JSONObject json = new JSONObject(message);
            String type = json.getString("reportType");
            String unitId = json.getString("unitId");
            String fileName = type + System.nanoTime();
            String projectPath = null;
            String fileURL = "download/";
            //TODO get the right report!
            //Process p = Runtime.getRuntime().exec("python script.py " + filename + filetype + filepath);
            //TODO set the report file name to:
            //TODO maybe set the download path in a properties file?
            JSONObject returnJson = new JSONObject();
            returnJson.put("reportType", type);
            returnJson.put("path", fileURL + fileName);
            return returnJson.toString();
        } catch (JSONException ex) {
            //return "{\"error\":\"Sorry, not a JSON message!\"}";
             return "{\"error\":" + ex + "}";
        }
        
    }
    
    
}
