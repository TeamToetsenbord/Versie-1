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
 * @author Elize
 */
@Entity
@Table(name = "hsdpa_connections")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HsdpaConnection.findAll", query = "SELECT h FROM HsdpaConnection h"),
    @NamedQuery(name = "HsdpaConnection.findByUnitId", query = "SELECT h FROM HsdpaConnection h WHERE h.hsdpaConnectionPK.unitId = :unitId"),
    @NamedQuery(name = "HsdpaConnection.findBySqual", query = "SELECT h FROM HsdpaConnection h WHERE h.squal = :squal"),
    @NamedQuery(name = "HsdpaConnection.findByRssi", query = "SELECT h FROM HsdpaConnection h WHERE h.rssi = :rssi"),
    @NamedQuery(name = "HsdpaConnection.findByRscp", query = "SELECT h FROM HsdpaConnection h WHERE h.rscp = :rscp"),
    @NamedQuery(name = "HsdpaConnection.findBySrxlev", query = "SELECT h FROM HsdpaConnection h WHERE h.srxlev = :srxlev"),
    @NamedQuery(name = "HsdpaConnection.findByNumberOfConnections", query = "SELECT h FROM HsdpaConnection h WHERE h.numberOfConnections = :numberOfConnections")})
public class HsdpaConnection implements Serializable, EntityClass{
    @Column(name = "squal")
    private BigInteger squal = null;
    @Column(name = "rssi")
    private BigInteger rssi = null;
    @Column(name = "rscp")
    private BigInteger rscp = null;
    @Column(name = "srxlev")
    private BigInteger srxlev = null;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HsdpaConnectionPK hsdpaConnectionPK = null;
    @Column(name = "number_of_connections")
    private Integer numberOfConnections = null;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Car car = null;

    public HsdpaConnection() {
    }

    public HsdpaConnection(HsdpaConnectionPK hsdpaConnectionPK) {
        this.hsdpaConnectionPK = hsdpaConnectionPK;
        this.car = new Car(this.hsdpaConnectionPK.getUnitId());
    }

    public HsdpaConnection(String unitId, Date date) {
        this.hsdpaConnectionPK = new HsdpaConnectionPK(unitId, date);
        this.car = new Car(unitId);
    }

    /**
     * Constructor for the csv file reader
     * @param unitId
     * @param beginTime
     * @param endTime
     * @param squal
     * @param rssi
     * @param rscp
     * @param srxlev
     * @param numberOfConnections
     * @param car 
     */
    public HsdpaConnection(String unitId, Date date, BigInteger squal, BigInteger rssi, BigInteger rscp, BigInteger srxlev, Integer numberOfConnections) {
        this.squal = squal;
        this.rssi = rssi;
        this.rscp = rscp;
        this.srxlev = srxlev;
        this.hsdpaConnectionPK = new HsdpaConnectionPK(unitId, date);
        this.numberOfConnections = numberOfConnections;
        
        this.car = new Car(unitId);
    }

    public HsdpaConnectionPK getHsdpaConnectionPK() {
        return hsdpaConnectionPK;
    }

    public void setHsdpaConnectionPK(HsdpaConnectionPK hsdpaConnectionPK) {
        this.hsdpaConnectionPK = hsdpaConnectionPK;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hsdpaConnectionPK != null ? hsdpaConnectionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HsdpaConnection)) {
            return false;
        }
        HsdpaConnection other = (HsdpaConnection) object;
        if ((this.hsdpaConnectionPK == null && other.hsdpaConnectionPK != null) || (this.hsdpaConnectionPK != null && !this.hsdpaConnectionPK.equals(other.hsdpaConnectionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.HsdpaConnection[ hsdpaConnectionPK=" + hsdpaConnectionPK + " ]";
    }

    @Override
    public Object getPK() {
        return this.getHsdpaConnectionPK();
    }

    @Override
    public EntityClass mergeWithObjectFromDatabase(EntityClass ec) {
        
        HsdpaConnection dbHC = (HsdpaConnection) ec;
                
        if(this.hsdpaConnectionPK != null){
            if (dbHC.hsdpaConnectionPK == null || 
                    !this.hsdpaConnectionPK.equals(dbHC.hsdpaConnectionPK)) {
                dbHC.hsdpaConnectionPK = this.hsdpaConnectionPK;
            }
        }
        if(this.squal != null){
            if (dbHC.squal == null || this.squal.intValue() != dbHC.squal.intValue()) {
                dbHC.squal = this.squal;
            }
        }
               
        if(this.rssi != null){
            if (dbHC.rssi == null || this.rssi.compareTo(dbHC.rssi) != 0) {
                dbHC.rssi = this.rssi;
            }                   
        }
        
        if(this.rscp != null){
            if (dbHC.rscp == null || this.rscp.compareTo(dbHC.rscp) != 0) {
                dbHC.rscp = this.rscp;
            }
        }
        
        if(this.srxlev != null ){
            if (dbHC.srxlev == null || this.srxlev.compareTo(dbHC.srxlev) != 0) {
                dbHC.srxlev = this.srxlev;
            }
        }
        
        if(this.numberOfConnections != null){
            if (dbHC.numberOfConnections == null || 
                    this.numberOfConnections.compareTo(dbHC.numberOfConnections.intValue()) != 0) {
                dbHC.numberOfConnections = this.numberOfConnections;
            }
        }
        if(this.car != null){
            if (dbHC.car == null || !this.car.equals(dbHC.car)) {
                dbHC.car = this.car;
            }
        }
        
        return dbHC;

    }


    
}
