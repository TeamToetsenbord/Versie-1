/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server_Manager;

import DatabaseClasses.Database_Manager;
import Readers.CSVFileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Elize
 */
public class WebServerCommunicationManager extends Thread{
        
    ServerSocket serverSocket; 
    
    public WebServerCommunicationManager(){
        createServerSocket();
        this.start();
    }
        
     private void createServerSocket(){
        if(serverSocket == null || serverSocket.isClosed()){
            try {
                   InetAddress address = InetAddress.getByName("localhost");
                   int port = 50;
                   int maxConnections = 1000;
                   serverSocket = new ServerSocket(port, maxConnections, address);
               } catch (UnknownHostException ex) {
                   System.out.println(ex);
               } catch (IOException ex) {
                   System.out.println(ex);
            }
        }
    
    }
     
    private void getMessagesFromWebApp(){
        String inputMessage = "";
        Socket socket = null;
        try {
            socket = serverSocket.accept();
            boolean recieved = false;
            
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            //While the client socket has not responded het, wait for it to send a message
            while(!recieved){
            while(in.ready()){
                inputMessage += (char)in.read();
                recieved = true;
            }
            }
            
            System.out.println("Message recieved: " + inputMessage + "(" + inputMessage.length() + ")");    
            JSONObject json = new JSONObject(inputMessage);
            handleJSONMessage(json, socket);
               
             
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (JSONException ex) {
            System.out.println(ex + inputMessage);
            returnMessageToSocket("Error", socket);
           
        }
        
    }
    @Override 
    public void run(){
       while(true){
       getMessagesFromWebApp();
       }
    }
    
    /**
     * Method for handling the messages recieved over the socket connection.
     * @param json: the message
     * @param socket: the client socket
     */
    private void handleJSONMessage(JSONObject json, Socket socket){
        try{
        if(!json.has("type")){
            JSONObject returnMessage = new JSONObject();
            returnMessage.put("type", "error");
            returnMessage.put("error", "The jsonObject does not have the \"type\" attribute!");
            returnMessageToSocket(returnMessage.toString(), socket);
        }else{
            String type = json.getString("type");
            switch(type){
                case "logIn":
                    doLogIn(json, socket);
                    break;
                case "reportData":
                    getReportData(json, socket);
                    break;
                 //TODO new file, live stream?   
                    
            }
           }
        } catch (JSONException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Method for checking if the login credentials really are stored in the database.
     * @param json: a login JSON object
     * @param socket: the socket that send the message.
     * @throws JSONException
     * @throws IOException 
     */
    private void doLogIn(JSONObject json, Socket socket) throws JSONException, IOException {
        String username = json.getString("username");
        String password = json.getString("password");
        String returnUserString = Database_Manager.logIn(username, password);
        if(returnUserString == null){
            returnUserString = "Not a user";
        }
        returnMessageToSocket(returnUserString, socket);
    }
    
    /**
     * This method returns a message to the original socket that send the first message
     * @param message: message to return
     * @param socket: the original socket
     * @throws IOException 
     */
    private void returnMessageToSocket(String message, Socket socket){
        try {
            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            out.write(message);
            out.flush();            
        } catch (IOException ex) {
            System.out.println("Return message could not be send!");
            System.out.println(ex);
        }
    }

    private void getReportData(JSONObject json, Socket socket) throws JSONException {
        String reportType = json.getString("reportType");
        String unitId = json.getString("unitId");
        JSONObject jsonReport = Database_Manager.getLatestReportData(reportType, unitId);
        returnMessageToSocket(jsonReport.toString(), socket);
    }
}
