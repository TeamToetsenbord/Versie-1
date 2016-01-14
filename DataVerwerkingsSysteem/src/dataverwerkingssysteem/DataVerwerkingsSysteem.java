/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataverwerkingssysteem;

import DatabaseClasses.EntityClasses.Car;
import DatabaseClasses.EntityClasses.CarPositionData;
import DatabaseClasses.Database_Manager;
import DatabaseClasses.EntityClasses.CarPositionDataPK;
import DatabaseClasses.EntityClasses.EntityClass;
import DatabaseClasses.InsertThread;
import DatabaseClasses.EntityClasses.User;
import InputStreams.MqttReciever;
import Readers.PositionsCSVReaderThread;
import Server_Manager.HTTPReciever;
import UI.User_Interface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Elize
 */
public class DataVerwerkingsSysteem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        
        User_Interface ui = new User_Interface();
        Database_Manager dbManager = new Database_Manager(ui); 
        MqttReciever m = new MqttReciever();
        
    }
    
}
