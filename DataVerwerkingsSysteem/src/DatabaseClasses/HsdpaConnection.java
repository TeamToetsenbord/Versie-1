/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
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
@Table(name = "hsdpa_connections", catalog = "CityGis Data", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HsdpaConnections.findAll", query = "SELECT h FROM HsdpaConnections h"),
    @NamedQuery(name = "HsdpaConnections.findByUnitId", query = "SELECT h FROM HsdpaConnections h WHERE h.hsdpaConnectionsPK.unitId = :unitId"),
    @NamedQuery(name = "HsdpaConnections.findByBeginTime", query = "SELECT h FROM HsdpaConnections h WHERE h.hsdpaConnectionsPK.beginTime = :beginTime"),
    @NamedQuery(name = "HsdpaConnections.findByEndTime", query = "SELECT h FROM HsdpaConnections h WHERE h.hsdpaConnectionsPK.endTime = :endTime"),
    @NamedQuery(name = "HsdpaConnections.findBySqual", query = "SELECT h FROM HsdpaConnections h WHERE h.squal = :squal"),
    @NamedQuery(name = "HsdpaConnections.findByRssi", query = "SELECT h FROM HsdpaConnections h WHERE h.rssi = :rssi"),
    @NamedQuery(name = "HsdpaConnections.findByRscp", query = "SELECT h FROM HsdpaConnections h WHERE h.rscp = :rscp"),
    @NamedQuery(name = "HsdpaConnections.findBySrxlev", query = "SELECT h FROM HsdpaConnections h WHERE h.srxlev = :srxlev"),
    @NamedQuery(name = "HsdpaConnections.findByNumberOfConnections", query = "SELECT h FROM HsdpaConnections h WHERE h.numberOfConnections = :numberOfConnections")})
public class HsdpaConnection implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HsdpaConnectionPK hsdpaConnectionsPK;
    private BigInteger squal = new BigInteger("0");
    private BigInteger rssi = new BigInteger("0");
    private BigInteger rscp = new BigInteger("0");
    private BigInteger srxlev = new BigInteger("0");
    @Column(name = "number_of_connections")
    private Integer numberOfConnections = 0;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Car cars;

    public HsdpaConnection() {
    }

    public HsdpaConnection(HsdpaConnectionPK hsdpaConnectionsPK) {
        this.hsdpaConnectionsPK = hsdpaConnectionsPK;
    }

    public HsdpaConnection(String unitId, Date beginTime, Date endTime) {
        this.hsdpaConnectionsPK = new HsdpaConnectionPK(unitId, beginTime, endTime);
    }

    public HsdpaConnectionPK getHsdpaConnectionsPK() {
        return hsdpaConnectionsPK;
    }

    public void setHsdpaConnectionsPK(HsdpaConnectionPK hsdpaConnectionsPK) {
        this.hsdpaConnectionsPK = hsdpaConnectionsPK;
    }

    public BigInteger getSqual() {
        return squal;
    }

    public void setSqual(BigInteger squal) {
        this.squal = squal;
    }

    public BigInteger getRssi() {
        return rssi;
    }

    public void setRssi(BigInteger rssi) {
        this.rssi = rssi;
    }

    public BigInteger getRscp() {
        return rscp;
    }

    public void setRscp(BigInteger rscp) {
        this.rscp = rscp;
    }

    public BigInteger getSrxlev() {
        return srxlev;
    }

    public void setSrxlev(BigInteger srxlev) {
        this.srxlev = srxlev;
    }

    public Integer getNumberOfConnections() {
        return numberOfConnections;
    }

    public void setNumberOfConnections(Integer numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
    }

    public Car getCars() {
        return cars;
    }

    public void setCars(Car cars) {
        this.cars = cars;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hsdpaConnectionsPK != null ? hsdpaConnectionsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HsdpaConnection)) {
            return false;
        }
        HsdpaConnection other = (HsdpaConnection) object;
        if ((this.hsdpaConnectionsPK == null && other.hsdpaConnectionsPK != null) || (this.hsdpaConnectionsPK != null && !this.hsdpaConnectionsPK.equals(other.hsdpaConnectionsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.HsdpaConnections[ hsdpaConnectionsPK=" + hsdpaConnectionsPK + " ]";
    }
    
}
