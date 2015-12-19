/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import Readers.CSVFileReader;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Elize
 */
public class CSVInsertManager {
    
    /**
     * csvObjectsToPersist: 
     * A list of all the object that need to be insterted or updated to the database.
     * Some of these objects are already in the database, 
     * but they will be handled in the the persistOrUpdateObject function.
     */
    private static ArrayList<EntityClass> csvObjectsToPersist = new ArrayList();
    private static EntityManagerFactory emf = null;
    private static ArrayList<InsertThread> insertThreads = new ArrayList();
    
    public static void addObjectToPersistList(EntityClass entity){
        csvObjectsToPersist.add(entity);
        createNewInsertThreadIfNeeded();
    }
    
    private static void checkEntityManagerFactory(){
        if(emf == null){
            emf = Persistence.createEntityManagerFactory(Database_Manager.getPersistenceName());
            UI.User_Interface.setInsertingLabelText("true");
        }
        
        if(getInsertThreadsSize() == 0 && csvObjectsToPersist.isEmpty()){
          System.out.println("Closed");
          emf.close();
          UI.User_Interface.setInsertingLabelText("false");
        }
    }
    
    public static void createNewInsertThreadIfNeeded(){
        if(!csvObjectsToPersist.isEmpty()){
            if(csvObjectsToPersist.size() >= 100 || !CSVFileReader.isReading() ){
                List<EntityClass> newList = null;
                if(!CSVFileReader.isReading()){
                 newList = new ArrayList(csvObjectsToPersist);
                }else{
                 newList = new ArrayList(csvObjectsToPersist.subList(
                         csvObjectsToPersist.size() - 100, 
                         csvObjectsToPersist.size()));
                }
                InsertThread is = new InsertThread(newList, emf);
                insertThreads.add(is);
                csvObjectsToPersist.removeAll(newList);
            }        
        }
        checkEntityManagerFactory();
    }

    public static ArrayList<InsertThread> getInsertThreads() {
        return insertThreads;
    }

    public static int getInsertThreadsSize() {
        return insertThreads.size();
    }
    
    public static void removeThread(InsertThread thread){
      insertThreads.remove(thread);
      checkEntityManagerFactory();
    }
    
}
