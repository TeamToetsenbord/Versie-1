/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses.EntityClasses;

import javax.persistence.EntityManager;

/**
 *
 * @author Elize
 */
public interface EntityClass {
    
    /**
     * Get the primary key of the entity object.
     * @return the PK
     */
    Object getPK();
    
    /**
     * Get the car the entitiy object.
     * @return if the object has a car, the car.
     * Otherwise, null;
     */
    
    Car getCar();
    
    /**
     * Method for merging the current object with the object on the database.
     * If a field is not null, and not te same, the value is updated.
     * @param ec the object from te database.
     * @return object to merge on database.
     */
    EntityClass mergeWithObjectFromDatabase(EntityClass ec);
    
       
}
