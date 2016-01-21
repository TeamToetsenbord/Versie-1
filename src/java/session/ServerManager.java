/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Elize
 */
public class ServerManager {
    
    public static String sendAndRecieveMessageToServer(JSONObject json){
        
            try{
                boolean returned = false;
                Socket socket = new Socket("localhost", 50);
                OutputStreamWriter out = new OutputStreamWriter(
                        socket.getOutputStream(), StandardCharsets.UTF_8);
                out.write(json.toString());
                out.flush();
                System.out.println("Send: " + json);
                
                InputStreamReader in = new InputStreamReader(socket.getInputStream());
                
                String returnMessage = "";
                //While the server has not responded het, wait for it to respond
                while(!returned){
                    while(in.ready()){
                        returnMessage += (char)in.read();
                        returned = true;
                    }
                }
                
                socket.close();
                try{
                    JSONObject returnJSON = new JSONObject(returnMessage);
                    String type = returnJSON.getString("type");
                    if(type.equals("error")){
                        return returnJSON.getString("error");
                    }else if(type.equals("message")){
                        return returnJSON.getString("message");
                    }else if(type.equals("user")){
                        return returnJSON.getString("username");
                    }else{
                        return returnMessage;
                    }
                    
                }catch(JSONException ex){
                    return returnMessage;
                }
                
                
                
            }catch(IOException ex){
                String returnErrorMessage = "Could not connect to the server...";
                System.out.println(returnErrorMessage);
                System.out.println(ex);
                return returnErrorMessage;
            }
        
            
    
    }
}
