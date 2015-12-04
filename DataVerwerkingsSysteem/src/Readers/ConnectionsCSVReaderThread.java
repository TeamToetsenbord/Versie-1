/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import DatabaseClasses.Database_Manager;
import DatabaseClasses.OverallConnection;
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
public class ConnectionsCSVReaderThread extends CSVFileReader{
    
    private static final String CONNECTIONS_FILE_PATH = "CSVFiles/Connections.csv";  
    private String userPath = null;
    String path = null;

    @Override
    public void run() {
        readAndInsertConnectionsCSV();
    }
    
    public ConnectionsCSVReaderThread(String path){
     this. userPath = path;   
    }
    
    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }
    
        
    public void readAndInsertConnectionsCSV(){
       
        reading = true;
        if(userPath == null){
            path = CONNECTIONS_FILE_PATH;
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
                showMessageDialog(null, "This file is not a connections.csv. Please try another one.");
            return;
            }
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
}
