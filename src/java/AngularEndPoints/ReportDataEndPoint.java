/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AngularEndPoints;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONException;
import org.json.JSONObject;
import session.ServerManager;

/**
 *
 * @author Elize
 */

@ServerEndpoint("/reportdataendpoint")
public class ReportDataEndPoint{
  
    @OnMessage
    public String onMessage(String message, Session session){
        
        try {
            JSONObject json = new JSONObject();
            json.put("type", "getReport");
            json.put("reportType", "CityGis");
            json.put("unitId", "");
            return ServerManager.sendAndRecieveMessageToServer(json);
        } catch (JSONException ex) {
           System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        return "Error";
    }
    
    
}
