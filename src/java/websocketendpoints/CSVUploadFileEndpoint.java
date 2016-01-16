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
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author School
 */
@ServerEndpoint("/fileUploadEndpoint")
public class CSVUploadFileEndpoint {
    Socket socket = null;

    @OnMessage
    public String onMessage(String message) {
        if(message.equals("start")){
            createNewSocket();
        }else if(message.equals("end")){
            closeSocket();
        }
        sendLineToInsertApp(message);
        
        return message;
    }

    private void sendLineToInsertApp(String message) {
        try {           
            socket.getOutputStream().write(message.getBytes(Charset.forName("UTF-8")));
            socket.getOutputStream().flush();
            //TODO komt niet verder dan dit
        } catch (UnknownHostException ex) {
            Logger.getLogger(CSVUploadFileEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVUploadFileEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void createNewSocket() {
        
        try {
            if(socket == null || socket.isClosed()){
            InetAddress address = InetAddress.getByName("localhost");
            int port = 50;
            Socket socket = new Socket(address, port);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(CSVUploadFileEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVUploadFileEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }

    private void closeSocket() {
        try {
            if(!socket.isClosed() && !socket.isOutputShutdown()){
            socket.getOutputStream().close();
            socket.getOutputStream().flush();
            socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVUploadFileEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
