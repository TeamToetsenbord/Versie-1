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
        
    /**
     * Create a new serversocket, if neccesary.
     */
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
     
     /**
      * When a new socket connects to this application, handle it on a ClientSocketHandler thread. 
      */
    private void getMessagesFromWebApp(){
        Socket socket = null;
        try {
            socket = serverSocket.accept();
            new ClientSocketHandler(socket);
        } catch (IOException ex) {
            System.out.println(ex);
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
    public static void handleJSONMessage(JSONObject json, Socket socket){
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
                case "getReport":
                    getReportData(json, socket);
                    break;
                case "newCSVFile":
                    newCSVFile(json, socket);
                    break;
                case "getUnitIds":
                    getUnitIds(socket);
                default:
                    JSONObject returnMessage = new JSONObject();
                    returnMessage.put("type", "error");
                    returnMessage.put("error", "The jsonObject does not have an expected type!");
                    returnMessageToSocket(returnMessage.toString(), socket);
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
    private static void doLogIn(JSONObject json, Socket socket) throws JSONException, IOException {
        String username = json.getString("username");
        String password = json.getString("password");
        String returnUserName = Database_Manager.logIn(username, password);
        JSONObject returnJSON = new JSONObject();
        if(returnUserName == null){
            returnJSON.put("type", "error");
            returnJSON.put("error", "Not a user!");
        }else{
        returnJSON.put("type", "user");
        returnJSON.put("username", returnUserName);
        }
        returnMessageToSocket(returnJSON.toString(), socket);
    }
    
    /**
     * This method returns a message to the original socket that send the first message
     * @param message: message to return
     * @param socket: the original socket
     * @throws IOException 
     */
    public static void returnMessageToSocket(String message, Socket socket){
        try {
            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            out.write(message);
            out.flush();            
        } catch (IOException ex) {
            System.out.println("Return message could not be send!");
            System.out.println(ex);
        }
    }

    /**
     * This method reads a report json message, and return the generated report to the client.
     * @param json: the message json
     * @param socket: the client socket
     * @throws JSONException 
     */
    private static void getReportData(JSONObject json, Socket socket) throws JSONException {
        String reportType = json.getString("reportType");
        String unitId = json.getString("unitId");
        JSONObject jsonReport = Database_Manager.getLatestReportData(reportType, unitId);
        returnMessageToSocket(jsonReport.toString(), socket);
    }

    /**
     * This method reads a new .csv file JSON message, and sends it to the CSVFileReader.
     * After that, the client recieves a thank you message.
     * @param json: the JSON message 
     * @param socket: the client socket
     * @throws JSONException 
     */
    private static void newCSVFile(JSONObject json, Socket socket) throws JSONException {
       String path = json.getString("path");
       String CSVtype = json.getString("CSVFileType");
       CSVFileReader.readFileByType(CSVtype, path);
       JSONObject returnJSON = new JSONObject();
       returnJSON.put("type","message");
       returnJSON.put("message", "Thank you for your .csv file!");
       returnMessageToSocket(returnJSON.toString(), socket);
    }
    
    
    private static void getUnitIds(Socket socket) {
        JSONObject returnjson = Database_Manager.getAllUnitIdsJSON();
        returnMessageToSocket(returnjson.toString(), socket);
    }
}
