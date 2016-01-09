/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.CSVInsertManager;
import DatabaseClasses.EntityClasses.CarStatusEvent;
import DatabaseClasses.Database_Manager;
import static Readers.CSVFileReader.getDateByString;
import static Readers.CSVFileReader.reading;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author Elize
 */
public class EventsCSVReaderThread extends CSVFileReader{
    
    private static final String EVENTS_FILE_PATH = "CSVFiles/Events.csv";
    private String userPath = null;
    String path = null;

    public EventsCSVReaderThread(String path){
        this.userPath = path;
        
    }
    
    @Override
    public void run() {
        readAndInsertEventsCSV();
    }
    
    
    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }
    
    public void readAndInsertEventsCSV(){
       
        CSVFileReader.setReading(true);
        if(userPath == null){
            path = EVENTS_FILE_PATH;
        }else{
            path = userPath;
        }
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";"; //splits file bij elke ;
        try{
            
            br = new BufferedReader(new FileReader(path));
            String firstline = br.readLine();
            
            if(!firstline.equals("DateTime;UnitId;Port;Value")){
                showMessageDialog(null, "This file is not a Events.csv. Please try another one.");
            return;
            }
            while ((line = br.readLine()) != null){
                String [] lines = line.split(cvsSplitBy);
                String dateString = lines[0];
                String unitId = lines[1];
                String port = lines [2];
                String value = lines [3];
                
                Date dateFormatted = getDateByString(dateString); 
 
                CarStatusEvent carStatusEvent = new CarStatusEvent(unitId, dateFormatted, port, value );
                
                CSVInsertManager.addObjectToPersistList(carStatusEvent);
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
             System.out.println("Reading events.csv finished");
             CSVFileReader.setReading(false);
        }   
    }
}
