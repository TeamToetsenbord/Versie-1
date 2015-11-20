/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import org.omg.CosNaming.NamingContextPackage.NotFound;

/**
 *
 * @author Elize
 */
@Entity
@Table(name = "car_status_events")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CarStatusEvent.findAll", query = "SELECT c FROM CarStatusEvent c"),
    @NamedQuery(name = "CarStatusEvent.findByUnitId", query = "SELECT c FROM CarStatusEvent c WHERE c.carStatusEventPK.unitId = :unitId"),
    @NamedQuery(name = "CarStatusEvent.findByEventDate", query = "SELECT c FROM CarStatusEvent c WHERE c.carStatusEventPK.eventDate = :eventDate"),
    @NamedQuery(name = "CarStatusEvent.findByIgnition", query = "SELECT c FROM CarStatusEvent c WHERE c.ignition = :ignition"),
    @NamedQuery(name = "CarStatusEvent.findByPowerstatus", query = "SELECT c FROM CarStatusEvent c WHERE c.powerstatus = :powerstatus")})
public class CarStatusEvent implements Serializable, EntityClass{
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CarStatusEventPK carStatusEventPK = null;
    @Column(name = "ignition")
    private Boolean ignition = false;
    @Column(name = "powerstatus")
    private Boolean powerstatus = false;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Car car = null;

    public CarStatusEvent() {
    }

    public CarStatusEvent(CarStatusEventPK carStatusEventPK) {
        this.carStatusEventPK = carStatusEventPK;
    }

    public CarStatusEvent(String unitId, Date eventDate) {
        this.carStatusEventPK = new CarStatusEventPK(unitId, eventDate);
    }

    /**
     * Constructor for reading the csv files
     * @param unitId
     * @param eventDate
     * @param ignition
     * @param powerstatus
     */
    public CarStatusEvent(String unitId, Date eventDate, String port, String value) {
        this.carStatusEventPK = new CarStatusEventPK(unitId, eventDate);
        this.car = new Car(unitId);
        if(port.equals("Ignition")){
            this.ignition = (Integer.parseInt(value) != 0);
            this.powerstatus = this.ignition;
        }else if(port.equals("Powerstatus")){
            this.powerstatus = (Integer.parseInt(value) != 0);
            this.ignition = this.powerstatus;
        }
    }
    
    

    public CarStatusEventPK getCarStatusEventPK() {
        return carStatusEventPK;
    }

    public void setCarStatusEventPK(CarStatusEventPK carStatusEventPK) {
        this.carStatusEventPK = carStatusEventPK;
    }

    public Boolean getIgnition() {
        return ignition;
    }

    public void setIgnition(Boolean ignition) {
        this.ignition = ignition;
    }

    public Boolean getPowerstatus() {
        return powerstatus;
    }

    public void setPowerstatus(Boolean powerstatus) {
        this.powerstatus = powerstatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carStatusEventPK != null ? carStatusEventPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CarStatusEvent)) {
            return false;
        }
        CarStatusEvent other = (CarStatusEvent) object;
        if ((this.carStatusEventPK == null && other.carStatusEventPK != null) || (this.carStatusEventPK != null && !this.carStatusEventPK.equals(other.carStatusEventPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.CarStatusEvent[ carStatusEventPK=" + carStatusEventPK + " ]";
    }

    public Car getCar(){
        return car;
    }

    @Override
    public Object getPK() {
            return this.getCarStatusEventPK();
        }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public EntityClass mergeWithObjectFromDatabase(EntityClass ec) {
       
      CarStatusEvent dbCSE = (CarStatusEvent) ec;
      if(this.carStatusEventPK != null){
            if (dbCSE.carStatusEventPK == null || !this.carStatusEventPK.equals(dbCSE.carStatusEventPK)) {
                dbCSE.setCarStatusEventPK(this.carStatusEventPK);
            }
      }  
      
      if(this.ignition != null){
            if (dbCSE.ignition == null || !this.ignition.equals(dbCSE.ignition)) {
                dbCSE.setIgnition(this.ignition);
            }
      }
      
      if(this.powerstatus != null){
            if (dbCSE.powerstatus == null || !this.powerstatus.equals(dbCSE.powerstatus)) {
                dbCSE.setPowerstatus(this.powerstatus);
            }
      }  
      
      if(this.car != null){
            if (dbCSE.car == null || !this.car.equals(dbCSE.car)) {
                dbCSE.setCar(this.car);
            }
      } 
      
      return dbCSE;

      
    }
    
}
