/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author School
 */
@Embeddable
public class TcpClientConnectionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "unit_id")
    private String unitId;
    @Basic(optional = false)
    @Column(name = "begin_time")
    @Temporal(TemporalType.DATE)
    private Date beginTime;
    @Basic(optional = false)
    @Column(name = "end_time")
    @Temporal(TemporalType.DATE)
    private Date endTime;

    public TcpClientConnectionPK() {
    }

    public TcpClientConnectionPK(String unitId, Date beginTime, Date endTime) {
        this.unitId = unitId;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unitId != null ? unitId.hashCode() : 0);
        hash += (beginTime != null ? beginTime.hashCode() : 0);
        hash += (endTime != null ? endTime.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcpClientConnectionPK)) {
            return false;
        }
        TcpClientConnectionPK other = (TcpClientConnectionPK) object;
        if ((this.unitId == null && other.unitId != null) || (this.unitId != null && !this.unitId.equals(other.unitId))) {
            return false;
        }
        if ((this.beginTime == null && other.beginTime != null) || (this.beginTime != null && !this.beginTime.equals(other.beginTime))) {
            return false;
        }
        if ((this.endTime == null && other.endTime != null) || (this.endTime != null && !this.endTime.equals(other.endTime))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.TcpClientConnectionsPK[ unitId=" + unitId + ", beginTime=" + beginTime + ", endTime=" + endTime + " ]";
    }
    
}
