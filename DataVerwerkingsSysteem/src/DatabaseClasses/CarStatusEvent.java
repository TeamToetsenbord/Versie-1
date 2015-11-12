/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entitiy class for the car_status_events database table.
 * Used for the Events CSV Files
 * @author Elize
 */
@Entity
@Table(name = "car_status_events", catalog = "CityGis Data", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CarStatusEvents.findAll", query = "SELECT c FROM CarStatusEvents c"),
    @NamedQuery(name = "CarStatusEvents.findByUnitId", query = "SELECT c FROM CarStatusEvents c WHERE c.carStatusEventsPK.unitId = :unitId"),
    @NamedQuery(name = "CarStatusEvents.findByEventDate", query = "SELECT c FROM CarStatusEvents c WHERE c.carStatusEventsPK.eventDate = :eventDate"),
    @NamedQuery(name = "CarStatusEvents.findByIgnition", query = "SELECT c FROM CarStatusEvents c WHERE c.ignition = :ignition"),
    @NamedQuery(name = "CarStatusEvents.findByPowerstatus", query = "SELECT c FROM CarStatusEvents c WHERE c.powerstatus = :powerstatus")})
public class CarStatusEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CarStatusEventPK carStatusEventPK;
    private Integer ignition;
    private Integer powerstatus;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Car car;

    public CarStatusEvent() {
    }

    public CarStatusEvent(String unitId, String eventDate, Integer ignition, Integer powerstatus, Car car) {
        this.carStatusEventPK = new CarStatusEventPK(unitId, eventDate);
        this.ignition = ignition;
        this.powerstatus = powerstatus;
        this.car = car;
    }

    public CarStatusEvent(CarStatusEventPK carStatusEventsPK) {
        this.carStatusEventPK = carStatusEventsPK;
    }

    public CarStatusEvent(String unitId, String eventDate) {
        this.carStatusEventPK = new CarStatusEventPK(unitId, eventDate);
    }

    public CarStatusEventPK getCarStatusEventsPK() {
        return carStatusEventPK;
    }

    public void setCarStatusEventsPK(CarStatusEventPK carStatusEventsPK) {
        this.carStatusEventPK = carStatusEventsPK;
    }

    public Integer getIgnition() {
        return ignition;
    }

    public void setIgnition(Integer ignition) {
        this.ignition = ignition;
    }

    public Integer getPowerstatus() {
        return powerstatus;
    }

    public void setPowerstatus(Integer powerstatus) {
        this.powerstatus = powerstatus;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car cars) {
        this.car = cars;
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
        return "DatabaseClasses.CarStatusEvents[ carStatusEventsPK=" + carStatusEventPK + " ]";
    }
    
}
