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
@Table(name = "tcp_client_connections")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TcpClientConnection.findAll", query = "SELECT t FROM TcpClientConnection t"),
    @NamedQuery(name = "TcpClientConnection.findByUnitId", query = "SELECT t FROM TcpClientConnection t WHERE t.tcpClientConnectionPK.unitId = :unitId"),
    @NamedQuery(name = "TcpClientConnection.findByRoundTripLatency", query = "SELECT t FROM TcpClientConnection t WHERE t.roundTripLatency = :roundTripLatency"),
    @NamedQuery(name = "TcpClientConnection.findByOutstandingSends", query = "SELECT t FROM TcpClientConnection t WHERE t.outstandingSends = :outstandingSends")})
public class TcpClientConnection implements Serializable, EntityClass{
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TcpClientConnectionPK tcpClientConnectionPK = null;
    @Column(name = "round_trip_latency")
    private BigInteger roundTripLatency = null;
    @Column(name = "outstanding_sends")
    private Integer outstandingSends = null;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Car car = null;

    public TcpClientConnection() {
    }

    public TcpClientConnection(TcpClientConnectionPK tcpClientConnectionPK) {
        this.tcpClientConnectionPK = tcpClientConnectionPK;
    }

    /**
     *
     * @param unitId
     * @param beginTime
     * @param endTime
     * @param roundTripLatency
     * @param outstandingSends
     */
    public TcpClientConnection(String unitId, Date date, BigInteger roundTripLatency, Integer outstandingSends) {
        this.roundTripLatency = roundTripLatency;
        this.outstandingSends = outstandingSends;
        this.car = new Car(unitId);
        this.tcpClientConnectionPK = new TcpClientConnectionPK(unitId, date);
    }
    
    

    public TcpClientConnection(String unitId, Date date) {
        this.tcpClientConnectionPK = new TcpClientConnectionPK(unitId, date);
    }

    public TcpClientConnection(String unitId, Date date, BigInteger roundTripLatency, Integer outstandingSends, Car car) {
        this.tcpClientConnectionPK = new TcpClientConnectionPK(unitId, date);
        this.roundTripLatency = roundTripLatency;
        this.outstandingSends = outstandingSends;
        this.car = car;
    }

    public TcpClientConnectionPK getTcpClientConnectionPK() {
        return tcpClientConnectionPK;
    }

    public void setTcpClientConnectionPK(TcpClientConnectionPK tcpClientConnectionPK) {
        this.tcpClientConnectionPK = tcpClientConnectionPK;
    }

    public BigInteger getRoundTripLatency() {
        return roundTripLatency;
    }

    public void setRoundTripLatency(BigInteger roundTripLatency) {
        this.roundTripLatency = roundTripLatency;
    }

    public Integer getOutstandingSends() {
        return outstandingSends;
    }

    public void setOutstandingSends(Integer outstandingSends) {
        this.outstandingSends = outstandingSends;
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
        hash += (tcpClientConnectionPK != null ? tcpClientConnectionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcpClientConnection)) {
            return false;
        }
        TcpClientConnection other = (TcpClientConnection) object;
        if ((this.tcpClientConnectionPK == null && other.tcpClientConnectionPK != null) || (this.tcpClientConnectionPK != null && !this.tcpClientConnectionPK.equals(other.tcpClientConnectionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.TcpClientConnection[ tcpClientConnectionPK=" + tcpClientConnectionPK + " ]";
    }

    @Override
    public Object getPK() {
        return this.getTcpClientConnectionPK();
    }

    @Override
    public EntityClass mergeWithObjectFromDatabase(EntityClass ec) {
        
        TcpClientConnection dbTCC = (TcpClientConnection) ec;
        
        if(this.tcpClientConnectionPK != null){
            if (dbTCC.tcpClientConnectionPK == null || !this.tcpClientConnectionPK.equals(dbTCC.tcpClientConnectionPK)) {
                dbTCC.tcpClientConnectionPK = this.tcpClientConnectionPK;
            }
        }
        
        if(this.roundTripLatency != null){
            if (dbTCC.roundTripLatency == null || this.roundTripLatency.compareTo(dbTCC.roundTripLatency) != 0) {
                dbTCC.roundTripLatency = this.roundTripLatency;
            }
        }
        
        if(this.outstandingSends != null){
            if (dbTCC.outstandingSends == null || this.outstandingSends.compareTo(dbTCC.outstandingSends) != 0) {
                dbTCC.outstandingSends = this.outstandingSends;
            }
        }
        
        if(this.car != null){
            if (dbTCC.car == null || !this.car.equals(dbTCC.car)) {
                dbTCC.car = this.car;
            }
        }

        return dbTCC;

    }
    
}
