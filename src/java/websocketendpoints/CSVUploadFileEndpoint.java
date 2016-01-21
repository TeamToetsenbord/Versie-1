/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocketendpoints;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONException;
import org.json.JSONObject;
import session.ServerManager;

/**
 *
 * @author School
 */
@ServerEndpoint("/fileUploadEndpoint")
public class CSVUploadFileEndpoint {
    Socket socket = null;

    @OnMessage
    public String onMessage(String message) {
        try{
        return ServerManager.sendAndRecieveMessageToServer(new JSONObject(message));
        } catch (JSONException ex) {
            return "Sorry, not an JSONObject!";
        }
    }
    
}
