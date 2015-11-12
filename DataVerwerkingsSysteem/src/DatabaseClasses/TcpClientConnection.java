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
@Table(name = "tcp_client_connections", catalog = "CityGis Data", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TcpClientConnections.findAll", query = "SELECT t FROM TcpClientConnections t"),
    @NamedQuery(name = "TcpClientConnections.findByUnitId", query = "SELECT t FROM TcpClientConnections t WHERE t.tcpClientConnectionsPK.unitId = :unitId"),
    @NamedQuery(name = "TcpClientConnections.findByBeginTime", query = "SELECT t FROM TcpClientConnections t WHERE t.tcpClientConnectionsPK.beginTime = :beginTime"),
    @NamedQuery(name = "TcpClientConnections.findByEndTime", query = "SELECT t FROM TcpClientConnections t WHERE t.tcpClientConnectionsPK.endTime = :endTime"),
    @NamedQuery(name = "TcpClientConnections.findByRoundTripLatency", query = "SELECT t FROM TcpClientConnections t WHERE t.roundTripLatency = :roundTripLatency"),
    @NamedQuery(name = "TcpClientConnections.findByOutstandingSends", query = "SELECT t FROM TcpClientConnections t WHERE t.outstandingSends = :outstandingSends")})
public class TcpClientConnection implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ConnectionsPK tcpClientConnectionsPK;
    @Column(name = "round_trip_latency")
    private BigInteger roundTripLatency;
    @Column(name = "outstanding_sends")
    private Integer outstandingSends;
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Car car;

    public TcpClientConnection() {
    }

    public TcpClientConnection(ConnectionsPK tcpClientConnectionsPK) {
        this.tcpClientConnectionsPK = tcpClientConnectionsPK;
    }

    public TcpClientConnection(String unitId, Date beginTime, Date endTime) {
        this.tcpClientConnectionsPK = new ConnectionsPK(unitId, beginTime, endTime);
    }

    public TcpClientConnection(String unitId, Date beginTime, Date endTime, BigInteger roundTripLatency, Integer outstandingSends, Car car) {
        this.tcpClientConnectionsPK = new ConnectionsPK(unitId, beginTime, endTime);
        this.roundTripLatency = roundTripLatency;
        this.outstandingSends = outstandingSends;
        this.car = car;
    }

    public ConnectionsPK getTcpClientConnectionsPK() {
        return tcpClientConnectionsPK;
    }

    public void setTcpClientConnectionsPK(ConnectionsPK tcpClientConnectionsPK) {
        this.tcpClientConnectionsPK = tcpClientConnectionsPK;
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
        hash += (tcpClientConnectionsPK != null ? tcpClientConnectionsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcpClientConnection)) {
            return false;
        }
        TcpClientConnection other = (TcpClientConnection) object;
        if ((this.tcpClientConnectionsPK == null && other.tcpClientConnectionsPK != null) || (this.tcpClientConnectionsPK != null && !this.tcpClientConnectionsPK.equals(other.tcpClientConnectionsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.TcpClientConnections[ tcpClientConnectionsPK=" + tcpClientConnectionsPK + " ]";
    }
    
}
