/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.Database_Manager;
import DatabaseClasses.OverallConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ronald
 */
public class CSV_File_Reader {
    
    private static final String CONNECTIONS_FILE_PATH = "CSVFiles/Connections.csv";    
    private static final String EVENTS_FILE_PATH = "CSVFiles/Events.csv";
    private static final String MONITORING_FILE_PATH = "CSVFiles/Monitoring.csv";
    private static final String POSITIONS_FILE_PATH = "CSVFiles/Positions.csv";
    
    public CSV_File_Reader(){
        
    }
    
    public static void readLiveConnections(String path){
       
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
                
                Date dateFormatted = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" )).parse(dateString);  
 
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
    
     public static void readLiveEvents(String path){
         
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
     
      public static void readLiveMonitoring(String path){
          
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
      
        public static void readLivePositions(String path){
        
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
}
