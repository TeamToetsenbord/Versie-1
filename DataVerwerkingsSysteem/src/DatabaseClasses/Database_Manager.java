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
    
    private static ArrayList<EntityClass> objectsToPersist = new ArrayList();
    private static final int OBJECTSPERTRANSACTION = 100;
    private static EntityManagerFactory entityManagerFactory
              = Persistence.createEntityManagerFactory("DataVerwerkingsSysteemPU");
    private EntityManager entityManager = null;
    private int count = 0;
    private User_Interface ui = null;
    
    public Database_Manager(User_Interface ui){
        this.ui = ui;
    }
    
    
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
            if(count > 0){
            entityManager.joinTransaction();
            }else if(count == 0 || currentSize < OBJECTSPERTRANSACTION){
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
            if(count == 0 || currentSize == OBJECTSPERTRANSACTION){
            entityManager.getTransaction().commit();
            entityManager.clear();
            entityManager.close();
            count = OBJECTSPERTRANSACTION;
            }
            
         
        }catch(PersistenceException ex){
            System.out.println(ex);
        
        }catch(Exception ex){
            System.out.println("Object: " + object + ". Exception: " + ex);
        }finally{
            objectsToPersist.remove(object);
        }
    }
    
    /**
     * Method for updating the object.
     * Only contrasting values are inserted.
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
     * This is the persist method used by alle entities;
     * @param object: the entity to be inserted. 
     */
    public void persist(EntityClass object, EntityManager entityManager){
              
       checkIfObjectHasCar(object, entityManager);
       entityManager.persist(object);
       
    }

    public static void addObjectToPersistList(EntityClass object) {
        objectsToPersist.add(object); 
        
    }
    
    public static void addObjectToPersistListAtFirstPosition(EntityClass object){
        objectsToPersist.add(0, object);
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
