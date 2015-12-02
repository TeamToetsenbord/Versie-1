/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import java.math.BigInteger;
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
 * @author Elize
 */
@Entity
@Table(name = "car_position_data")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CarPositionData.findAll", query = "SELECT c FROM CarPositionData c"),
    @NamedQuery(name = "CarPositionData.findByUnitId", query = "SELECT c FROM CarPositionData c WHERE c.carPositionDataPK.unitId = :unitId"),
    @NamedQuery(name = "CarPositionData.findByEventDate", query = "SELECT c FROM CarPositionData c WHERE c.carPositionDataPK.eventDate = :eventDate"),
    @NamedQuery(name = "CarPositionData.findByConnectionType", query = "SELECT c FROM CarPositionData c WHERE c.carPositionDataPK.connectionType = :connectionType"),
    @NamedQuery(name = "CarPositionData.findByLatitude", query = "SELECT c FROM CarPositionData c WHERE c.latitude = :latitude"),
    @NamedQuery(name = "CarPositionData.findByLongitude", query = "SELECT c FROM CarPositionData c WHERE c.longitude = :longitude"),
    @NamedQuery(name = "CarPositionData.findBySpeed", query = "SELECT c FROM CarPositionData c WHERE c.speed = :speed"),
    @NamedQuery(name = "CarPositionData.findByCourse", query = "SELECT c FROM CarPositionData c WHERE c.course = :course"),
    @NamedQuery(name = "CarPositionData.findByHdop", query = "SELECT c FROM CarPositionData c WHERE c.hdop = :hdop")})
public class CarPositionData implements Serializable, EntityClass {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CarPositionDataPK carPositionDataPK = null;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Integer speed = null;
    private Integer course = null;
    private Integer hdop = null;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Car car = null;

    public CarPositionData() {
    }

    public CarPositionData(CarPositionDataPK carPositionDataPK) {
        this.carPositionDataPK = carPositionDataPK;
        this.car = new Car(carPositionDataPK.getUnitId());
    }

    public CarPositionData(String unitId, Date eventDate, String connectionType) {
        this.carPositionDataPK = new CarPositionDataPK(unitId, eventDate, connectionType);
        this.car = new Car(unitId);
    }

    /**
     * Constructor for the CSV file reader.
     * @param unitId
     * @param eventDate
     * @param connectionType
     * @param latitude
     * @param longitude
     * @param speed
     * @param course
     * @param hdop 
     */
    public CarPositionData(String unitId, Date eventDate, String connectionType, 
            double latitude, double longitude, Integer speed, 
            Integer course, Integer hdop) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.course = course;
        this.hdop = hdop;
        this.carPositionDataPK = new CarPositionDataPK(unitId, eventDate, connectionType);
        this.car = new Car(unitId);
    }

    public CarPositionDataPK getCarPositionDataPK() {
        return carPositionDataPK;
    }

    public void setCarPositionDataPK(CarPositionDataPK carPositionDataPK) {
        this.carPositionDataPK = carPositionDataPK;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    @Override
    public Object getPK() {
        return this.getCarPositionDataPK();
    }
    
    @Override
    public EntityClass mergeWithObjectFromDatabase(EntityClass ec) {
      
        CarPositionData dbCPD = (CarPositionData) ec;
        
      if(this.carPositionDataPK != null){
            if (dbCPD.carPositionDataPK == null || !this.carPositionDataPK.equals(dbCPD.carPositionDataPK)) {
                dbCPD.setCarPositionDataPK(this.carPositionDataPK);  
            }
        }
      if(this.latitude != 0.0){
            if (dbCPD.latitude == 0.0 || this.latitude != dbCPD.latitude){
                dbCPD.setLatitude(this.latitude);
            }
      }
      
      if(this.longitude != 0.0){
            if (dbCPD.longitude == 0.0 || this.longitude != dbCPD.longitude) {
                dbCPD.setLongitude(this.longitude);
            }
        }
      if(this.speed != null){
            if (dbCPD.speed == null || this.speed.intValue() != dbCPD.speed.intValue()) {
                dbCPD.setSpeed(this.speed);
            }
        }
      if(this.course != null){
            if (dbCPD.course == null || this.course.intValue() != dbCPD.course.intValue()) {
                dbCPD.setCourse(this.course);
            }
        }
      if(this.hdop != null){
            if (dbCPD.hdop == null || this.hdop.intValue() != dbCPD.hdop.intValue()) {
                dbCPD.setHdop(this.hdop);
            }
      }
      
      if(this.car != null){
            if (dbCPD.car == null || !this.car.equals(dbCPD.car)) {
                dbCPD.setCar(this.car);
            }
      }
      
      return dbCPD;
    }
    
}
