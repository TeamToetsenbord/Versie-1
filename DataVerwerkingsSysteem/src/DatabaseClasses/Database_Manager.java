/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

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
        
    private static EntityManagerFactory entityManagerFactory
              = Persistence.createEntityManagerFactory("DataVerwerkingsSysteemPU");
    
    
    public static Object find(Class classToFind, Object objectToFind){
    
        Object foundObject = null;
        try{
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        foundObject = em.find(classToFind, objectToFind);
        em.getTransaction().commit();
        em.clear();
        em.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            return foundObject;
        }
            
    }
       
    @Override
    public void run() {
               
        while(true){
            
            if(!objectsToPersist.isEmpty()){
               persistOrMergeObject(objectsToPersist.get(0));
               
            }
            }
    
              
    }
    
    private void persistOrMergeObject(EntityClass object){
    
        try{
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            if(entityManager.find(object.getClass(),  object.getPK()) != null){
                merge(object, entityManager);
                
            }else{
               persist(object, entityManager);
            }
         
         
            entityManager.getTransaction().commit();
            
            entityManager.clear();
            entityManager.close();
            objectsToPersist.remove(object);
          
        }catch(PersistenceException ex){
            System.out.println(ex);
        
        }catch(Exception ex){
            System.out.println(ex);
        }
         
         
    }
    
    private void merge(EntityClass object, EntityManager entityManager){
       
       
       checkIfObjectHasCar(object, entityManager);
       entityManager.merge(object);
      
    
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

    
}
