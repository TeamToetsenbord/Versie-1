/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Elize
 */
public class SessionHandler {
    private final Set<LogInSession> sessions = new HashSet<>();
    
    public void addSession(Session session, String username, String password){
        try {
        Object user = logIn(username, password);
        if(user != null){
           LogInSession newSession = new LogInSession(session);
           sessions.add(newSession); 
        }else{
            session.close();
        }
        } catch (IOException ex) {
            System.out.println("Sorry, we cannot close this socket: ");
            System.out.println(ex);
        }
        
    }
    
    public void removeSession(Session session) {
        for(LogInSession loginSession: sessions){
            if(loginSession.getSession().equals(session)){
                sessions.remove(loginSession);
            }
        }
    }
    
     private static Object logIn(String username, String password){
         Object user = null;
         try {
            
            JSONObject json = new JSONObject();
            json.put("type", "logIn");
            json.put("username", username);
            json.put("password", password);
            String returnMessage = ServerManager.sendAndRecieveMessageToServer(json);
            System.out.println("I got: " + returnMessage);
            //TODO test it!
            if(!returnMessage.equals("Not a user")){
                user = null;
            }            
        } catch (JSONException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
         
         return user;
     }
     
}
