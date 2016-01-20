/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import DatabaseClasses.EntityClasses.EntityClass;
import Readers.CSVFileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Elize
 */
public abstract class CSVInsertManager {
    
    /**
     * csvObjectsToPersist: 
     * A list of all the object that need to be insterted or updated to the database.
     * Some of these objects are already in the database, 
     * but they will be handled in the the persistOrUpdateObject function.
     */
    private static ArrayList<EntityClass> csvObjectsToPersist = new ArrayList();
    private static EntityManagerFactory emf = null;
    private static ArrayList<InsertThread> insertThreads = new ArrayList();
    private static final int MAX_AMOUNT_OF_THREADS = 100;
    private static final int AMOUNT_OF_OBJECTS_PER_THREAD = 100;
    private static boolean inserting = false;
    private static boolean stopped = false;
    private static boolean newDataInserted = false;

    public static boolean isNewDataInserted() {
        return newDataInserted;
    }

    public static void setNewDataInserted(boolean newDataInserted) {
        CSVInsertManager.newDataInserted = newDataInserted;
    }

    public static boolean isInserting() {
        return inserting;
    }
    
    public static void addObjectToPersistList(EntityClass entity){
        System.out.println("New Object: " + entity);
        inserting = true;
        csvObjectsToPersist.add(entity);
        createNewInsertThreadIfNeeded();
    }
    
    
    private static void createEntityManagerFactoryIfNeeded(){
        if(emf == null || !emf.isOpen() && !stopped){
            emf = Persistence.createEntityManagerFactory("CSVInsertThread");
            UI.User_Interface.setInsertingLabelText("true");
        }
    }
    
    private static void closeEntityManagerFactoryIfNeeded(){
         if(getInsertThreadsSize() == 0 && csvObjectsToPersist.isEmpty() && emf != null){
          emf.close();
          inserting = false;
          UI.User_Interface.setInsertingLabelText("false");
          System.out.println("End: " + new Date());
        
         }
    }
    
    public static void createNewInsertThreadIfNeeded(){
        
        createEntityManagerFactoryIfNeeded();
         
         if(!csvObjectsToPersist.isEmpty() && insertThreads.size() <= MAX_AMOUNT_OF_THREADS && !stopped){
            if(csvObjectsToPersist.size() >= AMOUNT_OF_OBJECTS_PER_THREAD || !CSVFileReader.isReading() ){
                                                
                List<EntityClass> newList = null;
                if(!CSVFileReader.isReading()){
                 newList = new ArrayList(csvObjectsToPersist);
                }else{
                 newList = new ArrayList(csvObjectsToPersist.subList(
                         csvObjectsToPersist.size() - AMOUNT_OF_OBJECTS_PER_THREAD, 
                         csvObjectsToPersist.size()));
                }
                EntityManager em = emf.createEntityManager();
                InsertThread is = new InsertThread(newList, em);
                insertThreads.add(is);
                csvObjectsToPersist.removeAll(newList);
            }        
        }
    }

    public static ArrayList<InsertThread> getInsertThreads() {
        return insertThreads;
    }

    public static int getInsertThreadsSize() {
        return insertThreads.size();
    }
    
    public static void removeThread(InsertThread thread){
      insertThreads.remove(thread);
      if(!CSVFileReader.isReading()){
      createNewInsertThreadIfNeeded();
      }
      closeEntityManagerFactoryIfNeeded();
    }
    
    public static void stopAllThreads(){
        
      inserting = false;
      stopped = true;
        try{
          while(!insertThreads.isEmpty() && insertThreads != null && insertThreads.size() != 0){
                 insertThreads.get(0).stopThread();
          }   
        }catch(Exception ex){
            System.out.println(ex);
        }finally{
            if(emf != null && emf.isOpen()){
            emf.close();
        }
      }
     
    }
    
     public static boolean isInsertThreadsMaxSize(){
        return (insertThreads.size() >= MAX_AMOUNT_OF_THREADS);
    }
}
