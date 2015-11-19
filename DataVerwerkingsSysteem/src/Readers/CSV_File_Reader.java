/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.CarPositionData;
import DatabaseClasses.CarStatusEvent;
import DatabaseClasses.Database_Manager;
import DatabaseClasses.EntityClass;
import DatabaseClasses.HsdpaConnection;
import DatabaseClasses.OverallConnection;
import DatabaseClasses.TcpClientConnection;
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
public class CSV_File_Reader {
    
    private static final String CONNECTIONS_FILE_PATH = "CSVFiles/Connections.csv";    
    private static final String EVENTS_FILE_PATH = "CSVFiles/Events.csv";
    private static final String MONITORING_FILE_PATH = "CSVFiles/Monitoring.csv";
    private static final String POSITIONS_FILE_PATH = "CSVFiles/Positions.csv";
    private static final int TICKS_PER_SECOND = 60;
    public static boolean reading = false;
    
    public CSV_File_Reader(){
        
    }
    
    public static void readAndInsertConnectionsCSV(String path){
       
        reading = true;
        if(path == null){
            path = CONNECTIONS_FILE_PATH;
        }
                  
            
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";"; //splits file bij elke ;
        try{
            br = new BufferedReader(new FileReader(path));
            br.readLine();
            while ((line = br.readLine()) != null){
                String [] lines = line.split(cvsSplitBy);
                String dateString = lines[0];
                String unitId = lines[1];
                String port = lines [2];
                String value = lines [3];
                boolean connected = (Integer.parseInt(value) != 0);
                
                Date dateFormatted = getDateByString(dateString);   
 
                OverallConnection overallConnection = new OverallConnection(dateFormatted, unitId, connected);
                Database_Manager.addObjectToPersistList(overallConnection);
                               
            }
        }
        catch (Exception ex){    
            System.out.println(ex);
            }
         finally{
            if(br != null){
                try{
                    br.close();
                }
                catch (Exception e){
                }
            }
            reading = false;
        }   
    }
    
     public static void readAndInsertEventsCSV(String path){
       
        reading = true;
        if(path == null){
            path = EVENTS_FILE_PATH;
        }
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";"; //splits file bij elke ;
        try{
            
            br = new BufferedReader(new FileReader(path));
            br.readLine();
            while ((line = br.readLine()) != null){
                String [] lines = line.split(cvsSplitBy);
                String dateString = lines[0];
                String unitId = lines[1];
                String port = lines [2];
                String value = lines [3];
                //System.out.println("info:   "+ dateString +"   " + unitId +"    "+ port +"    "+ value);
                
                Date dateFormatted = getDateByString(dateString); 
 
                CarStatusEvent carStatusEvent = new CarStatusEvent(unitId, dateFormatted, port, value );
                Database_Manager.addObjectToPersistList(carStatusEvent);
                
            }
        }
        catch (Exception ex){   
            System.out.println(ex);
            }
         finally{
            if(br != null){
                try{
                    br.close();
                }
                catch (Exception e){
                }
            }
             reading = false;
        }   
    }
     
      public static void readAndInsertMonitoringCSV(String path){
          
          reading = true;
           if(path == null){
            path = MONITORING_FILE_PATH;
            }
          
       
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";"; //splits file bij elke ;
        try{
            br = new BufferedReader(new FileReader(path));
            br.readLine();
            while ((line = br.readLine()) != null){
                String [] lines = line.split(cvsSplitBy);              
                getMonitoringObjectFound(lines);

            }
        }
        catch (Exception ex){   
            System.out.println("Reader: " + ex);
            }
         finally{
            if(br != null){
                try{
                    br.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("reading finished");
            }
            reading = false;
        }
        
    }
      
    public static void readAndInsertPositionsCSV(String path){
        
        reading = true;
        if(path == null){
            path = POSITIONS_FILE_PATH;
        }
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";"; //splits file for every ;
        
        try{
            br = new BufferedReader(new FileReader(path));
            br.readLine();
            while ((line = br.readLine()) != null){
                String [] lines = line.split(cvsSplitBy);
                

                String dateString = lines[0];
                Date dateFormatted = getDateByString(dateString);
                String unitId = lines[1];
                String rdx = lines[2];
                String rdy = lines[3];
                String speed = lines[4];
                String course = lines[5];
                String numSatellites = lines[6];
                String hdop = lines[7];
                String qualityString = lines[8];
                String connectionType = "";
                if(qualityString.toLowerCase().contains("gps")){
                    connectionType = "gps";
                }else if(qualityString.toLowerCase().contains("dr")){
                    connectionType = "car system";
                }else{
                    connectionType = "mixed";
                }            
                
                long[] longAndLatArray = calculateLongAndLatFromRxAndRy(Long.parseLong(rdx), Long.parseLong(rdy));
                BigInteger longitude = BigInteger.valueOf(longAndLatArray[0]); 
                BigInteger latitude = BigInteger.valueOf(longAndLatArray[1]);
                CarPositionData cpd = new CarPositionData(unitId, dateFormatted,
                        connectionType, latitude, longitude, Integer.parseInt(speed), Integer.parseInt(course), Integer.parseInt(hdop) );
                Database_Manager.addObjectToPersistList(cpd);
            }
        }
        catch (Exception ex){   
            System.out.println(ex);
            }
         finally{
            if(br != null){
                try{
                    br.close();
                }
                catch (Exception e){
                }
            }
            reading = false;
        }
    }
        
        
        
        
    private static void getMonitoringObjectFound(String[] lines) {
                        
                String unitId = lines[0];
                String beginTimeString = lines[1];
                String endTimeString = lines[2];
                String type = lines[3];
                String sum = lines[6];
                Long sumFormattedToLong = new BigDecimal(sum).longValue();
                long average = calculateAverageByTicks(sumFormattedToLong);
                
                
                
                Date date = null;
                try {
                    date = (new SimpleDateFormat("yyyy-MM-dd HH:mm" ).parse(beginTimeString));

                } catch (ParseException ex) {
                    System.out.println(ex);
                }
                
                EntityClass object = null;
                if(type.toLowerCase().startsWith("hsdpa")){
                    HsdpaConnection hc  = new HsdpaConnection(unitId, date);
                    
                    if(type.toLowerCase().endsWith("squal")){
                        hc.setSqual(BigInteger.valueOf(average));
                    }else if(type.toLowerCase().endsWith("rscp")){
                        hc.setRscp(BigInteger.valueOf(average));
                    }else if(type.toLowerCase().endsWith("srxlev")){
                        hc.setSrxlev(BigInteger.valueOf(average));
                    }else if(type.toLowerCase().endsWith("rssi")){
                        hc.setRssi(BigInteger.valueOf(average));
                    }else if(type.toLowerCase().endsWith("numberofconnects")){
                         hc.setNumberOfConnections((int)average);

                    }
                                 
                    object = hc;
                                        
                }else if(type.toLowerCase().startsWith("tcpclient")){
                    TcpClientConnection tcc = new TcpClientConnection(unitId, date);
                    
                        if(type.toLowerCase().endsWith("roundtriplatency")){
                            tcc.setRoundTripLatency(BigInteger.valueOf(average));
                        }else if(type.toLowerCase().endsWith("outstandingsends")){
                             tcc.setOutstandingSends((int)average);
                        }
                    object = tcc;    
                }
                
                if(object != null){
                    Database_Manager.addObjectToPersistList(object);
                }
                
    }
    
    private static Date getDateByString(String string){
    
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
    private static long calculateAverageByTicks(long sum){
        return sum / TICKS_PER_SECOND;
    }
    
    /**
     * @Author: someone from internet.
     * Website: http://forum.geocaching.nl/index.php?showtopic=7886
     * @param rx
     * @param ry
     * @return the longitude, latitude in a double[].
     */
    private static long[] calculateLongAndLatFromRxAndRy(long rx, long ry){
        
        long x = rx;
        long y = ry;
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

	long	latitude = Double.doubleToLongBits(52.15517 + (somN / 3600));
	long	longitude = Double.doubleToLongBits(5.387206 + (somE / 3600));
        
        long[] latAndLongArray = {latitude, longitude};
        return latAndLongArray;
    }
    
    
}
