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
    private static Set<LogInSession> sessions = new HashSet<>();
    private static Set<DataRecieverSession> dataRecieverSessions = new HashSet<>();
    
    public static void addLogInSession(Session session, String username, String password){
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
    
    public static void removeLogInSession(Session session) {
        for(LogInSession loginSession: sessions){
            if(loginSession.getSession().equals(session)){
                sessions.remove(loginSession);
                break;
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
            JSONObject returnedMessageJSON = new JSONObject(returnMessage);
            String returndedUsername = returnedMessageJSON.getString("username");
            if(returndedUsername.equals("Not a user!")){
                user = null;
            }else{
             user = returndedUsername;
            }            
        } catch (JSONException ex) {
            System.out.println(ex);
        }
         
         return user;
     }
     
     public static void addDataRecieverSession(Session session){
         dataRecieverSessions.add(new DataRecieverSession(session));
         
     }
     
     /**
      * After the first DataRecieverSession has recieved an unitId, the DataGetterThread should be started.
      * @param unitId
      * @param session 
      */
     public static void setUnitIdForDataRecieverSession(String unitId, Session session){
         for(DataRecieverSession dataRecieverSession: dataRecieverSessions){
            if(dataRecieverSession.getSession().equals(session)){
                dataRecieverSession.setUnitID(unitId);
                break;
             }   
         }
         
         DataGetterHandler.startIfNeccesary();
     }
     
    public static void removeDataRecieverSession(Session session){
            for(DataRecieverSession dataRecieverSession: dataRecieverSessions){
            if(dataRecieverSession.getSession().equals(session)){
                dataRecieverSessions.remove(dataRecieverSession);
                break;
             }   
         }
        DataGetterHandler.stopIfNeccesary();
    }
     

    public static Set<DataRecieverSession> getDataRecieverSessions() {
        return dataRecieverSessions;
    }
     
     
     
     
}
