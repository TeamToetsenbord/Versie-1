/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

/**
 *
 * @author Elize
 */
public class Database_Manager extends Thread {
    
    private static ArrayList<Object> objectsToPersist = new ArrayList();
        
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
        super.run();
       
        while(true){
            
            if(!objectsToPersist.isEmpty()){
                persist(objectsToPersist.get(0));
            }else{
            
            }
          
        }
    }
    
    /**
     * This is the persist method used by alle entities;
     * @param object: the entity to be inserted. 
     */
    public void persist(Object object) {
       
       
       try{
           
       EntityManager entityManager = entityManagerFactory.createEntityManager();
       entityManager.getTransaction().begin();
       
       /**
        * Since not all the entity classes have a getCar() method,
        * we are going to check if it has one first
        * If so, we are going to check if the car needs to be inserted into the database.
        */
        try {
            Class noParams[] = {};
            Class[] paramString = new Class[1];	
            paramString[0] = String.class;
            Car car = (Car) object.getClass().getDeclaredMethod("getCar", noParams).invoke(object, null);
            insertCarIfNeeded(car, entityManager);
        } catch (Exception ex) {
            System.out.println(ex);
        }
       entityManager.persist(object);
       
       entityManager.getTransaction().commit();
         
       entityManager.clear();
       entityManager.close();
          
       }catch(Exception ex){
       System.out.println(ex);
       }finally{
       objectsToPersist.remove(object);
       }
    }

    public static void addObjectToPersistList(Object object) {
        objectsToPersist.add(object);       
    }
    
    public static void addObjectToPersistListAtFirstPosition(Object object){
        objectsToPersist.add(0, object);
    }

    private void insertCarIfNeeded(Car car, EntityManager em) {
        if(em.find(Car.class, car.getUnitId()) == null){
            em.persist(car);
        }
    }

 


    
    
   
   
}
