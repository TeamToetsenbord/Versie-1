/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses.EntityClasses;

import DatabaseClasses.EntityClasses.TcpClientConnection;
import DatabaseClasses.EntityClasses.OverallConnection;
import DatabaseClasses.EntityClasses.HsdpaConnection;
import DatabaseClasses.EntityClasses.EntityClass;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Elize
 */
@Entity
@Table(name = "cars")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Car.findAll", query = "SELECT c FROM Car c"),
    @NamedQuery(name = "Car.findByUnitId", query = "SELECT c FROM Car c WHERE c.unitId = :unitId")})
public class Car implements Serializable, EntityClass {
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "car")
    private Collection<OverallConnection> overallConnectionsCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "unit_id")
    private String unitId = null;
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "car")
    private Collection<CarStatusEvent> carStatusEventCollection = null;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "car")
    private Collection<HsdpaConnection> hsdpaConnectionCollection = null;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "car")
    private Collection<OverallConnection> overallConnectionCollection = null;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "car")
    private Collection<CarPositionData> carPositionDataCollection = null;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "car")
    private Collection<TcpClientConnection> tcpClientConnectionCollection = null;

    public Car() {
    }

    public Car(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @XmlTransient
    public Collection<CarStatusEvent> getCarStatusEventCollection() {
        return carStatusEventCollection;
    }

    public void setCarStatusEventCollection(Collection<CarStatusEvent> carStatusEventCollection) {
        this.carStatusEventCollection = carStatusEventCollection;
    }

    @XmlTransient
    public Collection<HsdpaConnection> getHsdpaConnectionCollection() {
        return hsdpaConnectionCollection;
    }

    public void setHsdpaConnectionCollection(Collection<HsdpaConnection> hsdpaConnectionCollection) {
        this.hsdpaConnectionCollection = hsdpaConnectionCollection;
    }

    @XmlTransient
    public Collection<OverallConnection> getOverallConnectionCollection() {
        return overallConnectionCollection;
    }

    public void setOverallConnectionCollection(Collection<OverallConnection> overallConnectionCollection) {
        this.overallConnectionCollection = overallConnectionCollection;
    }

    @XmlTransient
    public Collection<CarPositionData> getCarPositionDataCollection() {
        return carPositionDataCollection;
    }

    public void setCarPositionDataCollection(Collection<CarPositionData> carPositionDataCollection) {
        this.carPositionDataCollection = carPositionDataCollection;
    }

    @XmlTransient
    public Collection<TcpClientConnection> getTcpClientConnectionCollection() {
        return tcpClientConnectionCollection;
    }

    public void setTcpClientConnectionCollection(Collection<TcpClientConnection> tcpClientConnectionCollection) {
        this.tcpClientConnectionCollection = tcpClientConnectionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unitId != null ? unitId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.unitId == null && other.unitId != null) || (this.unitId != null && !this.unitId.equals(other.unitId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.Car[ unitId=" + unitId + " ]";
    }

    @XmlTransient
    public Collection<OverallConnection> getOverallConnectionsCollection() {
        return overallConnectionsCollection;
    }

    public void setOverallConnectionsCollection(Collection<OverallConnection> overallConnectionsCollection) {
        this.overallConnectionsCollection = overallConnectionsCollection;
    }

    @Override
    public Object getPK() {
        return this.getUnitId();
    }

    @Override
    public Car getCar() {
        return null;
    }

    @Override
    public EntityClass mergeWithObjectFromDatabase(EntityClass ec) {
        Car dbCar = (Car) ec;
        if(this.unitId != null ){
            if (dbCar.unitId == null || !this.unitId.equals(dbCar.getUnitId())){
                dbCar.setUnitId(this.unitId);
            }
        }
        
        if(this.carStatusEventCollection != null){
            if (dbCar.carStatusEventCollection == null){
                dbCar.carStatusEventCollection = this.carStatusEventCollection;
                
            }else if(!dbCar.getCarStatusEventCollection().containsAll(carStatusEventCollection)){
                addObjectToCollectionIfNeeded(dbCar.carStatusEventCollection,
                        this.carStatusEventCollection);
            }
        }
        
        if(this.hsdpaConnectionCollection != null){
            if (dbCar.hsdpaConnectionCollection == null) {
                dbCar.hsdpaConnectionCollection = this.hsdpaConnectionCollection;
            }else if (!dbCar.getHsdpaConnectionCollection().containsAll(
                        hsdpaConnectionCollection)) {
                    addObjectToCollectionIfNeeded(dbCar.hsdpaConnectionCollection,
                            this.hsdpaConnectionCollection);
                }
        }
        
        if(this.overallConnectionCollection != null){
            if (dbCar.overallConnectionCollection == null) {
                dbCar.overallConnectionCollection = this.overallConnectionCollection;
            }else if (!dbCar.getCarPositionDataCollection().containsAll(
                        overallConnectionCollection)) {
                    addObjectToCollectionIfNeeded(dbCar.carPositionDataCollection,
                            this.overallConnectionCollection);
            }
        }
        
        if(this.carPositionDataCollection != null){          
            if(dbCar.carPositionDataCollection == null){
               dbCar.carPositionDataCollection = this.carPositionDataCollection;
            }else if (!dbCar.getCarPositionDataCollection().containsAll(
                        carPositionDataCollection)){
                    addObjectToCollectionIfNeeded(dbCar.carPositionDataCollection,
                            this.carPositionDataCollection);
            }
        }
        
        if(this.tcpClientConnectionCollection != null){
            if (dbCar.tcpClientConnectionCollection == null) {
                dbCar.tcpClientConnectionCollection = this.tcpClientConnectionCollection;
            }else if (!dbCar.getCarPositionDataCollection().containsAll(
                        tcpClientConnectionCollection)){
                    addObjectToCollectionIfNeeded(dbCar.tcpClientConnectionCollection,
                            this.tcpClientConnectionCollection);
            }
        }
        
        return dbCar;
    }
    
    private void addObjectToCollectionIfNeeded(
            Collection<?> dbCollection, Collection<?> thisCollection){
            Collection<EntityClass> databaseCollection 
                    = (Collection<EntityClass>) dbCollection;
            
            for(Object object: thisCollection){
                if(!databaseCollection.contains(object)){
                    databaseCollection.add((EntityClass)object);
                }
            }
    }
 }
