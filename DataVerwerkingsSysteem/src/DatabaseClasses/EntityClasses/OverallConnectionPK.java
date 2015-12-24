/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses.EntityClasses;

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
public class OverallConnectionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "event_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate = null;
    @Basic(optional = false)
    @Column(name = "unit_id")
    private String unitId = null;
    @Basic(optional = false)
    @Column(name = "connected")
    private boolean connected;

    public OverallConnectionPK() {
    }

    public OverallConnectionPK(Date eventDate, String unitId, boolean connected) {
        this.eventDate = eventDate;
        this.unitId = unitId;
        this.connected = connected;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public boolean getConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventDate != null ? eventDate.hashCode() : 0);
        hash += (unitId != null ? unitId.hashCode() : 0);
        hash += (connected ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OverallConnectionPK)) {
            return false;
        }
        OverallConnectionPK other = (OverallConnectionPK) object;
        if ((this.eventDate == null && other.eventDate != null) || (this.eventDate != null && !this.eventDate.equals(other.eventDate))) {
            return false;
        }
        if ((this.unitId == null && other.unitId != null) || (this.unitId != null && !this.unitId.equals(other.unitId))) {
            return false;
        }
        if (this.connected != other.connected) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.OverallConnectionPK[ eventDate=" + eventDate + ", unitId=" + unitId + ", connected=" + connected + " ]";
    }
    
  
    
}
