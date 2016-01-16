/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server_Manager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


/**
 *
 * @author Elize
 */


@ServerEndpoint("/insert")
public class WebServerEndPoint {
    
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    
    @OnOpen
        public void open(Session session) {
            System.out.println("Open");
            peers.add(session);
    }

    @OnClose
        public void close(Session session) {
            peers.remove(session);
    }

    @OnError
        public void onError(Throwable error) {
            System.out.println("error: " + error.getMessage());
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        System.out.println("Message: " + message);
    }
    
    
}
