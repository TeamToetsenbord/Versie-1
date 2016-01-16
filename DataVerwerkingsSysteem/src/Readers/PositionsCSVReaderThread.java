/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.CSVInsertManager;
import DatabaseClasses.EntityClasses.CarPositionData;
import DatabaseClasses.Database_Manager;
import static Readers.CSVFileReader.reading;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author Ronald & Elize
 */
public class PositionsCSVReaderThread extends CSVFileReader{
    
    private static final String POSITIONS_FILE_PATH = "CSVFiles/Positions.csv";
    private String userPath = null;
    String path = null;

    public PositionsCSVReaderThread(String path){
        this.userPath = path;
        
    }
    @Override
    public void run() {
        readAndInsertPositionsCSV();
    }
    
    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }
            
    public void readAndInsertPositionsCSV(){
        
        CSVFileReader.setReading(true);
       
        if(userPath == null){
            path = POSITIONS_FILE_PATH;
        }else{
            path = userPath;
        }
        BufferedReader br = null;
        String line = "";
        
        try{
            br = new BufferedReader(new FileReader(path));
            String firstline = br.readLine();
            if(!firstline.equals("DateTime;UnitId;Rdx;Rdy;Speed;Course;NumSatellites;HDOP;Quality")){
                showMessageDialog(null, "This file is not a Positions.csv. Please try another one.");
                return;
            }
            
            while ((line = br.readLine()) != null){
                String [] lines = line.split(CSV_SPLIT_BY);
                readAndInsertLine(lines);
                
            }
        }catch (Exception ex){   
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
            System.out.println("Reading positions.csv finished");
            CSVFileReader.setReading(false);
        }
    }
    
    public static void readAndInsertLine(String[] lines){
                String dateString = lines[0];
                Date dateFormatted = CSVFileReader.getDateByString(dateString);
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
                float fx = Float.parseFloat(rdx);
                float fy = Float.parseFloat(rdy);
                
                double[] longAndLatArray = CSVFileReader.calculateLongAndLatFromRxAndRy((long)fx, (long)fy);
                double longitude = longAndLatArray[0]; 
                double latitude = longAndLatArray[1];
                CarPositionData cpd = new CarPositionData(unitId, dateFormatted,
                        connectionType, latitude, longitude, Integer.parseInt(speed), Integer.parseInt(course), Integer.parseInt(hdop) );
                
                CSVInsertManager.addObjectToPersistList(cpd);
    }
}
