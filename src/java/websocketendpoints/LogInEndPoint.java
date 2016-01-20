/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocketendpoints;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONException;
import org.json.JSONObject;
import session.SessionHandler;

/**
 *
 * @author School
 */
@ServerEndpoint("/login")
public class LogInEndPoint {

    
    private SessionHandler sessionHandler = new SessionHandler();
    
    @OnMessage
    public String onMessage(String message, Session session) {
        try{
            JSONObject json = new JSONObject(message);
            if(json.has("type") && json.get("type").equals("logIn")){
                sessionHandler.addSession(session, json.getString("username"), json.getString("password"));
                return "Logged in";
            }else{
                return "not the right json: " + json;
            }
        } catch (JSONException ex) {
            System.out.println("These login credentials do not seem to be right...");
            System.out.println(ex);
        }
        return "Sorry, we do not understand your message";
    }
    
    @OnOpen()
    public void onOpen(Session session, EndpointConfig config) throws IOException{
        
      }
    
    @OnClose
    public void onClose(Session session, CloseReason closeReason){
        System.out.println("closing: " + session);
        sessionHandler.removeSession(session);
        System.out.println("Close");
    
    }
    
    
    
    
}
