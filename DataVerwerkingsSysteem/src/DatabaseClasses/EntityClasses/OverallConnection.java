/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses.EntityClasses;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Elize
 */
@Entity
@Table(name = "overall_connections")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OverallConnection.findAll", query = "SELECT o FROM OverallConnection o"),
    @NamedQuery(name = "OverallConnection.findByEventDate", query = "SELECT o FROM OverallConnection o WHERE o.overallConnectionPK.eventDate = :eventDate"),
    @NamedQuery(name = "OverallConnection.findByUnitId", query = "SELECT o FROM OverallConnection o WHERE o.overallConnectionPK.unitId = :unitId"),
    @NamedQuery(name = "OverallConnection.findByConnected", query = "SELECT o FROM OverallConnection o WHERE o.overallConnectionPK.connected = :connected")})
public class OverallConnection implements Serializable, EntityClass{
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OverallConnectionPK overallConnectionPK = null;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Car car = null;

    public OverallConnection() {
    }

    public OverallConnection(OverallConnectionPK overallConnectionPK) {
        this.overallConnectionPK = overallConnectionPK;
    }

    /**
     * Constructor used by CSV file reader
     * @param eventDate
     * @param unitId
     * @param connected 
     */
    public OverallConnection(Date eventDate, String unitId, boolean connected) {
        this.overallConnectionPK = new OverallConnectionPK(eventDate, unitId, connected);
        this.car = new Car(unitId);
    }

    public OverallConnectionPK getOverallConnectionPK() {
        return overallConnectionPK;
    }

    public void setOverallConnectionPK(OverallConnectionPK overallConnectionPK) {
        this.overallConnectionPK = overallConnectionPK;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (overallConnectionPK != null ? overallConnectionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OverallConnection)) {
            return false;
        }
        OverallConnection other = (OverallConnection) object;
        if ((this.overallConnectionPK == null && other.overallConnectionPK != null) || (this.overallConnectionPK != null && !this.overallConnectionPK.equals(other.overallConnectionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.OverallConnection[ overallConnectionPK=" + overallConnectionPK + " ]";
    }

    @Override
    public Object getPK() {
        return this.getOverallConnectionPK();
    }

    @Override
    public EntityClass mergeWithObjectFromDatabase(EntityClass ec) {
        
        OverallConnection dbOC = (OverallConnection) ec;
        
        if(this.overallConnectionPK != null){
            if (dbOC.overallConnectionPK == null || !this.overallConnectionPK.equals(dbOC.overallConnectionPK)) {
                dbOC.overallConnectionPK = this.overallConnectionPK;
            }
        }
        if(this.car != null){
            if (dbOC.car == null || !this.car.equals(dbOC.car)) {
                dbOC.car = this.car;
            }
        }
        
        return dbOC;

    }

    
}
