/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server_Manager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Elize
 */
public class ClientSocketHandler extends Thread{

    Socket socket  = null;
    
    public ClientSocketHandler(Socket socket){
        this.socket = socket;
        this.start();
    }
    
    @Override
    public void run() {
        handleNewSocket();
    }

    private void handleNewSocket(){
        String inputMessage = "";
        try {
            boolean recieved = false;
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            //While the client socket has not responded het, wait for it to send a message
            while(!recieved){
            while(in.ready()){
                inputMessage += (char)in.read();
                recieved = true;
            }
            }
            
            JSONObject json = new JSONObject(inputMessage);
            WebServerCommunicationManager.handleJSONMessage(json, socket);
               
             
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (JSONException ex) {
            try {
                System.out.println(ex + inputMessage);
                JSONObject returnJSON = new JSONObject();
                returnJSON.put("type", "error");
                returnJSON.put("error", "Sorry, we could not parse your JSON message!");
                WebServerCommunicationManager.returnMessageToSocket("Error", socket);
            } catch (JSONException ex1) {
                System.out.println("Another JSON exception ocurred...");
                System.out.println(ex1);
            }
           
        }
    }
    
    
    
}
