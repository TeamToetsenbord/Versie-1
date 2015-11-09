/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author School
 */
@Entity
@Table(catalog = "CityGis Data", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cars.findAll", query = "SELECT c FROM Cars c"),
    @NamedQuery(name = "Cars.findByUnitId", query = "SELECT c FROM Cars c WHERE c.unitId = :unitId")})
public class Car implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "unit_id")
    private String unitId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
    private Collection<CarStatusEvents> carStatusEventsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cars")
    private Collection<HsdpaConnection> hsdpaConnectionsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
    private Collection<GpsPositionData> gpsPositionDataCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
    private Collection<OverallConnection> overallConnectionsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cars")
    private Collection<CarPositionData> carPositionDataCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
    private Collection<TcpClientConnection> tcpClientConnectionsCollection;

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
    public Collection<CarStatusEvents> getCarStatusEventsCollection() {
        return carStatusEventsCollection;
    }

    public void setCarStatusEventsCollection(Collection<CarStatusEvents> carStatusEventsCollection) {
        this.carStatusEventsCollection = carStatusEventsCollection;
    }

    @XmlTransient
    public Collection<HsdpaConnection> getHsdpaConnectionsCollection() {
        return hsdpaConnectionsCollection;
    }

    public void setHsdpaConnectionsCollection(Collection<HsdpaConnection> hsdpaConnectionsCollection) {
        this.hsdpaConnectionsCollection = hsdpaConnectionsCollection;
    }

    @XmlTransient
    public Collection<GpsPositionData> getGpsPositionDataCollection() {
        return gpsPositionDataCollection;
    }

    public void setGpsPositionDataCollection(Collection<GpsPositionData> gpsPositionDataCollection) {
        this.gpsPositionDataCollection = gpsPositionDataCollection;
    }

    @XmlTransient
    public Collection<OverallConnection> getOverallConnectionsCollection() {
        return overallConnectionsCollection;
    }

    public void setOverallConnectionsCollection(Collection<OverallConnection> overallConnectionsCollection) {
        this.overallConnectionsCollection = overallConnectionsCollection;
    }

    @XmlTransient
    public Collection<CarPositionData> getCarPositionDataCollection() {
        return carPositionDataCollection;
    }

    public void setCarPositionDataCollection(Collection<CarPositionData> carPositionDataCollection) {
        this.carPositionDataCollection = carPositionDataCollection;
    }

    @XmlTransient
    public Collection<TcpClientConnection> getTcpClientConnectionsCollection() {
        return tcpClientConnectionsCollection;
    }

    public void setTcpClientConnectionsCollection(Collection<TcpClientConnection> tcpClientConnectionsCollection) {
        this.tcpClientConnectionsCollection = tcpClientConnectionsCollection;
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
        return "DatabaseClasses.Cars[ unitId=" + unitId + " ]";
    }
    
}
