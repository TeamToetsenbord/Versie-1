/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.CascadeType;
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
@Table(name = "car_position_data")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CarPositionData.findAll", query = "SELECT c FROM CarPositionData c"),
    @NamedQuery(name = "CarPositionData.findByUnitId", query = "SELECT c FROM CarPositionData c WHERE c.carPositionDataPK.unitId = :unitId"),
    @NamedQuery(name = "CarPositionData.findByEventDate", query = "SELECT c FROM CarPositionData c WHERE c.carPositionDataPK.eventDate = :eventDate"),
    @NamedQuery(name = "CarPositionData.findByLatitude", query = "SELECT c FROM CarPositionData c WHERE c.latitude = :latitude"),
    @NamedQuery(name = "CarPositionData.findByLongitude", query = "SELECT c FROM CarPositionData c WHERE c.longitude = :longitude"),
    @NamedQuery(name = "CarPositionData.findBySpeed", query = "SELECT c FROM CarPositionData c WHERE c.speed = :speed"),
    @NamedQuery(name = "CarPositionData.findByCourse", query = "SELECT c FROM CarPositionData c WHERE c.course = :course"),
    @NamedQuery(name = "CarPositionData.findByHdop", query = "SELECT c FROM CarPositionData c WHERE c.hdop = :hdop")})

public class CarPositionData implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CarPositionDataPK carPositionDataPK;
    private BigInteger latitude;
    private BigInteger longitude;
    private Integer speed;
    private Integer course;
    private Integer hdop;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Car car;
    @JoinColumn(name = "connection_type_id", referencedColumnName = "connection_type_id")
    @ManyToOne
    private ConnectionType connectionTypeId;

    public CarPositionData() {
    }

    public CarPositionData(CarPositionDataPK carPositionDataPK) {
        this.carPositionDataPK = carPositionDataPK;
    }

    /**
     * Constructor for the CSV reader
     * @param unitId
     * @param eventDate
     * @param latitude
     * @param longitude
     * @param speed
     * @param course
     * @param hdop
     * @param connectionTypeId 
     */
    public CarPositionData(String unitId, Date eventDate, BigInteger latitude, BigInteger longitude, Integer speed, Integer course, Integer hdop, ConnectionType connectionTypeId) {
        this.carPositionDataPK = new CarPositionDataPK(unitId, eventDate);
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.course = course;
        this.hdop = hdop;
        this.car = new Car(unitId);
        this.connectionTypeId = connectionTypeId;
    }

    public CarPositionData(String unitId, Date eventDate) {
        this.carPositionDataPK = new CarPositionDataPK(unitId, eventDate);
    }

    public CarPositionDataPK getCarPositionDataPK() {
        return carPositionDataPK;
    }

    public void setCarPositionDataPK(CarPositionDataPK carPositionDataPK) {
        this.carPositionDataPK = carPositionDataPK;
    }

    public BigInteger getLatitude() {
        return latitude;
    }

    public void setLatitude(BigInteger latitude) {
        this.latitude = latitude;
    }

    public BigInteger getLongitude() {
        return longitude;
    }

    public void setLongitude(BigInteger longitude) {
        this.longitude = longitude;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Integer getHdop() {
        return hdop;
    }

    public void setHdop(Integer hdop) {
        this.hdop = hdop;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public ConnectionType getConnectionTypeId() {
        return connectionTypeId;
    }

    public void setConnectionTypeId(ConnectionType connectionTypeId) {
        this.connectionTypeId = connectionTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carPositionDataPK != null ? carPositionDataPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CarPositionData)) {
            return false;
        }
        CarPositionData other = (CarPositionData) object;
        if ((this.carPositionDataPK == null && other.carPositionDataPK != null) || (this.carPositionDataPK != null && !this.carPositionDataPK.equals(other.carPositionDataPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.CarPositionData[ carPositionDataPK=" + carPositionDataPK + " ]";
    }
    
}
