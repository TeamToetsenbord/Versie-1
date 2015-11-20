/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.CarStatusEvent;
import DatabaseClasses.Database_Manager;
import static Readers.CSVFileReader.getDateByString;
import static Readers.CSVFileReader.reading;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;

/**
 *
 * @author Elize
 */
public class EventsCSVReaderThread extends CSVFileReader{
    
    private static final String EVENTS_FILE_PATH = "CSVFiles/Events.csv";
    private String userPath = null;
    String path = null;

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
       
        reading = true;
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
            br.readLine();
            while ((line = br.readLine()) != null){
                String [] lines = line.split(cvsSplitBy);
                String dateString = lines[0];
                String unitId = lines[1];
                String port = lines [2];
                String value = lines [3];
                
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
}
