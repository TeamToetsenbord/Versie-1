/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.CSVInsertManager;
import DatabaseClasses.Database_Manager;
import DatabaseClasses.EntityClasses.EntityClass;
import DatabaseClasses.EntityClasses.HsdpaConnection;
import DatabaseClasses.EntityClasses.TcpClientConnection;
import static Readers.CSVFileReader.calculateAverageByTicks;
import static Readers.CSVFileReader.reading;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author Elize
 */
public class MonitoringCSVReaderThread extends CSVFileReader {
    
    private static final String MONITORING_FILE_PATH = "CSVFiles/Monitoring.csv";
    private String path = null;
    private String userPath = null;

    @Override
    public void run() {
        readAndInsertMonitoringCSV();
    }
    
    
    
    public MonitoringCSVReaderThread(String path){
        this.userPath = path;
    }
      
      public void readAndInsertMonitoringCSV(){
          
          CSVFileReader.setReading(true);
           if(userPath == null){
            path = MONITORING_FILE_PATH;
            }else{
            path = userPath;
           }
          
       
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";"; //splits file bij elke ;
        try{
            br = new BufferedReader(new FileReader(path));
            String firstline = br.readLine();
            if(!firstline.equals("UnitId;BeginTime;EndTime;Type;Min;Max;Sum")){
                showMessageDialog(null, "This file is not a Monitoring.csv. Please try another one.");
                return;
            }

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
                System.out.println("Reading Monitoring.csv finished");
            }
            CSVFileReader.setReading(false);
        }
        
    }
          
    private void getMonitoringObjectFound(String[] lines) {
                        
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
                    CSVInsertManager.addObjectToPersistList(object);
                }
                
    }
}
