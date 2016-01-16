/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputStreams;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.getProperties;
import java.util.Arrays;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Elize
 */
public class MqttReciever extends Thread implements MqttCallback {

    private String url = "";
    private String clientId = "";
    private String user = "";
    private String pass = "";
    private MqttClient client;
    private int connectionRetries = 5;
    private String[] topics = {"POSITIONS", "MONITORING", "EVENTS", "CONNECTIONS"};
    

    public MqttReciever(){
        getPropertiesFile();
        this.start();
    }
    
    @Override
    public void run(){
        this.connectAndListen();
    }
    
    /***
     * Connect to the broker and start listening.
     */
    public void connectAndListen() {
        System.out.println("Connect to message broker at: " + url);

        try {
            client = new MqttClient(url, clientId);
            client.connect(createConnectionProperties(user, pass));
            client.setCallback(this);
            Arrays.stream(topics).forEach(t -> {
                try {
                    client.subscribe(t);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            });


        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("Connection failed." + e.getMessage());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MqttReciever.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /***
     * Is called when connection is lost.
     *
     * @param cause
     */
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost." + cause.getMessage());

        connectionRetries--;
        if (connectionRetries != 0) {
            System.out.println("Retrying establishing a connection.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connectAndListen();
        }
    }

    /***
     * Callback methods for each received method.
     *
     * @param topic   the topic where the message originated from.
     * @param message the contents of the actual message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        
        CityGisLiveMessageProcessor.processMessage(topic, message.toString());
       
    }

    /***
     * Is not called in practice.
     *
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery complete." + token.toString());
    }

    private MqttConnectOptions createConnectionProperties(String user, String password) {
        MqttConnectOptions connectOptions = new MqttConnectOptions();

        connectOptions.setCleanSession(true);
        if (user != null && user.length() > 0) {
            connectOptions.setUserName(user);
            connectOptions.setPassword(password.toCharArray());
        }
        return connectOptions;
    }

    private void getPropertiesFile() {
        File configFile = new File("liveStreamConfig.properties");
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            this.url = props.getProperty("url");
            this.user = props.getProperty("user");
            this.pass = props.getProperty("pass");
            this.clientId = props.getProperty("clientId");
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}

    
    
   
    
    
    

