/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity class for the overall_connections table.
 * Used by the connections CSV.
 * @author School
 */
@Entity
@Table(name = "overall_connections", catalog = "CityGis Data", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OverallConnections.findAll", query = "SELECT o FROM OverallConnections o"),
    @NamedQuery(name = "OverallConnections.findByEventDate", query = "SELECT o FROM OverallConnections o WHERE o.overallConnectionsPK.eventDate = :eventDate"),
    @NamedQuery(name = "OverallConnections.findByUnitId", query = "SELECT o FROM OverallConnections o WHERE o.overallConnectionsPK.unitId = :unitId"),
    @NamedQuery(name = "OverallConnections.findByConnected", query = "SELECT o FROM OverallConnections o WHERE o.connected = :connected")})
public class OverallConnection implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OverallConnectionPK overallConnectionsPK;
    private Boolean connected;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Car car;

    public OverallConnection() {
    }

    public OverallConnection(Date eventDate, String unitId, Boolean connected, Car car) {
        this.overallConnectionsPK = new OverallConnectionPK(eventDate, unitId);
        this.connected = connected;
        this.car = car;
    }

    public OverallConnection(OverallConnectionPK overallConnectionsPK) {
        this.overallConnectionsPK = overallConnectionsPK;
    }

    public OverallConnection(Date eventDate, String unitId) {
        this.overallConnectionsPK = new OverallConnectionPK(eventDate, unitId);
    }

    public OverallConnectionPK getOverallConnectionsPK() {
        return overallConnectionsPK;
    }

    public void setOverallConnectionsPK(OverallConnectionPK overallConnectionsPK) {
        this.overallConnectionsPK = overallConnectionsPK;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
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
        hash += (overallConnectionsPK != null ? overallConnectionsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OverallConnection)) {
            return false;
        }
        OverallConnection other = (OverallConnection) object;
        if ((this.overallConnectionsPK == null && other.overallConnectionsPK != null) || (this.overallConnectionsPK != null && !this.overallConnectionsPK.equals(other.overallConnectionsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.OverallConnections[ overallConnectionsPK=" + overallConnectionsPK + " ]";
    }
    
}
