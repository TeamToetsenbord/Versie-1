/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

/**
 *
 * @author Elize
 */
public class DataGetterHandler {
        
    private static DataGetterThread currentDataGetterThread = null;
    
    public static void startIfNeccesary() {
        if(!SessionHandler.getDataRecieverSessions().isEmpty()
           && currentDataGetterThread == null){
            currentDataGetterThread = new DataGetterThread();
        }
    }
    
    public static void stopIfNeccesary() {
        if(SessionHandler.getDataRecieverSessions().isEmpty()
           && currentDataGetterThread != null){
            currentDataGetterThread.stopThread();
            currentDataGetterThread = null;
        }
    }
}
