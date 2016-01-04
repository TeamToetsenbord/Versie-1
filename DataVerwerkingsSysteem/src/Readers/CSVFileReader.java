/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.CSVInsertManager;
import DatabaseClasses.EntityClasses.CarPositionData;
import DatabaseClasses.EntityClasses.CarStatusEvent;
import DatabaseClasses.Database_Manager;
import DatabaseClasses.EntityClasses.EntityClass;
import DatabaseClasses.EntityClasses.HsdpaConnection;
import DatabaseClasses.EntityClasses.OverallConnection;
import DatabaseClasses.EntityClasses.TcpClientConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ronald & Elize
 */
public class CSVFileReader extends Thread{
    
    private static final int TICKS_PER_SECOND = 60;
    public static boolean reading = false;

    public static boolean isReading() {
        return reading;
    }

    public static void setReading(boolean reading) {
       
        CSVFileReader.reading = reading;
         if(!reading){
            CSVInsertManager.createNewInsertThreadIfNeeded();
        }
    }
    
    public CSVFileReader(){
        
    }
    
    protected static Date getDateByString(String string){
    
        Date dateFormatted = null; 
        try {
            dateFormatted = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" )).parse(string);
        } catch (ParseException ex) {
            System.out.println(ex);
        }finally{
        return dateFormatted;
        }
    }
    
    /**
     * Calculate the average of the given sum.
     * Used by the readAndInsertMonitoringCSV file.
     * @param sum
     * @return the average
     */
    protected static long calculateAverageByTicks(long sum){
        return sum / TICKS_PER_SECOND;
    }
    
    /**
     * @Author: someone from internet.
     * Website: http://forum.geocaching.nl/index.php?showtopic=7886
     * @param rx
     * @param ry
     * @return the longitude, latitude in a double[].
     */
    protected static double[] calculateLongAndLatFromRxAndRy(long rx, long ry){
        
        float x =  rx;
        float y = ry;
           double dX = (x - 155000.0) * Math.pow(10, -5);
	double dY = (y - 463000.0) * Math.pow(10, -5);

	double somN = ((3235.65389 * dY) + (-32.58297 * Math.pow(dX, 2)) + 
                (-0.2475 * Math.pow(dY, 2)) + (-0.84978 * Math.pow(dX, 2) * dY) + 
                (-0.0655 * Math.pow(dY, 3)) + (-0.01709 * Math.pow(dX, 2) * Math.pow(dY, 2)) + 
                (-0.00738 * dX) + (0.0053 * Math.pow(dX, 4)) + 
                (-0.00039 * Math.pow(dX, 2) * Math.pow(dY, 3)) + 
                (0.00033 * Math.pow(dX, 4) * dY) + (-0.00012 * dX * dY));
	double somE = (5260.52916 * dX) + (105.94684 * dX * dY) + 
                (2.45656 * dX * Math.pow(dY, 2)) + (-0.81885 * Math.pow(dX, 3)) + 
                (0.05594 * dX * Math.pow(dY, 3)) + (-0.05607 * Math.pow(dX, 3) * dY) + 
                (0.01199 * dY) + (-0.00256 * Math.pow(dX, 3) * Math.pow(dY, 2)) + 
                (0.00128 * dX * Math.pow(dY, 4)) + (0.00022 * Math.pow(dY, 2)) + 
                (-0.00022 * Math.pow(dX, 2)) + (0.00026 * Math.pow(dX, 5));

	double latitude = 52.15517 + (somN / 3600);
	double longitude = 5.387206 + (somE / 3600);
   
        
        double[] latAndLongArray = {latitude, longitude};
        return latAndLongArray;
    }
    
    
    public static void readFileByType(String type, String path){
        System.out.println("Start: " + new Date());
        switch(type){
            case "Positions":
               new PositionsCSVReaderThread(path).start();
               break;
            case "Events":
               new EventsCSVReaderThread(path).start();
                break;
            case "Monitoring":
                new MonitoringCSVReaderThread(path).start();
                break;
            case "Connections":
                new ConnectionsCSVReaderThread(path).start();
                break;
            
        }
        
    }
    
   
    
    
}
