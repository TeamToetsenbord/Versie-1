/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Elize
 */
public class InsertThread extends Database_Manager{
    
    /**
     * Not really working. Maybe try something else?
     */
    List<EntityClass> objectsToPersist = null;
    boolean running = false;
    
    public InsertThread(List<EntityClass> objectsToPersistList){
        super(null);
        this.objectsToPersist = objectsToPersistList;
        running = true;
        this.start();
    }

    @Override
    public void run() {
        
        while(running){
        if(objectsToPersist.isEmpty()){
        super.persistOrUpdateObject(objectsToPersist.get(0));
        }else{
         running = false;
        }
        }
    }
    
    
    
    
}
