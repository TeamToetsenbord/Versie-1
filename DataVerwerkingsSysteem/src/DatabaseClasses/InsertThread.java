/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import DatabaseClasses.EntityClasses.EntityClass;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Elize
 */
public class InsertThread extends Database_Manager{
    
    /**
     * Not really working. Maybe try something else?
     */
    private List<EntityClass> objectsToPersist = null;
    private boolean running = false;
    @PersistenceContext(unitName="CSVInsertThread") 
    private EntityManager em = null;
        
    public InsertThread(List<EntityClass> objectsToPersistList, EntityManagerFactory emf){
        super();
        em = emf.createEntityManager();
      
        em.getTransaction().begin();
        
        this.objectsToPersist = objectsToPersistList;
        running = true;
        this.start();
    }
    
    public InsertThread(List<EntityClass> objectsToPersistList, EntityManager em){
        super();
        this.em = em;
        em.getTransaction().begin();
        this.objectsToPersist = objectsToPersistList;
        running = true;
        this.start();
    }

    @Override
    public void run(){
        while(running){
        if(!objectsToPersist.isEmpty()){
        super.persistOrUpdateObject(objectsToPersist.get(0), em, objectsToPersist);
        }else{
         stopThread();
        }
        }
    }
    
    public void stopThread(){

        if(em.getTransaction().isActive()){  
          em.getTransaction().commit();
        } 
         em.clear();
         em.close();
         running = false;
         CSVInsertManager.removeThread(this);
    }
    
}
