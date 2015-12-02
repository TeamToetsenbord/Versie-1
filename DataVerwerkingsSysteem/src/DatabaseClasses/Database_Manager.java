/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import Readers.CSVFileReader;
import UI.User_Interface;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;

/**
 *
 * @author Elize
 */
public class Database_Manager extends Thread {
    
    /**
     * ObjectsToPersist: 
     * A list of all the object that need to be insterted or updated to the database.
     * Some of these objects are already in the database, 
     * but they will be handled in the the persistOrUpdateObject function.
     */
    private static ArrayList<EntityClass> objectsToPersist = new ArrayList();
    //The amount of objects that will be processed per transaction.
    private static final int OBJECTS_PER_TRANSACTION = 100; 
    /**
     * entityManagerFactory: Creates a connections to the database.
     * Since it is static, this connection should last as long as the program is running.
     */    
    private static EntityManagerFactory entityManagerFactory
              = Persistence.createEntityManagerFactory("DataProccesingSystemPU");
    private static EntityManager entityManager = null;
    private int count = OBJECTS_PER_TRANSACTION;
    private User_Interface ui = null;
      
    public Database_Manager(User_Interface ui){
        this.ui = ui;
    }
    
    /**
     * This method is run on an alternative thread.
     * It will make sure all the objects in ObjectsToPersist are processed in the persistOrUpdateObject function.
     * When the objectsToPersist list is empty, this method will still run, 
     * but is wont do anything unil new objects are added to the objectsToPesrist list
     */
    @Override
    public void run() {
        while(true){
            if(!objectsToPersist.isEmpty() && objectsToPersist.size() > 0){
               EntityClass objectToPersist = objectsToPersist.get(0);
               if(objectToPersist != null){
               persistOrUpdateObject(objectToPersist);
               ui.setInsertingLabelText("true");
               }
            }else{
                if(CSVFileReader.reading == false && objectsToPersist.size() == 0){
                ui.setInsertingLabelText("false");
                }
            }
        }   
    }
    
    protected void persistOrUpdateObject(EntityClass object){
    
        try{
            int currentSize = objectsToPersist.size();
            if(entityManager == null && !entityManager.isOpen()){
               entityManager = entityManagerFactory.createEntityManager();
               entityManager.getTransaction().begin();
            }
            EntityClass objectInDatabaseFound = 
                    entityManager.find(object.getClass(),  object.getPK());
            if(objectInDatabaseFound == null){
                persist(object, entityManager);
            }else if(objectInDatabaseFound != null && !objectInDatabaseFound.equals(object)){
                update(object, entityManager, objectInDatabaseFound);
            }
            
            count--;  
            if(count == 0 || currentSize <= OBJECTS_PER_TRANSACTION){
            entityManager.getTransaction().commit();
            entityManager.clear();
            entityManager.close();
            count = OBJECTS_PER_TRANSACTION;
            System.out.println("committed");
            }
            
        }catch(PersistenceException ex){
            System.out.println("Object: " + object + ". Exception: " + ex);
        }catch(Exception ex){
            //TODO remove the pokemon programming:/
            System.out.println(ex);
        }finally{
            objectsToPersist.remove(object);
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
    private void update(EntityClass newObject, EntityManager entityManager, EntityClass dbObject){
       EntityClass objectToPersist = newObject.mergeWithObjectFromDatabase(dbObject);
       checkIfObjectHasCar(objectToPersist, entityManager);
       entityManager.merge(objectToPersist);
    }
    
    /**
     * This is the persist method used by all entities;
     * @param object: the entity to be inserted. 
     */
    public void persist(EntityClass object, EntityManager entityManager){
       checkIfObjectHasCar(object, entityManager);
       entityManager.persist(object);
    }

    /**
     * Method for adding objects to the (private) objectsToPersist list.
     * Using this method, the list cannot be tampered with.
     * @param object: The object to add to the list. This must be a child of EntityClass.
     */
    public static void addObjectToPersistList(EntityClass object) {
        objectsToPersist.add(object);    
    }

    /**
     * Method for inserting new cars.
     * @param car: found car
     * @param em 
     */
    private void insertCarIfNeeded(Car car, EntityManager em) {
        if(em.find(Car.class, car.getUnitId()) == null){
            em.persist(car);
        }
    }
    
    /**
     * Since not all the entity classes have a getCar() method,
     * we are going to check if it has one first
     * If so, we are going to check if the car needs to be inserted into the database.
     */
    private void checkIfObjectHasCar(EntityClass object, EntityManager entityManager) {
        try {
            
            Car car = (Car) object.getCar();
            if(car != null){
            insertCarIfNeeded(car, entityManager);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    
    public static void closeConnection() {
        if(entityManagerFactory.isOpen()){
            entityManagerFactory.close();
        }
    } 
        
}
