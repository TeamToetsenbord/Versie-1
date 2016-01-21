/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Elize
 */
public class DataGetterThread extends Thread {

    //Set the refresh time to 1 minute
    int refreshTime = 60000;
    boolean run = false;
    JSONObject[] reportJSONObjects = null;
    JSONObject controlRoomReportJSON = null; 
    
    private void setJSONObjects(){
        try{
        if( reportJSONObjects == null){
            reportJSONObjects = new JSONObject[3];
                JSONObject cityGisReportJSON = new JSONObject()
                    .put("type", "getReport")
                    .put("reportType", "CityGis")
                    .put("unitId", "");
                reportJSONObjects[0] = cityGisReportJSON;
                JSONObject authorityReportJSON = new JSONObject()
                    .put("type", "getReport")
                    .put("reportType", "Authority")
                    .put("unitId", "");
                reportJSONObjects[1] = authorityReportJSON;
                JSONObject connectionsReportJSON = new JSONObject()
                    .put("type", "getReport")
                    .put("reportType", "Connections")
                    .put("unitId", "");
                reportJSONObjects[2] = connectionsReportJSON;
                        
           }
        if(controlRoomReportJSON == null){
            controlRoomReportJSON = new JSONObject()
                    .put("type", "getReport")
                    .put("reportType", "Control_Room");
            }
        } catch (JSONException ex) {
            System.out.println("Could not created JSON report objects...");
            System.out.println(ex);
        }
        }
            

    public DataGetterThread(){
        setJSONObjects();
        this.run = true;
        this.start();
    }
    
    
    
    @Override
    public void run() {
        while(run){
        long starttime = System.nanoTime();
        getNewDataFromDatabase();
        long secondsPassed = (System.nanoTime() - starttime) / 1000000000;
        if(secondsPassed < refreshTime){
            try {
                System.out.println("Sleeping " + (refreshTime - secondsPassed) + "Seconds" );
                Thread.sleep(refreshTime - secondsPassed);
            } catch (InterruptedException ex) {
                System.out.println("Thread interupted");
            }
            
        }
        }
    }

    void stopThread() {
        this.run = false;
    }

    private void getNewDataFromDatabase() {
      for(int i = 0; i < reportJSONObjects.length; i++ ){
          sendToAllConnectedDataRecieverSessions(
                  ServerManager.sendAndRecieveMessageToServer(reportJSONObjects[i]));
      }
     
      for(DataRecieverSession dataRecieverSession: SessionHandler.getDataRecieverSessions()){
          if(dataRecieverSession.getUnitID() != null){
          try {
              JSONObject controlJSONWithUnitId = new JSONObject(controlRoomReportJSON, JSONObject.getNames(controlRoomReportJSON));
              controlJSONWithUnitId.put("unitId", dataRecieverSession.getUnitID());
              String returnMessage = ServerManager.sendAndRecieveMessageToServer(controlJSONWithUnitId);
              sendMessageToSession(returnMessage, dataRecieverSession);
          } catch (JSONException ex) {
              System.out.println("Could not create a controlJSONWithUnitId JSONObject.");
              System.out.println(ex);
          }
        }
      }
    }
    
    private void sendToAllConnectedDataRecieverSessions(String message){
        for(DataRecieverSession dataRecieverSession: SessionHandler.getDataRecieverSessions()){
            dataRecieverSession.getSession().getAsyncRemote().sendText(message);
        }
    }
  
    private void sendMessageToSession(String returnMessage, DataRecieverSession dataRecieverSession) {
        System.out.println("Sending: " + returnMessage);
        dataRecieverSession.getSession().getAsyncRemote().sendText(returnMessage);
    }
}
