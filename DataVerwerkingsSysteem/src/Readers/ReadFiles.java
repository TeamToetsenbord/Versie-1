/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Readers;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author Ronald
 */
public class ReadFiles {
    public ReadFiles(){
        
    }
    
    public static void ReadLiveConnections(String fconstr){
        String fcon = fconstr;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";"; //splits file bij elke ;
        try{
            br = new BufferedReader(new FileReader(fcon));
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
        catch (Exception E){     
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
    
     public static void ReadLiveEvents(String econstr){
        String econ = econstr;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";"; //splits file bij elke ;
        try{
            br = new BufferedReader(new FileReader(econ));
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
        catch (Exception E){     
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
     
      public static void ReadLiveMonitoring(String mconstr){
        String mcon = mconstr;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";"; //splits file bij elke ;
        try{
            br = new BufferedReader(new FileReader(mcon));
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
        catch (Exception E){     
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
      
          public static void ReadLivePositions(String pconstr){
        String pcon = pconstr;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";"; //splits file bij elke ;
        try{
            br = new BufferedReader(new FileReader(pcon));
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
        catch (Exception E){     
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
