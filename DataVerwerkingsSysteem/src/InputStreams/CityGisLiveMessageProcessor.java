/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputStreams;

import DatabaseClasses.CSVInsertManager;
import DatabaseClasses.EntityClasses.CarPositionData;
import DatabaseClasses.EntityClasses.CarStatusEvent;
import DatabaseClasses.EntityClasses.EntityClass;
import DatabaseClasses.EntityClasses.HsdpaConnection;
import DatabaseClasses.EntityClasses.OverallConnection;
import DatabaseClasses.EntityClasses.TcpClientConnection;
import Readers.CSVFileReader;
import com.sun.media.jfxmedia.logging.Logger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author School
 */
public class CityGisLiveMessageProcessor extends CSVFileReader {
    
         
    public static void processMessage(String type, String message ){
        try{
            JSONObject jsonMessage = new JSONObject(message);
            
            switch(type){
                case "POSITIONS": 
                    getPositionsObjectFromJson(jsonMessage);
                    break;
                case "MONITORING":
                    getMonitoringObjectFromJson(jsonMessage);
                    break;
                case "EVENTS":
                    getEventsObjectFromJson(jsonMessage);
                    break;
                case "CONNECTIONS":
                    getConnectionsObjectFromJson(jsonMessage);
                    break;
                default:
                    throw new InputMismatchException() ;
            }
                        
        }catch(JSONException ex){
            System.out.println("This message is not in JSON format. /n"
                    + "Please try another file.");
        }catch(InputMismatchException ex){
            System.out.println("The type of file is not known.");
        }
    
    }
    
    private static void getPositionsObjectFromJson(JSONObject jsonObject) throws JSONException{
                String dateString = jsonObject.getString("dateTime");
                Date dateFormatted = getJsonDateByString(dateString);
                long unitId = jsonObject.getLong("unitId");
                String rdx = jsonObject.getString("rDx");
                String rdy = jsonObject.getString("rDy");
                double speed = jsonObject.getDouble("speed");
                double course = jsonObject.getDouble("course");
                int numSatellites = jsonObject.getInt("numSattellites");
                int hdop = jsonObject.getInt("hdop");
                String qualityString = jsonObject.getString("quality");
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
                CarPositionData cpd = new CarPositionData(Long.toString(unitId), dateFormatted,
                        connectionType, latitude, longitude, 
                        (int)speed, (int)course, hdop);
                
                CSVInsertManager.addObjectToPersistList(cpd);
                   
    }

    private static void getEventsObjectFromJson(JSONObject jsonMessage) throws JSONException {
                String dateString = jsonMessage.getString("dateTime");
                String unitId = Long.toString(jsonMessage.getLong("unitId"));
                String port = jsonMessage.getString("port");
                int value = jsonMessage.getInt("value");
                
                Date dateFormatted = getJsonDateByString(dateString); 
 
                CarStatusEvent carStatusEvent = new CarStatusEvent(
                        unitId, dateFormatted, port, Integer.toString(value));
                
                CSVInsertManager.addObjectToPersistList(carStatusEvent);
    }

    private static void getConnectionsObjectFromJson(JSONObject jsonMessage) throws JSONException {
                String dateString = jsonMessage.getString("dateTime");
                String unitId = Long.toString(jsonMessage.getLong("unitId"));
                String port = jsonMessage.getString("port");
                String value = Integer.toString(jsonMessage.getInt("value"));
                boolean connected = (Integer.parseInt(value) != 0);
                
                Date dateFormatted = getJsonDateByString(dateString);   
 
                OverallConnection overallConnection = new OverallConnection(dateFormatted, unitId, connected);
                CSVInsertManager.addObjectToPersistList(overallConnection);
    }

    private static void getMonitoringObjectFromJson(JSONObject jsonMessage) throws JSONException {
                String unitId = jsonMessage.getString("unitId");
                String beginTimeString = jsonMessage.getString("beginTime");
                String endTimeString = jsonMessage.getString("endTime");
                String type = jsonMessage.getString("type");
                double sum = jsonMessage.getDouble("sum") ;
                Long sumFormattedToLong = new BigDecimal(sum).longValue();
                long average = calculateAverageByTicks(sumFormattedToLong);
          
                Date date = null;
                try {
                    date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm" ).parse(beginTimeString));

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
    
        private static Date getJsonDateByString(String dateString){
            
        Date dateFormatted = null; 
        try {
            dateFormatted = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss" )).parse(dateString);
        }catch (ParseException ex) {
            System.out.println(ex);
        }finally{
        return dateFormatted;
        }
            
        }
    
}
