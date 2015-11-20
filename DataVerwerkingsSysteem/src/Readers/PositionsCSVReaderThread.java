/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.CarPositionData;
import DatabaseClasses.Database_Manager;
import static Readers.CSV_File_Reader.reading;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Ronald & Elize
 */
public class PositionsCSVReaderThread extends CSV_File_Reader{
    
    private static final String POSITIONS_FILE_PATH = "CSVFiles/Positions.csv";
    private String userPath = null;
    String path = null;

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
        
        reading = true;
       
        if(userPath == null){
            path = POSITIONS_FILE_PATH;
        }else{
            path = userPath;
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
                Date dateFormatted = CSV_File_Reader.getDateByString(dateString);
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
                
                
                long[] longAndLatArray = CSV_File_Reader.calculateLongAndLatFromRxAndRy((long)fx, (long)fy);
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
            System.out.println("Reading positions.csv finished");
            reading = false;
        }
    }
}
