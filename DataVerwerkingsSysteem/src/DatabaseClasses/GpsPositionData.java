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
 *
 * @author School
 */
@Entity
@Table(name = "gps_position_data", catalog = "CityGis Data", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GpsPositionData.findAll", query = "SELECT g FROM GpsPositionData g"),
    @NamedQuery(name = "GpsPositionData.findByUnitId", query = "SELECT g FROM GpsPositionData g WHERE g.gpsPositionDataPK.unitId = :unitId"),
    @NamedQuery(name = "GpsPositionData.findByEventDate", query = "SELECT g FROM GpsPositionData g WHERE g.gpsPositionDataPK.eventDate = :eventDate"),
    @NamedQuery(name = "GpsPositionData.findBySpeed", query = "SELECT g FROM GpsPositionData g WHERE g.speed = :speed"),
    @NamedQuery(name = "GpsPositionData.findByHdop", query = "SELECT g FROM GpsPositionData g WHERE g.hdop = :hdop"),
    @NamedQuery(name = "GpsPositionData.findByCourse", query = "SELECT g FROM GpsPositionData g WHERE g.course = :course")})
public class GpsPositionData implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GpsPositionDataPK gpsPositionDataPK;
    private Integer speed = 0;
    private Integer hdop = 0;
    private Integer course = 0;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Car car;

    public GpsPositionData() {
    }

    public GpsPositionData(GpsPositionDataPK gpsPositionDataPK) {
        this.gpsPositionDataPK = gpsPositionDataPK;
    }

    public GpsPositionData(String unitId, Date eventDate) {
        this.gpsPositionDataPK = new GpsPositionDataPK(unitId, eventDate);
    }

    public GpsPositionDataPK getGpsPositionDataPK() {
        return gpsPositionDataPK;
    }

    public void setGpsPositionDataPK(GpsPositionDataPK gpsPositionDataPK) {
        this.gpsPositionDataPK = gpsPositionDataPK;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getHdop() {
        return hdop;
    }

    public void setHdop(Integer hdop) {
        this.hdop = hdop;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
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
        hash += (gpsPositionDataPK != null ? gpsPositionDataPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GpsPositionData)) {
            return false;
        }
        GpsPositionData other = (GpsPositionData) object;
        if ((this.gpsPositionDataPK == null && other.gpsPositionDataPK != null) || (this.gpsPositionDataPK != null && !this.gpsPositionDataPK.equals(other.gpsPositionDataPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.GpsPositionData[ gpsPositionDataPK=" + gpsPositionDataPK + " ]";
    }
    
}
