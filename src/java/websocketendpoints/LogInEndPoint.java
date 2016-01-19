/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocketendpoints;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author School
 */
@ServerEndpoint("/login")
public class LogInEndPoint {

    @OnMessage
    public String onMessage(String message) {
        return null;
    }
    
    @OnOpen()
    public void onOpen(){
        System.out.println("Open");
    
    }
    
}
