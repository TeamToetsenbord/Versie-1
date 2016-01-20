/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import DatabaseClasses.EntityClasses.Car;
import DatabaseClasses.EntityClasses.EntityClass;
import DatabaseClasses.EntityClasses.Password;
import DatabaseClasses.EntityClasses.User;
import Readers.CSVFileReader;
import UI.User_Interface;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Elize
 */
public class Database_Manager extends Thread {
    
    /**
     * csvObjectsToPersist: 
     * A list of all the object that need to be insterted or updated to the database.
     * Some of these objects are already in the database, 
     * but they will be handled in the the persistOrUpdateObject function.
     */
    private static ArrayList<EntityClass> csvObjectsToPersist = new ArrayList();
    //The amount of objects that will be processed per transaction.
    private static final int OBJECTS_PER_TRANSACTION = 100; 
    private static EntityManager entityManager = null;
    private static final String PERSISTENCE_UNIT_NAME = "DataProccesingSystemPU";
    private int count = OBJECTS_PER_TRANSACTION;
    private User_Interface ui = null;
    private static boolean running = false;
    /**
     * entityManagerFactory: Creates a connections to the database.
     * Since it is static, this connection should last as long as the program is running.
     */    
    private static EntityManagerFactory entityManagerFactory
              = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
      
    public Database_Manager(User_Interface ui){
        this.ui = ui;
        setOnExitActions();
        running = true;
        this.start();
    }
    
    public Database_Manager(){
        setOnExitActions();
    }
    
    public static String getPersistenceName() {
        return PERSISTENCE_UNIT_NAME; 
    }
    
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
    
    /**
     * This method will set the exit loop for this application.
     * When the program is exited, the connections should be closed.
     */
    private void setOnExitActions(){
          Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run()
            {
                Database_Manager.closeConnection(); 
            }
         });
    }
    
    /**
     * This method is run on an alternative thread.
     * It will make sure all the objects in ObjectsToPersist are processed in the persistOrUpdateObject function.
     * When the objectsToPersist list is empty, this method will still run, 
     * but is wont do anything unil new objects are added to the objectsToPesrist list
     */
    @Override
    public void run() {
        while(running){
                    
            if(entityManager == null){
            entityManager = entityManagerFactory.createEntityManager();
            }
            
            if(!csvObjectsToPersist.isEmpty() && csvObjectsToPersist.size() > 0){
               EntityClass objectToPersist = csvObjectsToPersist.get(0);
               if(objectToPersist != null){
               handleTransactionsForCSVFile(objectToPersist);
               ui.setInsertingLabelText("true");               
               }
            }else{
                if(CSVFileReader.reading == false && csvObjectsToPersist.isEmpty() && !CSVInsertManager.isInserting()){
                ui.setInsertingLabelText("false");
                }
            }
        }   
    }
    
    private void handleTransactionsForCSVFile(EntityClass object){
         int currentSize = csvObjectsToPersist.size();
            if(!entityManager.getTransaction().isActive() || !entityManager.isOpen() ){
               entityManager.getTransaction().begin();
            }
            
            persistOrUpdateObject(object, entityManager, csvObjectsToPersist);
            
            count--;  
            if(count == 0 || currentSize <= OBJECTS_PER_TRANSACTION){
            entityManager.getTransaction().commit();
            entityManager.clear();
            count = OBJECTS_PER_TRANSACTION;
            }
        
    }
    
    protected void persistOrUpdateObject(EntityClass object, 
                 EntityManager em, 
                 List<EntityClass> objectsToPersist){
            
            if(!em.getTransaction().isActive()){
             em.getTransaction().begin();   
            }
            try{
            EntityClass objectInDatabaseFound = 
                    em.find(object.getClass(),  
                            em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(object));
            if(objectInDatabaseFound == null){
                persist(object, em);
            }else if(objectInDatabaseFound != null && !objectInDatabaseFound.equals(object)){
                update(object, objectInDatabaseFound, em);
            }
            }catch(PersistenceException ex){
            System.out.println("Object: " + object + ". Exception: " + ex);
        }catch(Exception ex){
            //TODO remove the pokemon programming:/
            System.out.println(ex);
        }finally{
            objectsToPersist.remove(object);
            if(em.getTransaction().isActive()){
                try{
                em.getTransaction().commit();
                }catch(Exception ex){
                    System.out.println("Exception: " + object);
                }
                    
                    
            }
            
        }
    }
    
    /**
     * Method for updating the object.
     * By updating we want to get the existing from the database, and merge it with the new attributes.
     * For example, the carId of a hspdConnection is found, but not stored in the database.
     * In order to insert the new data, and keep the old data, we need to merge the object, and instert it.
     * Only contrasting values are inserted.
     * You could compare this function with SQL UPDATE (Entity) SET (new attributes) WHERE (entityID)
     * @param newObject: the newest object,
     * @param entityManager
     * @param dbObject: the object found on the database.
     */
    private void update(EntityClass newObject, EntityClass dbObject, EntityManager em){
       CSVInsertManager.setNewDataInserted(true);
       EntityClass objectToPersist = newObject.mergeWithObjectFromDatabase(dbObject);
       checkIfObjectHasCar(objectToPersist, em);
       em.merge(objectToPersist);
    }
    
    /**
     * This is the persist method used by all entities;
     * @param object: the entity to be inserted. 
     */
    public void persist(EntityClass object, EntityManager em){
       CSVInsertManager.setNewDataInserted(true);
       checkIfObjectHasCar(object, em);
       em.persist(object);
    }

    /**
     * Method for adding objects to the (private) objectsToPersist list.
     * Using this method, the list cannot be tampered with.
     * @param object: The object to add to the list. This must be a child of EntityClass.
     */
    public static void addObjectToPersistList(EntityClass object) {
        csvObjectsToPersist.add(object);  
    }

    /**
     * Method for inserting new cars.
     * @param car: found car
     * @param em 
     */
    private void insertCarIfNeeded(Car car, EntityManager em) {
        if(em.find(car.getClass(), car.getUnitId()) == null){
            em.persist(car);
        }
    }
    
    /**
     * Since not all the entity classes have a getCar() method,
     * we are going to check if it has one first
     * If so, we are going to check if the car needs to be inserted into the database.
     */
    private void checkIfObjectHasCar(EntityClass object, EntityManager em) {
        Car car = null;
        try {
            car = (Car) object.getCar();
            if(car != null){
            insertCarIfNeeded(car, em);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            
        }
    }
    
    
    public static void closeConnection() {
        try{
        running = false;
        CSVInsertManager.stopAllThreads();
        
        if(entityManager != null && entityManager.isOpen()){
            if(entityManager.getTransaction().isActive()){
              entityManager.getTransaction().commit();
            }
            entityManager.clear();
            entityManager.close();
        }
        
        }catch(Exception ex){
            System.out.println(ex);
        }finally{
            if(entityManagerFactory != null && entityManagerFactory.isOpen()){
            entityManagerFactory.close();
            }
        }
       
    } 
    
     public static String logIn(String username, String password) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Password passwordFound = em.find(Password.class, password);
        em.getTransaction().commit();
        em.clear();
        em.close();
        if(passwordFound != null && passwordFound.getUserName().equals(username)){
            return username;
        }else{
            return null;
        }
    }
        
     
     /**
      * Method to check if the report really is one of the supported reports.
      * @param reportDataType: report to be generated
      * @param unitId: if necessary, the unitId of the tracked car
      * @return the JSON message to be send to the client
      * @throws JSONException 
      */
    public static JSONObject getLatestReportData(String reportDataType, String unitId) throws JSONException {
        
        if( reportDataType.equals("Connections")
            || reportDataType.equals("Control_Room")
            || reportDataType.equals("Authority")   
            || reportDataType.equals("CityGis")){
            return getJSONFromDatabase(reportDataType, unitId);
        }else{
          JSONObject returnJSON =  new JSONObject();
          returnJSON.put("error", "This type is not a report type!");
          returnJSON.put("type", "error");
          return returnJSON;
        }
       
    }

    /**
     * Method to get the report data from the database, and parse it into a JSONObject.
     * @param reportType: the type of report requested should be one of the following: 
     * {Control_Room, Connections, Authority, CityGis}
     * @param unitId: in case of thte Control_Room report, a unitId is necessary
     * @return the report JSONObject
     */
    public static JSONObject getJSONFromDatabase(String reportType, String unitId){
        
        JSONObject returnJSON = new JSONObject();
        try{
        returnJSON.put("type", "report");
        returnJSON.put("reportType", reportType);    
        EntityManager em = entityManagerFactory.createEntityManager();
        HashMap<String, String> querylist = getReportQueriesByReportType(reportType);
        Iterator it = querylist.entrySet().iterator();
        while(it.hasNext()){
           JSONArray reportPartJsonArray = new JSONArray();
           em.getTransaction().begin(); 
           Map.Entry<String, String> pair = (Map.Entry) it.next();
           String name = pair.getKey();
           String queryString = pair.getValue();
           //If the query needs a unit id, replace the %unit_id% string
           if(queryString.contains("%unit_id%") && unitId != null){
           queryString.replaceAll("%unit_id%", unitId);
           }
           Query query = em.createNativeQuery(queryString);
           List resultset = query.getResultList();
           em.getTransaction().commit();
           for(Object result: resultset){
               reportPartJsonArray.put(new JSONObject(result.toString()));
           }
           returnJSON.put(name, reportPartJsonArray);
        }
        em.clear();
        em.close();
        }catch (JSONException ex) {
            System.out.println("An JSON error occured while getting data from the database:");
            System.out.println(ex);
        }catch (IllegalStateException ex){
            System.out.println("An error occured while getting data from the database:");
            System.out.println(ex);
        }catch (PersistenceException ex){
            System.out.println("An Persistence error occured while getting data from the database:");
            System.out.println(ex);
        }        
        return returnJSON;
    }
    
    /**
     * 
     * @param reportType: should be one of the following {Control_Room, Connections, Authority, CityGis}
     * @return: a map with the name of the column as key, and the query as the value
     */   
    private static HashMap<String, String> getReportQueriesByReportType(String reportType){
        File configFile = new File(reportType + "ReportQueries.properties");
        HashMap<String, String> queriesWithColumnname = new HashMap();
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            for (Enumeration<?> names = props.propertyNames(); names.hasMoreElements(); ) {
            String name = (String)names.nextElement();
            String value = props.getProperty(name);
                queriesWithColumnname.put(name.substring(reportType.length() + 1), value);
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }finally{
            return queriesWithColumnname;
        }
    }
    
}
