/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server_Manager;

import DatabaseClasses.Car;
import DatabaseClasses.Database_Manager;
import DatabaseClasses.EntityClass;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elize
 */
public class HTTPReciever extends Thread {

    public HTTPReciever() {
        this.start();
    }
    
    
    
    @Override
    public void run() {
       while(true){
           recieveAndInsertObject();
       }
    }

    //Method to recieve all messages from outside clients.
    private void recieveAndInsertObject(){
               
    int port = 8000;
    
    InputStream is = null;
    Socket s = null;
    ServerSocket ss = null;
        
        try {
        InetAddress address = InetAddress.getByName("localhost");
        System.out.println("Reading...");
        ss = new ServerSocket(port, 50, address);
        System.out.println("Reciever:" + ss.getInetAddress().getHostAddress() + " " + ss.getLocalPort());
        s = ss.accept();
        is = s.getInputStream();
            //Just read the plain text in the inputstream
            String readString = "";
            int readChar = 0;
            while((readChar = is.read()) != -1){
            readString += (char) readChar;
            }
            System.out.println("String:" + readString);
        
          //Read the objects in the inputstream, I don't think we are going to use this.
          //Maybe for inserting live data?
         
//        ObjectInputStream objectInput = new ObjectInputStream(is);  
//        String message1 = (String) objectInput.readObject();
//        Car message2 = (Car) objectInput.readObject();
//        System.out.println(message1 + "/n" +  message2.toString());
              
        }catch(Exception e){System.out.println(e);}
        finally{
        try {
            is.close();
            s.close();
            ss.close();
        } catch (IOException ex) {
            Logger.getLogger(HTTPReciever.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        }
    }
    
    
    //Just a test method, we are going to use php/angularJS for this
    public static void sentSignal(){
        
    try{
        
        Socket socket = new Socket("localhost", 8000);
        System.out.println("Client:" + socket.getInetAddress().getHostAddress() + " " + socket.getPort());
        OutputStream dataOutputStream = socket.getOutputStream();
        dataOutputStream.write("Hello".getBytes());
//        ObjectOutputStream objectOutput = new ObjectOutputStream(dataOutputStream);
      
        //objectOutput.writeObject(new String("Insert"));
        //objectOutput.writeObject(new Car("124536"));
        
        dataOutputStream.close();
        
    }catch(Exception ex){
        System.out.println(ex);
    }
    
    }
    
    
    
    

}
