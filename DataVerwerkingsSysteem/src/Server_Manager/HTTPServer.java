/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server_Manager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 *
 * @author Elize
 */
public class HTTPServer extends ServerSocket {

    public HTTPServer() throws IOException {
    }

    public HTTPServer(int port) throws IOException {
        super(port);
    }

    public HTTPServer(int port, int backlog) throws IOException {
        super(port, backlog);
    }

    public HTTPServer(int port, int backlog, InetAddress bindAddr) throws IOException {
        super(port, backlog, bindAddr);
    }
    
    
    
}
