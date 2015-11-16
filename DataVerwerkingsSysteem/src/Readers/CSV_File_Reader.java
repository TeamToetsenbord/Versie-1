/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.CarStatusEvent;
import DatabaseClasses.Database_Manager;
import DatabaseClasses.HsdpaConnection;
import DatabaseClasses.OverallConnection;
import java.io.BufferedReader;
import java.io.FileReader;
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
    public CSV_File_Reader(){
        
    }
    
    public static void readAndInsertConnectionsCSV(String path){
       
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
        }   
    }
    
     public static void readAndInsertEventsCSV(String path){
       
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
        }   
    }
     
      public static void readMonitoringCSV(String path){
          
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
        }
        
    }
      
        public static void readPositionsCSV(String path){
        
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
                String date = lines[0];
                String unitid = lines[1];
                String port = lines [2];
                String value = lines [3];
                System.out.println("info:   "+ date +"   " + unitid +"    "+ port +"    "+ value);
                                //TODO Add information to database!

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
        }
     }
        
        
        
        
    private static void getMonitoringObjectFound(String[] lines) {
         
        //UnitId;BeginTime;EndTime;Type;Min;Max;Sum

                
                String unitId = lines[0];
                String beginTimeString = lines[1];
                String endTimeString = lines[2];
                String type = lines[3];
                String sum = lines[6];
                
                Date beginTime = getDateByString(beginTimeString); 
                Date endTime = getDateByString(endTimeString); 
                
                if(type.contains("Hsdpa")){
                HsdpaConnection hsdpaConnection = new HsdpaConnection(unitId, beginTime, endTime);
                }else if(type.contains("TcpClient")){
                
                }else if(type.contains("Gps")){
                    
                }
                //TODO get other information!
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
    private int calculateAverageByTicks(int sum){
        return sum / TICKS_PER_SECOND;
    }
}
